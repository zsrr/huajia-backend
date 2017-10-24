package com.sssta.huajia.dto;

import com.stephen.a2.response.BaseResponse;

public class VerificationResponse extends BaseResponse {
    String msgId;

    public VerificationResponse(String msgId) {
        this.msgId = msgId;
    }

    public VerificationResponse() {
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }
}
