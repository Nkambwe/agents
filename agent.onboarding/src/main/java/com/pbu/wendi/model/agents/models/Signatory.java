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
public class Signatory extends DomainEntity {

    @Column(length = 120, nullable=false)
    private String fullName;

    @Column(length = 220, nullable=false)
    private String signatureUrl;

    @Column(nullable=false)
    private LocalDateTime signedOn;

    @ManyToOne
    private BusinessAgent business;

    @ManyToOne
    private IndividualAgent person;
    public Signatory() {
        super();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Signatory signatory = (Signatory) o;
        return getId() != 0 && Objects.equals(getId(), signatory.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
