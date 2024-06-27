package com.pbu.wendi.repositories.agents.repos;

import com.pbu.wendi.model.agents.models.Approval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ApprovalRepository extends JpaRepository<Approval, Long> ,ApprovalExtensionRepository {
    boolean existsByPersonId(Long personId);
    boolean  existsByBusinessId(Long businessId);
    List<Approval> findAllByBusinessId(Long businessId);
    List<Approval> findAllByPersonId(Long personId);
    @Modifying
    @Query("UPDATE Approval a SET a.isDeleted = :status WHERE a.id = :id")
    void softDelete(@Param("id") long id, @Param("status") boolean status);
}
