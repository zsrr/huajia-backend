package com.sssta.huajia.service;

import com.sssta.huajia.Constants;
import com.sssta.huajia.dao.UserRepository;
import com.sssta.huajia.dto.RemindBody;
import com.stephen.a2.exception.NotFoundException;
import com.stephen.a2.exception.ResourceConflictException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
@Transactional
public class ValidationServiceImpl extends JedisService implements ValidationService {

    private final UserRepository userDAO;

    @Autowired
    public ValidationServiceImpl(UserRepository userDAO, JedisPool pool) {
        super(pool);
        this.userDAO = userDAO;
    }

    @Override
    public void registerValidation(String phone, String type) {
        if ((type.equals(Constants.TYPE_OLD) && userDAO.hasOldUser(phone)) ||
                (type.equals(Constants.TYPE_YOUNG) && userDAO.hasYoungUser(phone))) {
            throw new ResourceConflictException();
        }
    }

    @Override
    public void loginValidation(String phone, String type) {
        if ((type.equals(Constants.TYPE_OLD) && !userDAO.hasOldUser(phone)) ||
                (type.equals(Constants.TYPE_YOUNG) && !userDAO.hasYoungUser(phone))) {
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
        try (Jedis jedis = getJedis()) {
            if (!jedis.sismember(Constants.REDIS_UNDETERMINED_BINDING_KEY, oldPhone + "-" + youngPhone)) {
                throw new NotFoundException();
            }
        }
    }

    @Override
    public void remindValidation(String phone, RemindBody remindBody) {
        if (!userDAO.isYoungBoundToOld(remindBody.getTarget(), phone)) {
            throw new NotFoundException();
        }
    }
}
