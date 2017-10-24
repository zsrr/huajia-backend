package com.sssta.huajia.dto;

import com.stephen.a2.response.BaseResponse;
import org.springframework.http.HttpStatus;

public class RegisterResponse extends BaseResponse {

    Long id;

    public RegisterResponse(Long id) {
        super();
        this.id = id;
        setStatus(HttpStatus.CREATED.value());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
