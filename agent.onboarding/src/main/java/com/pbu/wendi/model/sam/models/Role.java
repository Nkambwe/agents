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
 * Class Name: Role
 * Extends : DomainEntity class a base class for all database entities
 * Description: Class handles System role details and is mapped to the table system_roles
 * Created By: Nkambwe mark
 */

@Data
@SuperBuilder
@Entity
@ToString(callSuper=true)
public class Role extends DomainEntity {

    @Column(length = 225, nullable = false)
    private String name;

    @Column(length = 225, nullable = false)
    private String description;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<User> users;

    @ManyToMany
    @JoinTable(
            name = "roles_permission_set",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_set_id")
    )
    @ToString.Exclude
    private List<PermissionSet> permissions;

    public Role() {
        super();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Role role = (Role) o;
        return getId() != 0 && Objects.equals(getId(), role.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

