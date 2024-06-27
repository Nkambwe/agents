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
public class County extends DomainEntity {
    @Column(length = 200, nullable=false)
    private String countyName;

    public County() {
        super();
    }

    @ManyToOne
    private District district;

    @OneToMany(mappedBy = "county", fetch = FetchType.EAGER)
    private List<Parish> parishes;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        County that = (County) o;
        return !getCountyName().equals("") && Objects.equals(getCountyName(), that.getCountyName());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
