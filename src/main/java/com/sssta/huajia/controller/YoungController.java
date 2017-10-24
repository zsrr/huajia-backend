package com.sssta.huajia.controller;

import com.sssta.huajia.service.JSMSService;
import com.sssta.huajia.service.UserService;
import com.sssta.huajia.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class YoungController extends BaseController {

    @Autowired
    public YoungController(UserService userService, ValidationService validationService, JSMSService jsmsService) {
        super(userService, validationService, jsmsService);
    }

    @Override
    protected String getType() {
        return "young";
    }
}
