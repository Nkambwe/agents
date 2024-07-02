package com.pbu.wendi.repositories.attachments.repos;

import com.pbu.wendi.model.attachments.models.IdentificationType;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IdentificationTypeRepository extends JpaRepository<IdentificationType, Long> {
    boolean existsById(long id);
    IdentificationType findById(long id);
    boolean existsByTypeName(String type_name);

    boolean existsByTypeNameAndIdNot(String type_name, long id);

    @Transactional
    @Modifying
    @Query("UPDATE IdentificationType t SET t.isDeleted = :status WHERE t.id = :id")
    void markAsDeleted(@Param("id") long id, @Param("status") boolean status);

    @Transactional
    @Modifying
    @Query("UPDATE IdentificationType t SET t.typeName = :#{#type.typeName}, t.isDeleted = :#{#type.isDeleted} WHERE t.id = :#{#type.id}")
    void updateType(@Param("type") IdentificationType type);
}
