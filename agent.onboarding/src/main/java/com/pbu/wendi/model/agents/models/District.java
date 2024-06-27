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
public class District extends DomainEntity {
    @Column(length = 200, nullable=false)
    private String districtName;

    public District() {
        super();
    }

    @OneToMany(mappedBy = "district", fetch = FetchType.EAGER)
    private List<BusinessAgent> businesses;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        District that = (District) o;
        return !getDistrictName().equals("") && Objects.equals(getDistrictName(), that.getDistrictName());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
