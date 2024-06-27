package com.pbu.wendi.model.agents.models;

import com.pbu.wendi.utils.enums.Gender;
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
public class IndividualAgent extends Agent {
    @Column(length = 100, nullable=false)
    private String firstName;

    @Column(length = 100)
    private String middleName;

    @Column(length = 100, nullable=false)
    private String surname;

    @Column(nullable=false)
    private LocalDateTime birthDate;

    @Column(nullable=false)
    private Gender gender;

    @Column(nullable=false)
    private long identificationId;

    @Column(length = 30)
    private String personalTin;

    @Column(length = 25, nullable=false)
    private String primaryContact;

    @Column(length = 25)
    private String secondaryContact;

    @Column(nullable=false)
    private String email;

    @OneToMany(mappedBy = "person", fetch = FetchType.EAGER)
    private List<Approval> approvals;

    @Column(length = 220, nullable=false)
    private String district;

    @Column(length = 220, nullable=false)
    private String county;

    @Column(length = 220, nullable=false)
    private String villageOrParish;

    @ManyToOne
    private Affiliation affiliation;

    @ManyToOne
    private Bank bank;

    @ManyToOne
    private Wallet wallet;

    @OneToMany(mappedBy = "person", fetch = FetchType.EAGER)
    private List<Operator> operators;

    @OneToMany(mappedBy = "agent", fetch = FetchType.EAGER)
    private List<Partner> partners;

    @OneToMany(mappedBy = "agent", fetch = FetchType.EAGER)
    private List<Kin> nextOfKin;

    @OneToMany(mappedBy = "person", fetch = FetchType.EAGER)
    private List<Signatory> signatories;
    public IndividualAgent() {
        super();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        IndividualAgent that = (IndividualAgent) o;
        return getId() != 0 && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

