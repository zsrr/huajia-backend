package com.sssta.huajia.service;

public interface JSMSService {
    String sendValidCode(String phone);
    boolean isCodeValid(String phone, String msgId, String code);
}
