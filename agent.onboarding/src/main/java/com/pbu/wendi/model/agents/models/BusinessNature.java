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
public class BusinessNature extends DomainEntity {
    @Column(length = 200, nullable=false)
    private String nature;

    public BusinessNature() {
        super();
    }

    @OneToMany(mappedBy = "biz_nature", fetch = FetchType.EAGER)
    private List<BusinessAgent> agent;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        BusinessNature that = (BusinessNature) o;
        return !getNature().equals("") && Objects.equals(getNature(), that.getNature());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
