package com.pbu.wendi.model.sam.models;

import com.pbu.wendi.utils.DomainEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Class Name: App
 * Extends : DomainEntity class a base class for all database entities
 * Description: Class handles business branch that a user is attached to and is mapped to the table business_branches
 * Created By: Nkambwe mark
 */

@Data
@SuperBuilder
@Entity
@ToString(callSuper=true)
public class Branch extends DomainEntity {
    @Column(length = 10, nullable = false)
    private String solId;

    @Column(length = 120, nullable = false)
    private String branchName;

    @Column(nullable = false)
    private boolean isActive;

    @Column(nullable = false)
    private LocalDateTime createdOn;

    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<User> users;

    public Branch() {
        super();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Branch branch = (Branch) o;
        return getId() != 0 && Objects.equals(getId(), branch.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
