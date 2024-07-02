package com.pbu.wendi.model.sam.models;

import com.pbu.wendi.utils.DomainEntity;
import jakarta.persistence.*;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Objects;

/**
 * Class Name: Role
 * Extends : DomainEntity class a base class for all database entities
 * Description: Class handles system Permission and is mapped to the table system_permission_set
 * Created By: Nkambwe mark
 */

@Data
@SuperBuilder
@Entity
@ToString(callSuper=true)
@Table(name="ag_permission")
public class Permission extends DomainEntity {
    @Column(length = 220, nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private boolean isLocked;

    @ManyToMany(mappedBy = "permissions")
    @ToString.Exclude
    private List<PermissionSet> permissionSets;

    public Permission() {
        super();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Permission that = (Permission) o;
        return getId() != 0 && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
