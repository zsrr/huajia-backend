package com.sssta.huajia.service;

public interface ValidationService {
    void registerValidation(String phone, String type);

    void loginValidation(String phone, String type);

    void beforeBindingValidation(Long oldId, String phone);
}
