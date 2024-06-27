package com.pbu.wendi.model.sam.models;

import com.pbu.wendi.utils.DomainEntity;
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
 * Class Name: Role
 * Extends : DomainEntity class a base class for all database entities
 * Description: Class handles system log archive record
 * Created By: Nkambwe mark
 */

@Data
@SuperBuilder
@Entity
@ToString(callSuper=true)
public class ArchiveLog extends DomainEntity {
    @Column(length = 100, nullable = false)
    private String action;

    @Column(nullable = false)
    private LocalDateTime logTime;

    @Column(length = 40, nullable = false)
    private String ipAddress;

    @Column(length = 20, nullable = false)
    private String logForUser;

    @Column(nullable = false)
    private LocalDateTime archivedOn;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public ArchiveLog() {
        super();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ArchiveLog that = (ArchiveLog) o;
        return getId() != 0 && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
