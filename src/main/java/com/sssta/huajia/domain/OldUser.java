package com.sssta.huajia.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class OldUser extends User {

    @ManyToOne
    protected YoungUser child;

    public YoungUser getChild() {
        return child;
    }

    public void setChild(YoungUser child) {
        this.child = child;
    }
}
