package com.sssta.huajia.service;

import com.sssta.huajia.dao.UserRepository;
import com.sssta.huajia.dto.LoginResponse;
import com.sssta.huajia.dto.RegisterResponse;
import com.stephen.a2.authorization.TokenManager;
import com.stephen.a2.authorization.TokenModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

@Service
public class UserServiceImpl extends JedisService implements UserService {

    private final UserRepository userDAO;
    private final TokenManager tokenManager;

    @Autowired
    public UserServiceImpl(UserRepository userDAO, JedisPool jedisPool, TokenManager tokenManager) {
        super(jedisPool);
        this.userDAO = userDAO;
        this.tokenManager = tokenManager;
    }

    @Override
    @Transactional
    public RegisterResponse register(String phone, String type) {
        if (type.equals("old")) {
            return new RegisterResponse(userDAO.oldRegister(phone));
        } else if (type.equals("young")) {
            return new RegisterResponse(userDAO.youngRegister(phone));
        }

        return null;
    }

    @Override
    @Transactional
    public LoginResponse login(String phone, String type) {
        Long userId = 0L;
        if (type.equals("old")) {
            userId = userDAO.getOldUserId(phone);
        } else if (type.equals("young")) {
            userId = userDAO.getYoungUserId(phone);
        }
        TokenModel model = tokenManager.createToken(userId);
        return new LoginResponse(userId, model.getToken());
    }
}
