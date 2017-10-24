package com.sssta.huajia.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "YOUNG_USER", indexes = @Index(name = "young_phone_index", columnList = "phone", unique = true))
public class YoungUser {

    @Id
    @GeneratedValue(generator = Constants.PERFECT_SEQUENCE)
    protected Long id;

    @Column(nullable = false, unique = true, length = 14)
    @NotNull
    protected String phone;

    @OneToMany(mappedBy = "child")
    protected Set<OldUser> parents = new HashSet<>();

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getId() {
        return id;
    }

    public Set<OldUser> getParents() {
        return parents;
    }

    public void setParents(Set<OldUser> parents) {
        this.parents = parents;
    }
}
