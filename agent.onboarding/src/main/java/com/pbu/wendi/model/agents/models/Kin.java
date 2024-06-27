package com.pbu.wendi.model.agents.models;

import com.pbu.wendi.utils.DomainEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;

import java.util.Objects;

@Data
@SuperBuilder
@Entity
@ToString(callSuper=true)
public class Kin extends DomainEntity {

    @Column(length = 220, nullable=false)
    private String fullName;

    @Column(length = 150, nullable=false)
    private String address;

    @Column(length = 220)
    private String email;

    @Column(length = 80)
    private String relationship;

    @Column(length = 25)
    private String phoneNo;

    @ManyToOne
    private IndividualAgent agent;

    public Kin() {
        super();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Kin kin = (Kin) o;
        return getId() != 0 && Objects.equals(getId(), kin.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
