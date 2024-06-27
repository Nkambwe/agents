package com.pbu.wendi.repositories.attachments.repos;

import com.pbu.wendi.model.attachments.models.Identification;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IdentificationRepository extends JpaRepository<Identification, Long> {
    boolean existsByIdNo(String id_number);

    boolean existsByIdNoAndIdNot(String id_number, long id);
    Identification findById(long row_id);
    List<Identification> findAllByOwnerId(long owner_id);
    @Transactional
    @Modifying
    @Query("UPDATE Identification i SET i.isDeleted = :status WHERE i.id = :id")
    void markAsDeleted(@Param("id") long id, @Param("status") boolean status);

    @Transactional
    @Modifying
    @Query("UPDATE Identification i SET i.ownerId = :#{#doc.ownerId}, i.idNo = :#{#doc.idNo}, i.isDeleted = :#{#doc.isDeleted}, i.issueDate = :#{#doc.issueDate}, i.expiryDate = :#{#doc.expiryDate}, i.fileUrl = :#{#doc.fileUrl}, i.data = :#{#doc.data}  WHERE i.id = :#{#doc.id}")
    void updateIdentification(@Param("doc") Identification doc);
}