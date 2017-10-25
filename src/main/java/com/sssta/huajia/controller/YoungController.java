package com.sssta.huajia.controller;

import com.sssta.huajia.Constants;
import com.sssta.huajia.service.JPushService;
import com.sssta.huajia.service.JSMSService;
import com.sssta.huajia.service.UserService;
import com.sssta.huajia.service.ValidationService;
import com.stephen.a2.authorization.Authorization;
import com.stephen.a2.authorization.CurrentUserId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class YoungController extends BaseController {

    @Autowired
    public YoungController(UserService userService, ValidationService validationService, JSMSService jsmsService, JPushService jPushService) {
        super(userService, validationService, jsmsService);
    }

    @Override
    protected String getType() {
        return Constants.TYPE_YOUNG;
    }

    @Authorization
    @RequestMapping(value = "/bind", method = RequestMethod.POST)
    public ResponseEntity bind(@RequestParam("target") String oldPhone, @CurrentUserId String youngPhone) {
        validationService.finalBindingValidation(oldPhone, youngPhone);
        return new ResponseEntity(userService.bind(oldPhone, youngPhone), HttpStatus.OK);
    }
}
