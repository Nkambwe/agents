package com.pbu.wendi.model.sam.models;

import com.pbu.wendi.utils.DomainEntity;
import com.pbu.wendi.utils.enums.Gender;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
/**
 * Class Name: User
 * Extends : DomainEntity class a base class for all database entities
 * Description: Class handles System user details and is mapped to the table system_users
 * Created By: Nkambwe mark
 */

@Data
@SuperBuilder
@Entity
@ToString(callSuper=true)
public class User extends DomainEntity {
    @Column(nullable = false, length = 80)
    private String username;

    @Column(nullable = false, length = 80)
    private String firstname;

    @Column(nullable = false, length = 80)
    private String lastname;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
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

    @Column(nullable = false)
    private LocalDateTime lastPasswordChange;

    @Column(length = 80)
    private String modifiedBy;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @ManyToMany
    @JoinTable(
            name = "user_app",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "app_id")
    )
    @ToString.Exclude
    private List<App> apps;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SystemLog> logs;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ArchiveUser> uarchives;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ArchiveLog> larchives;

    public User() {
        super();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return getId() != 0 && Objects.equals(getId(), user.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

