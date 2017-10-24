package com.sssta.huajia.dto;

import com.stephen.a2.response.BaseResponse;

public class VerificationSMSResponse extends BaseResponse {
    String msgId;

    public VerificationSMSResponse(String msgId) {
        this.msgId = msgId;
    }

    public VerificationSMSResponse() {
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }
}
