package com.sssta.huajia.controller;


import com.sssta.huajia.service.JPushService;
import com.sssta.huajia.service.JSMSService;
import com.sssta.huajia.service.UserService;
import com.sssta.huajia.service.ValidationService;
import com.stephen.a2.authorization.Authorization;
import com.stephen.a2.authorization.CurrentUserId;
import com.stephen.a2.response.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/old")
public class OldController extends BaseController {

    private final JPushService jPushService;

    @Autowired
    public OldController(UserService userService, ValidationService validationService, JSMSService jsmsService, JPushService jPushService) {
        super(userService, validationService, jsmsService);
        this.jPushService = jPushService;
    }

    @Override
    protected String getType() {
        return "old";
    }

    @Authorization
    @RequestMapping(value = "/bind", method = RequestMethod.POST)
    public ResponseEntity bind(@CurrentUserId String oldPhone, @RequestParam("target") String youngPhone) {
        validationService.beforeBindingValidation(oldPhone, youngPhone);
        jPushService.bind(oldPhone, youngPhone);
        return new ResponseEntity(new BaseResponse(), HttpStatus.OK);
    }
}
