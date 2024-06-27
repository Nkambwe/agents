package com.pbu.wendi.model.sam.models;

import com.pbu.wendi.utils.DomainEntity;
import com.pbu.wendi.utils.enums.Gender;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Class Name: ArchiveUser
 * Extends : DomainEntity class a base class for all database entities
 * Description: Class handles system users archive record
 * Created By: Nkambwe mark
 */

@Data
@SuperBuilder
@Entity
@ToString(callSuper=true)
public class ArchiveUser extends DomainEntity {

    @Column(nullable = false, length = 80)
    private String username;

    @Column(nullable = false, length = 80)
    private String firstname;

    @Column(nullable = false, length = 80)
    private String lastname;

    @Column(nullable = false)
    private Gender gender;

    @Column(nullable = false, length = 10)
    private String pfNo;

    @Column
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private boolean isLoggedIn;

    @Column(nullable = false)
    private boolean isActive;

    @Column(nullable = false)
    private boolean isVerified;

    @Column(length = 80)
    private String verifiedBy;

    @Column(length = 80, nullable = false)
    private String createdBy;

    @Column(nullable = false)
    private LocalDateTime createdOn;

    @Column
    private LocalDateTime modifiedOn;

    @Column(length = 80)
    private String modifiedBy;

    @Column(nullable = false)
    private long roleId;

    @Column(nullable = false)
    private long branchId;

    @Column(nullable = false)
    private LocalDateTime archivedOn;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public ArchiveUser() {
        super();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ArchiveUser that = (ArchiveUser) o;
        return getId() != 0 && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
