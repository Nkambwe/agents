package com.pbu.wendi.model.agents.models;

import com.pbu.wendi.utils.DomainEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;

import java.util.List;
import java.util.Objects;

@Data
@SuperBuilder
@Entity
@ToString(callSuper=true)
public class Bank  extends DomainEntity {
    @Column(length = 10, nullable=false)
    private String sortCode;

    @Column(length = 200, nullable=false)
    private String bankName;

    public Bank() {
        super();
    }

    @OneToMany(mappedBy = "bank", fetch = FetchType.EAGER)
    private List<BusinessAgent> businesses;

    @OneToMany(mappedBy = "bank", fetch = FetchType.EAGER)
    private List<IndividualAgent> persons;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Bank that = (Bank) o;
        return !getBankName().equals("") && Objects.equals(getBankName(), that.getBankName());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
