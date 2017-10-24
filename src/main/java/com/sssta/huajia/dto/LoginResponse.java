package com.sssta.huajia.dto;

import com.stephen.a2.response.BaseResponse;

public class LoginResponse extends BaseResponse {

    Long id;
    String token;

    public LoginResponse(Long id, String token) {
        this.id = id;
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
