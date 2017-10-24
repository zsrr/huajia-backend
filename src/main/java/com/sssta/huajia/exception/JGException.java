package com.sssta.huajia.exception;


import cn.jiguang.common.resp.APIConnectionException;
import com.stephen.a2.exception.BaseRuntimeException;
import com.stephen.a2.response.BaseResponse;
import com.stephen.a2.response.ErrorDetail;
import org.springframework.http.HttpStatus;

public class JGException extends BaseRuntimeException {

    private static final long serialVersionUID = 1846396102779389953L;

    public JGException(Throwable cause) {
        super(cause);
    }

    @Override
    public BaseResponse getBaseResponse() {
        ErrorDetail ed = new ErrorDetail("Jiguang exception happened", this.getClass(), getCause().getMessage());
        return new BaseResponse(getHttpStatus(), ed);
    }

    @Override
    public HttpStatus getHttpStatus() {
        if (getCause() instanceof APIConnectionException) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return HttpStatus.BAD_REQUEST;
    }
}
