package com.sssta.huajia.domain;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class User {

    public User(String phone) {
        this.phone = phone;
    }

    protected User() {

    }

    @Id
    @Column(length = 14, name = "phone")
    protected String phone;

    protected String registrationId;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }
}
