package com.pbu.wendi.model.sam.models;

import com.pbu.wendi.utils.DomainEntity;
import jakarta.persistence.*;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;

import java.util.List;
import java.util.Objects;

/**
 * Class Name: App
 * Extends : DomainEntity class a base class for all database entities
 * Description: Class handles applications that user has access to
 * Created By: Nkambwe mark
 */

@Data
@SuperBuilder
@Entity
@ToString(callSuper=true)
public class App extends DomainEntity {
    @Column(length = 225, nullable = false)
    private String name;

    @Column(length = 225, nullable = false)
    private String description;

    @ManyToMany(mappedBy = "apps")
    @ToString.Exclude
    private List<User> users;

    public App() {
        super();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        App app = (App) o;
        return getId() != 0 && Objects.equals(getId(), app.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

