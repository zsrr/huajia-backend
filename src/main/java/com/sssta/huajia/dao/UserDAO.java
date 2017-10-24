package com.sssta.huajia.dao;

import com.sssta.huajia.domain.OldUser;
import com.sssta.huajia.domain.User;
import com.sssta.huajia.domain.YoungUser;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
public class UserDAO extends BaseDAO implements UserRepository {

    @Autowired
    public UserDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public boolean hasOldUser(String phone) {
        try {
            OldUser ou = getCurrentSession().get(OldUser.class, phone);
            return ou != null;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean hasYoungUser(String phone) {
        try {
            YoungUser ou = getCurrentSession().get(YoungUser.class, phone);
            return ou != null;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void oldRegister(String phone) {
        OldUser oldUser = new OldUser();
        oldUser.setPhone(phone);
        getCurrentSession().persist(oldUser);
    }

    @Override
    public void youngRegister(String phone) {
        YoungUser youngUser = new YoungUser();
        youngUser.setPhone(phone);
        getCurrentSession().persist(youngUser);
    }

    @Override
    public boolean isYoungBoundToOld(String oldPhone, String youngPhone) {
        Session session = getCurrentSession();
        OldUser ou = session.get(OldUser.class, oldPhone);
        return ou.getChild().getPhone().equals(youngPhone);
    }

    @Override
    public String getRegistrationIdByPhone(String phone) {
        User user = getCurrentSession().get(User.class, phone);
        return user.getRegistrationId();
    }

    @Override
    public <T extends User> T getUserByPhone(String phone, Class<T> type) {
        Session session = getCurrentSession();
        return session.get(type, phone);
    }

    @Override
    public <T extends User> T getUserRefByPhone(String phone, Class<T> type) {
        Session session = getCurrentSession();
        return session.getReference(type, phone);
    }

}
