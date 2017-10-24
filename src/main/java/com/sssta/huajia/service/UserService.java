package com.sssta.huajia.service;

import com.sssta.huajia.dto.LoginResponse;
import com.stephen.a2.response.BaseResponse;

public interface UserService {

    BaseResponse register(String phone, String type);

    LoginResponse login(String phone, String registrationId);

    BaseResponse bind(String oldPhone, String youngPhone);

}
