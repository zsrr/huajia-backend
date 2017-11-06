package com.sssta.huajia.controller;

import com.sssta.huajia.Constants;
import com.sssta.huajia.dto.RemindBody;
import com.sssta.huajia.service.PushService;
import com.sssta.huajia.service.JSMSService;
import com.sssta.huajia.service.UserService;
import com.sssta.huajia.service.ValidationService;
import com.stephen.a2.authorization.Authorization;
import com.stephen.a2.authorization.CurrentUserId;
import com.stephen.a2.response.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class YoungController extends BaseController {

    private final PushService pushService;

    @Autowired
    public YoungController(UserService userService, ValidationService validationService, JSMSService jsmsService, PushService pushService) {
        super(userService, validationService, jsmsService);
        this.pushService = pushService;
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

    // 这里要开启一个定时任务
    @Authorization
    @RequestMapping(value = "/remind")
    public ResponseEntity remind(@RequestBody RemindBody body, @CurrentUserId String current) {
        validationService.remindValidation(current, body);
        pushService.remind(current, body);
        return new ResponseEntity(new BaseResponse(), HttpStatus.OK);
    }

    @Authorization
    @RequestMapping(value = "/stop-remind")
    public ResponseEntity stopRemind(@RequestBody RemindBody body, @CurrentUserId String current) {
        pushService.stopRemind(current, body);
        return new ResponseEntity(new BaseResponse(), HttpStatus.OK);
    }
}
