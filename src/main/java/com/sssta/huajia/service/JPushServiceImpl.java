package com.sssta.huajia.service;

import cn.jpush.api.push.model.PushPayload;
import com.sssta.huajia.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class JPushServiceImpl implements JPushService {

    private UserRepository userDAO;

    @Autowired
    public JPushServiceImpl(UserRepository userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public void bind(Long id, String phone) {

    }

    PushPayload bindPayLoad() {
        return null;
    }
}
