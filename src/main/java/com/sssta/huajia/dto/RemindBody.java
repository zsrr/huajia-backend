package com.sssta.huajia.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class RemindBody {

    @JsonProperty(required = true)
    private String target;

    @JsonProperty(required = true)
    private Date time;

    @JsonProperty(required = true)
    private String message;

    public String getTarget() {
        return target;
    }

    public Date getTime() {
        return time;
    }

    public String getMessage() {
        return message;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
