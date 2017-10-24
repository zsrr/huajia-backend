package com.sssta.huajia.controller;

import com.sssta.huajia.dto.SMSVerifyMessage;
import com.sssta.huajia.dto.VerificationSMSResponse;
import com.sssta.huajia.service.JSMSService;
import com.sssta.huajia.service.UserService;
import com.sssta.huajia.service.ValidationService;
import com.stephen.a2.exception.HttpParamResolveException;
import com.stephen.a2.exception.UnAuthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

public abstract class BaseController {

    final UserService userService;
    final ValidationService validationService;
    final JSMSService jsmsService;

    public BaseController(UserService userService, ValidationService validationService, JSMSService jsmsService) {
        this.userService = userService;
        this.validationService = validationService;
        this.jsmsService = jsmsService;
    }

    private ResponseEntity baseFlow(String action, String phone, SMSVerifyMessage verifyMessage, Object obj) throws MissingServletRequestPartException {
        if (action.equals("send")) {
            String msgId = jsmsService.sendValidCode(phone);
            return new ResponseEntity(new VerificationSMSResponse(msgId), HttpStatus.OK);
        } else if (action.equals("verify")) {
            checkVerifyMessage(verifyMessage);
            if (jsmsService.isCodeValid(phone, verifyMessage.getMsgId(), verifyMessage.getCode())) {
                return new ResponseEntity(obj, HttpStatus.CREATED);
            } else {
                throw new UnAuthorizedException();
            }
        } else {
            throw new HttpParamResolveException("action", action);
        }
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity register(@RequestParam("action") String action,
                                   @RequestParam("phone") String phone,
                                   @RequestBody(required = false) SMSVerifyMessage verifyMessage) throws MissingServletRequestPartException {
        validationService.registerValidation(phone, getType());
        return baseFlow(action, phone, verifyMessage, userService.register(phone, getType()));
    }

    @RequestMapping("/login")
    public ResponseEntity login(@RequestParam("phone") String phone,
                                @RequestParam("action") String action,
                                @RequestBody(required = false) SMSVerifyMessage verifyMessage) throws MissingServletRequestPartException {
        validationService.loginValidation(phone, "old");
        return baseFlow(action, phone, verifyMessage, userService.login(phone, getType()));
    }

    private void checkVerifyMessage(SMSVerifyMessage verifyMessage) throws MissingServletRequestPartException {
        if (verifyMessage == null || verifyMessage.getMsgId() == null || verifyMessage.getCode() == null) {
            throw new MissingServletRequestPartException("verifymessage");
        }
    }

    protected abstract String getType();

}
