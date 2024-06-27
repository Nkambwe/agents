package com.pbu.wendi.model.agents.models;

import com.pbu.wendi.utils.DomainEntity;
import com.pbu.wendi.utils.enums.Gender;
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
public class Operator extends DomainEntity {

    @Column(length = 180,nullable=false)
    private String operatorName;

    @Column(nullable=false)
    private Gender gender;

    @Column(length = 25,nullable=false)
    private String phoneNo;

    @Column(length = 50, nullable=false)
    private String idNin;

    @Column(length = 220, nullable=false)
    private String channels;

    @Column(length = 120, nullable=false)
    private String bankName;

    @Column(length = 50, nullable=false)
    private String accountNo;

    @Column(length = 220, nullable=false)
    private String accountName;

    @Column(nullable=false)
    private boolean hasCrime;

    //Reference number to criminal record
    @Column(length = 60)
    private String crimeRef;

    @Column(nullable=false)
    private boolean hasActiveCrime;

    //Reference number to current case record
    @Column(length = 60)
    private String activeCrimeRef;

    @ManyToOne
    private IndividualAgent person;

    @ManyToOne
    private BusinessAgent business;

    public Operator() {
        super();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Operator operator = (Operator) o;
        return getId() != 0 && Objects.equals(getId(), operator.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}


