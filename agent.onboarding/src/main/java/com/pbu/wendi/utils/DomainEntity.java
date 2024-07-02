package com.pbu.wendi.utils;

import jakarta.persistence.*;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;

import java.util.Objects;

@Data
@SuperBuilder
@MappedSuperclass
public abstract class DomainEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "row_id", nullable = false)
    private long id;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;
    public DomainEntity() {}

    @Override
    public String toString() {
        return id != 0 ? String.format("Entity ID: %s", id): super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        DomainEntity that = (DomainEntity) o;
        return getId() != 0 && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
