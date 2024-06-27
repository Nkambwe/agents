package com.pbu.wendi.model.attachments.models;

import com.pbu.wendi.utils.DomainEntity;
import com.pbu.wendi.utils.enums.AttachmentType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;

import java.time.LocalDateTime;
import java.util.Objects;

@Data
@SuperBuilder
@Entity
@ToString(callSuper=true)
public class Identification extends DomainEntity {
    @Column(nullable=false)
    private long ownerId;

    @Column(length = 60, nullable=false)
    private String idNo;

    @Column(nullable=false)
    private AttachmentType docType;

    @Column(nullable=false)
    private LocalDateTime issueDate;

    @Column
    private LocalDateTime expiryDate;

    @Column(length = 400, nullable=false)
    private String fileUrl;

    @Column(nullable=false)
    private String data;

    @ManyToOne
    private IdentificationType type;

    public Identification() {
        super();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Identification that = (Identification) o;
        return getId() != 0 && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
