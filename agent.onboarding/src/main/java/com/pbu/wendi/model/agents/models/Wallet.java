package com.pbu.wendi.model.agents.models;
import com.pbu.wendi.utils.DomainEntity;
import jakarta.persistence.*;
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
public class Wallet extends DomainEntity {
    @Column(length = 200, nullable=false)
    private String walletName;

    public Wallet() {
        super();
    }

    @OneToMany(mappedBy = "wallet", fetch = FetchType.EAGER)
    private List<BusinessAgent> businesses;

    @OneToMany(mappedBy = "wallet", fetch = FetchType.EAGER)
    private List<IndividualAgent> persons;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Wallet that = (Wallet) o;
        return !getWalletName().equals("") && Objects.equals(getWalletName(), that.getWalletName());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
