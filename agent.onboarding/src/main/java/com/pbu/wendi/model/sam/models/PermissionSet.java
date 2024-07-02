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
 * Description: Class handles collection of Permission bundled into a single
 * set details and is mapped to the table system_permission_set
 * Created By: Nkambwe mark
 */

@Data
@SuperBuilder
@Entity
@ToString(callSuper=true)
public class PermissionSet extends DomainEntity {
    @Column(length = 220, nullable = false)
    private String setName;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private boolean isLocked;

    @ManyToMany
    @JoinTable(
            name = "permission_set_permissions",
            joinColumns = @JoinColumn(name = "permission_set_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    @ToString.Exclude
    private List<Permission> permissions;

    @ManyToMany(mappedBy = "permissions")
    @ToString.Exclude
    private List<Role> roles;

    public PermissionSet() {
        super();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PermissionSet that = (PermissionSet) o;
        return getId() != 0 && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}