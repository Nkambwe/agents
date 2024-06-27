package com.pbu.wendi.model.agents.models;

import com.pbu.wendi.utils.DomainEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;

import java.util.Objects;

@Data
@SuperBuilder
@Entity
@ToString(callSuper=true)
public class AgentSettings extends DomainEntity {

    @Column(length = 180, nullable=false)
    private String paramName;

    @Column(length = 180, nullable=false)
    private String paramValue;

    @Column(length = 225)
    private String description;

    public AgentSettings() {
        super();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AgentSettings that = (AgentSettings) o;
        return getId() != 0 && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
