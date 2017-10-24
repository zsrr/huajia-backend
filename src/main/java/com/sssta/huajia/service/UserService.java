package com.sssta.huajia.service;

import com.sssta.huajia.dto.LoginResponse;
import com.sssta.huajia.dto.RegisterResponse;

public interface UserService {

    RegisterResponse register(String phone, String type);

    LoginResponse login(String phone, String type);

}
