package com.pbu.wendi.model.agents.models;

import com.pbu.wendi.utils.DomainEntity;
import jakarta.persistence.*;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;

import java.time.LocalDateTime;
import java.util.Objects;

@Data
@SuperBuilder
@Entity
@ToString(callSuper=true)
public class Partner extends DomainEntity {

    @Column(length = 220, nullable=false)
    private String fullName;

    @Column(nullable=false)
    private LocalDateTime birthDate;

    @Column(length = 25, nullable=false)
    private String contactNo;

    @ManyToOne
    private IndividualAgent agent;

    public Partner() {
        super();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Partner partner = (Partner) o;
        return getId() != 0 && Objects.equals(getId(), partner.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
