package com.sssta.huajia.service;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import com.sssta.huajia.Constants;
import com.sssta.huajia.dao.UserRepository;
import com.sssta.huajia.dto.RemindBody;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@Transactional
public class PushServiceImpl extends JedisService implements PushService {

    private final UserRepository userDAO;
    private final JPushClient client;
    private final Scheduler scheduler;

    @Autowired
    public PushServiceImpl(UserRepository userDAO, JPushClient client, JedisPool pool, Scheduler scheduler) {
        super(pool);
        this.userDAO = userDAO;
        this.client = client;
        this.scheduler = scheduler;
    }

    @Override
    @Async(Constants.ASYNC_EXECUTOR)
    public void bind(String oldPhone, String youngPhone) {
        String target = userDAO.getRegistrationIdByPhone(youngPhone);
        try (Jedis jedis = getJedis()) {
            client.sendPush(bindPushPayload(oldPhone, target));
            jedis.sadd(Constants.REDIS_UNDETERMINED_BINDING_KEY, oldPhone + "-" + youngPhone);
        } catch (APIConnectionException | APIRequestException e) {
            // 应该用Logger记录下来
            e.printStackTrace();
        }
    }

    @Override
    public void remind(String phone, RemindBody body) {
        try {
            String time = body.getTime().toString();
            TriggerKey key = new TriggerKey(time, phone);
            CronTrigger ct = (CronTrigger) scheduler.getTrigger(key);
            if (ct == null) {
                JobDetail job = JobBuilder.newJob(RemindJob.class)
                        .withIdentity(time, phone).build();
                job.getJobDataMap().put("scheduleJob", job);
                CronScheduleBuilder csb = CronScheduleBuilder.cronSchedule(transformDateToCronExpression(body.getTime()));
                ct = TriggerBuilder.newTrigger().withIdentity(time, phone).withSchedule(csb).build();
                scheduler.scheduleJob(job, ct);
            }
        } catch (SchedulerException e) {
            // Logger记录
            e.printStackTrace();
        }
    }

    @Override
    public void stopRemind(String phone, RemindBody body) {
        try {
            TriggerKey key = new TriggerKey(body.getTime().toString(), phone);
            scheduler.unscheduleJob(key);
        } catch (SchedulerException e) {
            // Logger记录
            e.printStackTrace();
        }
    }

    private static String transformDateToCronExpression(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("ss mm HH dd MM ? yyyy");
        return sdf.format(date);
    }

    private PushPayload bindPushPayload(String phone, String registrationId) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.registrationId(registrationId))
                .setMessage(Message.newBuilder()
                        .setContentType(Constants.MESSAGE_TYPE_BIND)
                        .setTitle("有一个绑定请求")
                        .setMsgContent("收到绑定请求，请及时处理")
                        .addExtra("phone", phone)
                        .build())
                .build();
    }

    public static class RemindJob implements Job {

        private final JPushClient client;
        private final String registrationId;
        private final String message;

        private RemindJob(JPushClient client, String registrationId, String message) {
            this.client = client;
            this.registrationId = registrationId;
            this.message = message;
        }

        @Override
        public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
            try {
                client.sendPush(remindPushPayload());
            } catch (APIConnectionException | APIRequestException e) {
                // Logger记录下来
                e.printStackTrace();
            }
        }

        private PushPayload remindPushPayload() {
            return PushPayload.newBuilder()
                    .setPlatform(Platform.all())
                    .setAudience(Audience.registrationId(registrationId))
                    .setMessage(Message.newBuilder()
                            .setContentType(Constants.MESSAGE_TYPE_BIND)
                            .setTitle("提醒")
                            .setMsgContent("收到提醒")
                            .addExtra("message", message)
                            .build())
                    .build();
        }
    }
}
