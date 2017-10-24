package com.sssta.huajia.service;

import com.sssta.huajia.dao.UserRepository;
import com.stephen.a2.exception.NotFoundException;
import com.stephen.a2.exception.ResourceConflictException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ValidationServiceImpl implements ValidationService {

    private final UserRepository userDAO;

    @Autowired
    public ValidationServiceImpl(UserRepository userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public void registerValidation(String phone, String type) {
        if ((type.equals("old") && userDAO.hasOldUser(phone)) ||
                (type.equals("young") && userDAO.hasYoungUser(phone))) {
            throw new ResourceConflictException();
        }
    }

    @Override
    public void loginValidation(String phone, String type) {
        if ((type.equals("old") && !userDAO.hasOldUser(phone)) ||
                (type.equals("young") && !userDAO.hasYoungUser(phone))) {
            throw new NotFoundException();
        }
    }

    @Override
    public void beforeBindingValidation(Long oldId, String phone) {
        if (userDAO.isYoungBoundToOld(oldId, phone)) {
            throw new ResourceConflictException();
        }
    }
}
