package com.sssta.huajia.service;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import com.sssta.huajia.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
@Transactional
public class JPushServiceImpl implements JPushService {

    private final UserRepository userDAO;
    private final JPushClient client;
    private final JedisPool pool;

    private static final String TYPE_BIND = "bind";

    public static final String REDIS_UNDETERMINED_BINDING_KEY = "huajia-undetermined-binding";

    @Autowired
    public JPushServiceImpl(UserRepository userDAO, JPushClient client, JedisPool pool) {
        this.userDAO = userDAO;
        this.client = client;
        this.pool = pool;
    }

    @Override
    @Async("asyncExecutor")
    public void bind(String oldPhone, String youngPhone) {
        String target = userDAO.getRegistrationIdByPhone(youngPhone);
        try (Jedis jedis = pool.getResource()) {
            client.sendPush(bindPushPayload(oldPhone, target));
            jedis.sadd(REDIS_UNDETERMINED_BINDING_KEY, oldPhone + "-" + youngPhone);
        } catch (APIConnectionException | APIRequestException e) {
            // 应该用Logger记录下来
            e.printStackTrace();
        }
    }

    private PushPayload bindPushPayload(String phone, String registrationId) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.registrationId(registrationId))
                .setMessage(Message.newBuilder()
                        .setContentType(TYPE_BIND)
                        .setTitle("有一个绑定请求")
                        .setMsgContent("收到绑定请求，请及时处理")
                        .addExtra("phone", phone)
                        .build())
                .build();
    }
}
