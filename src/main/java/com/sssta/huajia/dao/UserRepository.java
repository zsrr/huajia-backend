package com.sssta.huajia.dao;

import com.sssta.huajia.domain.User;

public interface UserRepository {

    boolean hasOldUser(String phone);

    boolean hasYoungUser(String phone);

    void oldRegister(String phone);

    void youngRegister(String phone);

    boolean isYoungBoundToOld(String oldPhone, String youngPhone);

    String getRegistrationIdByPhone(String phone);

    User getUserByPhone(String phone);

}
