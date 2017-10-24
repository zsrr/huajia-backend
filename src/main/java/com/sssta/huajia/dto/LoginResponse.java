package com.sssta.huajia.dto;

import com.stephen.a2.response.BaseResponse;

public class LoginResponse extends BaseResponse {

    String token;

    public LoginResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
