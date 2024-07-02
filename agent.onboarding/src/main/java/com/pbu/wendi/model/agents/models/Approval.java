package com.pbu.wendi.model.agents.models;

import com.pbu.wendi.utils.DomainEntity;
import com.pbu.wendi.utils.enums.Status;
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
public class Approval extends DomainEntity {

    @Column(length = 10, nullable=false)
    private String recruitedBy;

    @Column(nullable=false)
    private LocalDateTime recruitedOn;

    @Column(length = 40, nullable=false)
    private Status recruitStatus;

    @Column
    private String recruitComment;

    @Column(length = 10)
    private String createdBy;

    @Column
    private LocalDateTime createdOn;

    @Column(length = 40)
    private Status createdStatus;

    @Column
    private String createdComment;

    @Column(length = 10)
    private String reviewedBy;

    @Column
    private LocalDateTime reviewedOn;

    @Column(length = 40)
    private Status reviewStatus;

    @Column
    private String reviewComment;

    @Column(length = 10)
    private String approvedBy;

    @Column
    private LocalDateTime approvedOn;

    @Column(length = 40)
    private Status approveStatus;

    @Column
    private String approveComment;

    @ManyToOne
    private BusinessAgent  business;

    @ManyToOne
    private IndividualAgent  person;

    public Approval() {
        super();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Approval approval = (Approval) o;
        return getId() != 0 && Objects.equals(getId(), approval.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
