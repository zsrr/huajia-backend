package com.sssta.huajia.dao;

import com.sssta.huajia.domain.OldUser;
import com.sssta.huajia.domain.YoungUser;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;

@Repository
public class UserDAO extends BaseDAO implements UserRepository {

    @Autowired
    public UserDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public boolean hasOldUser(String phone) {
        try {
            TypedQuery<Long> query = getCurrentSession().createQuery("select id from OldUser o where o.phone = :phone").setParameter("phone", phone);
            return query.getSingleResult() != null;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean hasYoungUser(String phone) {
        try {
            TypedQuery<Long> query = getCurrentSession().createQuery("select id from YoungUser o where o.phone = :phone").setParameter("phone", phone);
            return query.getSingleResult() != null;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Long oldRegister(String phone) {
        OldUser oldUser = new OldUser();
        oldUser.setPhone(phone);
        getCurrentSession().persist(oldUser);
        return oldUser.getId();
    }

    @Override
    public Long youngRegister(String phone) {
        YoungUser youngUser = new YoungUser();
        youngUser.setPhone(phone);
        getCurrentSession().persist(youngUser);
        return youngUser.getId();
    }

    @Override
    public Long getOldUserId(String phone) {
        Session session = getCurrentSession();
        TypedQuery<Long> query = session.createQuery("select id from OldUser o where o.phone = :phone").setParameter("phone", phone);
        return query.getSingleResult();
    }

    @Override
    public Long getYoungUserId(String phone) {
        Session session = getCurrentSession();
        TypedQuery<Long> query = session.createQuery("select id from YoungUser o where o.phone = :phone").setParameter("phone", phone);
        return query.getSingleResult();
    }

    @Override
    public boolean isYoungBoundToOld(Long oldId, String phone) {
        Session session = getCurrentSession();
        TypedQuery<Long> query = session.createQuery("select count(o) from OldUser o where o.child.phone = :phone").setParameter("phone", phone);
        return query.getSingleResult() > 0L;
    }
}
