package com.sssta.huajia.dto;

import com.stephen.a2.response.BaseResponse;

public class VerifyResponse extends BaseResponse {
    String msgId;

    public VerifyResponse(String msgId) {
        this.msgId = msgId;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }
}
