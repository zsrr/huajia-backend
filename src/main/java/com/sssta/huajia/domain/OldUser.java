package com.sssta.huajia.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "OLD_USER", indexes = @Index(name = "old_phone_index", columnList = "phone", unique = true))
public class OldUser {

    @Id
    @GeneratedValue(generator = Constants.PERFECT_SEQUENCE)
    protected Long id;

    @Column(unique = true, nullable = false, length = 14, name = "phone")
    @NotNull
    protected String phone;

    @ManyToOne
    protected YoungUser child;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getId() {
        return id;
    }

    public YoungUser getChild() {
        return child;
    }

    public void setChild(YoungUser child) {
        this.child = child;
    }
}
