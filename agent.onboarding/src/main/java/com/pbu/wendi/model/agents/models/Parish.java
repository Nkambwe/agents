package com.pbu.wendi.model.agents.models;
import com.pbu.wendi.utils.DomainEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;

import java.util.Objects;

@Data
@SuperBuilder
@Entity
@ToString(callSuper=true)
public class Parish extends DomainEntity {
    @Column(length = 200, nullable=false)
    private String parishName;

    public Parish() {
        super();
    }

    @ManyToOne
    private County county;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Parish that = (Parish) o;
        return !getParishName().equals("") && Objects.equals(getParishName(), that.getParishName());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
