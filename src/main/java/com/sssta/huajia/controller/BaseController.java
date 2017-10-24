package com.sssta.huajia.controller;

import com.sssta.huajia.dto.SMSVerifyMessage;
import com.sssta.huajia.dto.VerificationResponse;
import com.sssta.huajia.service.JSMSService;
import com.sssta.huajia.service.UserService;
import com.sssta.huajia.service.ValidationService;
import com.stephen.a2.exception.HttpParamResolveException;
import com.stephen.a2.exception.UnAuthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

public abstract class BaseController {

    final UserService userService;
    final ValidationService validationService;
    final JSMSService jsmsService;

    private interface Operation {
        Object operate() throws Exception;
    }

    public BaseController(UserService userService, ValidationService validationService, JSMSService jsmsService) {
        this.userService = userService;
        this.validationService = validationService;
        this.jsmsService = jsmsService;
    }

    private ResponseEntity baseFlow(String action, String phone, SMSVerifyMessage verifyMessage, Operation operation) throws Exception {
        if (action.equals("send")) {
            String msgId = jsmsService.sendValidCode(phone);
            return new ResponseEntity(new VerificationResponse(msgId), HttpStatus.OK);
        } else if (action.equals("verify")) {
            checkVerifyMessage(verifyMessage);
            if (jsmsService.isCodeValid(phone, verifyMessage.getMsgId(), verifyMessage.getCode())) {
                return new ResponseEntity(operation.operate(), HttpStatus.OK);
            } else {
                throw new UnAuthorizedException();
            }
        } else {
            throw new HttpParamResolveException("action", action);
        }
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity register(@RequestParam("action") String action,
                                   @RequestParam("phone") final String phone,
                                   @RequestBody(required = false) SMSVerifyMessage verifyMessage) throws Exception {
        validationService.registerValidation(phone, getType());
        return baseFlow(action, phone, verifyMessage, () -> userService.register(phone, getType()));
    }

    @RequestMapping("/login")
    public ResponseEntity login(@RequestParam("phone") final String phone,
                                @RequestParam("action") String action,
                                @RequestParam(value = "registrationId", required = false) final String registrationId,
                                @RequestBody(required = false) SMSVerifyMessage verifyMessage) throws Exception {
        validationService.loginValidation(phone, getType());
        return baseFlow(action, phone, verifyMessage, () -> {
            if (registrationId == null) {
                throw new MissingServletRequestParameterException("registrationId", "string");
            }
            return userService.login(phone, registrationId);
        });
    }

    private void checkVerifyMessage(SMSVerifyMessage verifyMessage) throws MissingServletRequestPartException {
        if (verifyMessage == null || verifyMessage.getMsgId() == null || verifyMessage.getCode() == null) {
            throw new MissingServletRequestPartException("verifymessage");
        }
    }

    protected abstract String getType();

}
