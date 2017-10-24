package com.sssta.huajia.service;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jsms.api.JSMSClient;
import cn.jsms.api.SendSMSResult;
import cn.jsms.api.ValidSMSResult;
import cn.jsms.api.common.model.SMSPayload;
import com.sssta.huajia.exception.JGException;
import com.stephen.a2.exception.UnAuthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class JSMSServiceImpl extends JedisService implements JSMSService {

    private JSMSClient client;

    public static final String REDIS_VERIFIED_KEY = "huajia_verified";

    @Autowired
    public JSMSServiceImpl(JSMSClient client, JedisPool jedisPool) {
        super(jedisPool);
        this.client = client;
    }

    @Override
    public String sendValidCode(String phone) {
        SMSPayload payload = SMSPayload.newBuilder().
                setMobileNumber(phone).
                setTempId(1).
                setTTL(30).build();
        try (Jedis jedis = getJedis()) {
            SendSMSResult result = client.sendSMSCode(payload);
            String msgId = result.getMessageId();
            jedis.sadd(REDIS_VERIFIED_KEY, phone + "-" + msgId.replace("-", ""));
            return result.getMessageId();
        } catch (APIConnectionException | APIRequestException e) {
            throw new JGException(e);
        }
    }

    @Override
    public boolean isCodeValid(String phone, String msgId, String code) {
        try (Jedis jedis = jedisPool.getResource()) {
            checkIfInVerifiedList(jedis, phone, msgId);
            ValidSMSResult result = client.sendValidSMSCode(msgId, code);
            boolean value = result.getIsValid();
            if (value) {
                jedis.srem(REDIS_VERIFIED_KEY, phone + "-" + msgId.replace("-", ""));
            }
            return value;
        } catch (APIConnectionException | APIRequestException e) {
            throw new JGException(e);
        }
    }

    private void checkIfInVerifiedList(Jedis jedis, String phone, String msgId) {
        String s = msgId.replace("-", "");
        if (!jedis.sismember(REDIS_VERIFIED_KEY, phone + "-" + s)) {
            throw new UnAuthorizedException();
        }
    }
}
