package com.sssta.huajia.service;

import com.sssta.huajia.dao.UserRepository;
import com.stephen.a2.exception.NotFoundException;
import com.stephen.a2.exception.ResourceConflictException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
@Transactional
public class ValidationServiceImpl implements ValidationService {

    private final UserRepository userDAO;
    private final JedisPool pool;

    @Autowired
    public ValidationServiceImpl(UserRepository userDAO, JedisPool pool) {
        this.userDAO = userDAO;
        this.pool = pool;
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
    public void beforeBindingValidation(String oldPhone, String youngPhone) {
        if (userDAO.isYoungBoundToOld(oldPhone, youngPhone)) {
            throw new ResourceConflictException();
        }
    }

    @Override
    public void finalBindingValidation(String oldPhone, String youngPhone) {
        try (Jedis jedis = pool.getResource()) {
            if (!jedis.sismember(JPushServiceImpl.REDIS_UNDETERMINED_BINDING_KEY, oldPhone + "-" + youngPhone)) {
                throw new NotFoundException();
            }
        }
    }
}
