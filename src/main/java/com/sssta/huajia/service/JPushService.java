package com.sssta.huajia.service;

public interface JPushService {

    // 只能是老人向年轻人发送推送
    void bind(Long id, String phone);

}
