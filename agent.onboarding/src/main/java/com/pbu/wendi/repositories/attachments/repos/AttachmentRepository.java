package com.pbu.wendi.repositories.attachments.repos;

import com.pbu.wendi.model.attachments.models.Attachment;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    boolean existsById(long id);
    Attachment findById(long id);
    List<Attachment> findAllByOwnerId(long owner_id);
    boolean existsAttachmentByAttDescrAndOwnerId(String att_descr, long owner_id);
    @Transactional
    @Modifying
    @Query("UPDATE Attachment a SET a.isDeleted = :status WHERE a.id = :id_no")
    void markAsDeleted(@Param("id_no") long id, @Param("status") boolean status);

    @Transactional
    @Modifying
    @Query("UPDATE Attachment a SET a.ownerId = :#{#doc.ownerId}, a.isDeleted = :#{#doc.isDeleted}, a.attDescr = :#{#doc.attDescr}, a.issueDate = :#{#doc.issueDate}, a.expiryDate = :#{#doc.expiryDate}, a.fileUrl = :#{#doc.fileUrl}, a.data = :#{#doc.data}  WHERE a.id = :#{#doc.id}")
    void updateAttachment(@Param("doc") Attachment doc);
}
