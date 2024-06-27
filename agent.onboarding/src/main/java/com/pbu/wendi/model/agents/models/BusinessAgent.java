package com.pbu.wendi.model.agents.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Data
@SuperBuilder
@Entity
@ToString(callSuper=true)
public class BusinessAgent extends Agent {
    @Column(length = 20)
    private String regNo;

    @Column(length = 220, nullable=false)
    private String registeredName;

    @Column(nullable=false)
    private boolean sameName;

    @Column(length = 220, nullable=false)
    private String businessName;

    @Column(length = 220)
    private String businessNature;

    @Column(nullable=false)
    private LocalDateTime incorporatedOn;

    @Column(length = 50, nullable=false)
    private String businessTin;

    @Column(length = 30, nullable=false)
    private String businessTel;

    @Column(length = 120, nullable=false)
    private String postalOffice;

    @Column(nullable=false)
    private int yearsInBusiness;

    @Column(nullable=false)
    private int outlets;

    @Column(length = 220, nullable=false)
    private String district;

    @Column(length = 220, nullable=false)
    private String county;

    @Column(length = 220, nullable=false)
    private String villageOrParish;

    @Column(length = 220)
    private String physicalAddress;

    @Column(length = 60, nullable=false)
    private String longitude;

    @Column(length = 60, nullable=false)
    private String latitude;

    @ManyToOne
    private Affiliation affiliation;

    @ManyToOne
    private Bank bank;

    @ManyToOne
    private Wallet wallet;

    @ManyToOne
    private BusinessNature biz_nature;

    @OneToMany(mappedBy = "business", fetch = FetchType.EAGER)
    private List<Approval> approvals;

    @OneToMany(mappedBy = "business", fetch = FetchType.EAGER)
    private List<Operator> operators;

    @OneToMany(mappedBy = "business", fetch = FetchType.EAGER)
    private List<Signatory> signatories;
    public BusinessAgent() {
        super();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        BusinessAgent that = (BusinessAgent) o;
        return getId() != 0 && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

