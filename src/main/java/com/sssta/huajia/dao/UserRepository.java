package com.sssta.huajia.dao;

public interface UserRepository {

    boolean hasOldUser(String phone);

    boolean hasYoungUser(String phone);

    Long oldRegister(String phone);

    Long youngRegister(String phone);

    Long getOldUserId(String phone);

    Long getYoungUserId(String phone);

    boolean isYoungBoundToOld(Long oldId, String phone);

}
