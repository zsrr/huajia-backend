package com.sssta.huajia.service;

import com.sssta.huajia.dao.UserRepository;
import com.sssta.huajia.domain.OldUser;
import com.sssta.huajia.domain.User;
import com.sssta.huajia.domain.YoungUser;
import com.sssta.huajia.dto.LoginResponse;
import com.stephen.a2.authorization.TokenManager;
import com.stephen.a2.authorization.TokenModel;
import com.stephen.a2.response.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public BaseResponse register(String phone, String type) {

        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatus(HttpStatus.CREATED.value());

        if (type.equals("old")) {
            userDAO.oldRegister(phone);
            return baseResponse;
        } else if (type.equals("young")) {
            userDAO.youngRegister(phone);
            return baseResponse;
        }

        return null;
    }

    @Override
    @Transactional
    public LoginResponse login(String phone, String registrationId) {
        User user = userDAO.getUserByPhone(phone, User.class);
        user.setRegistrationId(registrationId);
        TokenModel model = tokenManager.createToken(user.getPhone());
        return new LoginResponse(model.getToken());
    }

    @Override
    @Transactional
    public BaseResponse bind(String oldPhone, String youngPhone) {
        OldUser ou = userDAO.getUserByPhone(oldPhone, OldUser.class);
        ou.setChild(userDAO.getUserRefByPhone(youngPhone, YoungUser.class));
        return new BaseResponse();
    }
}
