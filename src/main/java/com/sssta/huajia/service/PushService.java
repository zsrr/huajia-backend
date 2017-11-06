package com.sssta.huajia.service;

import com.sssta.huajia.dto.RemindBody;

public interface PushService {

    // 只能是老人向年轻人发送推送
    void bind(String oldPhone, String youngPhone);

    void remind(String phone, RemindBody body);

    void stopRemind(String phone, RemindBody body);

}
