package com.sssta.huajia.dao;


import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class BaseDAO {
    protected SessionFactory sessionFactory;

    public BaseDAO(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }
}
