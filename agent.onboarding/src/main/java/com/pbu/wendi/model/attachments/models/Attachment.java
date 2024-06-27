package com.pbu.wendi.model.attachments.models;

import com.pbu.wendi.utils.DomainEntity;
import com.pbu.wendi.utils.enums.AttachmentType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class Attachment extends DomainEntity {
    @Column(nullable=false)
    private long ownerId;

    @Column(nullable=false)
    private AttachmentType docType;

    @Column(length = 220, nullable=false)
    private String attDescr;

    @Column(nullable=false)
    private LocalDateTime issueDate;

    @Column
    private LocalDateTime expiryDate;

    @Column(length = 400, nullable=false)
    private String fileUrl;

    @Column(nullable=false)
    private String data;

    public Attachment() {
        super();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Attachment that = (Attachment) o;
        return getId() != 0 && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
