package com.pbu.wendi.model.sam.models;

import com.pbu.wendi.utils.DomainEntity;
import jakarta.persistence.*;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Class Name: User
 * Extends : DomainEntity class a base class for all database entities
 * Description: Class handles user logs details and is mapped to the table system_logs
 * Created By: Nkambwe mark
 */

@Data
@SuperBuilder
@Entity
@ToString(callSuper=true)
public class SystemLog extends DomainEntity {
    @Column(length = 100, nullable = false)
    private String action;

    @Column(nullable = false)
    private LocalDateTime logTime;

    @Column(length = 40, nullable = false)
    private String ipAddress;

    @ManyToOne
    @JoinColumn(name = "log_id")
    private User user;

    public SystemLog() {
        super();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        SystemLog systemLog = (SystemLog) o;
        return getId() != 0 && Objects.equals(getId(), systemLog.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
