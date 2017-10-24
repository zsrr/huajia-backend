package com.sssta.huajia.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
public class YoungUser extends User {

    @OneToMany(mappedBy = "child")
    protected Set<OldUser> parents = new HashSet<>();

    public Set<OldUser> getParents() {
        return parents;
    }

    public void setParents(Set<OldUser> parents) {
        this.parents = parents;
    }
}
