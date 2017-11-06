package com.sssta.huajia.service;

import com.sssta.huajia.dto.RemindBody;

public interface ValidationService {
    void registerValidation(String phone, String type);

    void loginValidation(String phone, String type);

    void beforeBindingValidation(String oldPhone, String youngPhone);

    void finalBindingValidation(String oldPhone, String youngPhone);

    void remindValidation(String phone, RemindBody remindBody);
}
