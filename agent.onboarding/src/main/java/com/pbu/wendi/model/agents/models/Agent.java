package com.pbu.wendi.model.agents.models;

import com.pbu.wendi.utils.DomainEntity;
import com.pbu.wendi.utils.enums.AgentCategory;
import com.pbu.wendi.utils.enums.AgentType;
import com.pbu.wendi.utils.enums.RetailType;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;

import java.util.Objects;

@Data
@SuperBuilder
@ToString(callSuper=true)
@MappedSuperclass
public abstract class Agent extends DomainEntity {

    @Column(nullable=false)
    private String sortCode; //6 characters in length incremented by 1

    @Column(nullable=false)
    private String pinNumber; //4 numeric character. random generated

    @Column(nullable=false)
    private AgentType agentType;

    @Column(nullable=false)
    private AgentCategory category;

    @Column(nullable=false)
    private RetailType retailCategory;

    @Column(length = 150)
    private String postalAddress;

    public Agent() {
        super();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Agent agent = (Agent) o;
        return getId() != 0 && Objects.equals(getId(), agent.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
