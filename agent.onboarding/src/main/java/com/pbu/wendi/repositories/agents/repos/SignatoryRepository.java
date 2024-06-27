package com.pbu.wendi.repositories.agents.repos;

import com.pbu.wendi.model.agents.models.Signatory;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SignatoryRepository extends JpaRepository<Signatory, Long> {
    boolean existsById(long roleId);
    boolean existsByFullName(String name);
    boolean existsByFullNameAndIdNot(String name, long id);
    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM Signatory s " +
            "LEFT JOIN s.person p " +
            "LEFT JOIN s.business b " +
            "WHERE s.fullName = :name " +
            "AND (p.id = :agentId OR b.id = :agentId)")
    boolean existsByFullNameAndPersonIdOrBusinessId(@Param("name") String name, @Param("agentId") long agentId);

    List<Signatory> findByBusinessId(Long businessId);

    List<Signatory> findByPersonId(Long personId);

    @Transactional
    @Modifying
    @Query("UPDATE Signatory s SET s.isDeleted = :status WHERE s.id = :id")
    void softDelete(@Param("id") long id, @Param("status") boolean status);

    @Transactional
    @Modifying
    @Query("UPDATE Signatory s SET s.fullName = :#{#signatory.fullName}, s.signatureUrl = :#{#signatory.signatureUrl}, s.isDeleted = :#{#signatory.isDeleted}, s.business.id = :#{#signatory.business.id} WHERE s.id = :#{#signatory.id}")
    void updateSignatoryWithBusiness(@Param("signatory") Signatory signatory);

    @Transactional
    @Modifying
    @Query("UPDATE Signatory s SET s.fullName = :#{#signatory.fullName}, s.signatureUrl = :#{#signatory.signatureUrl}, s.isDeleted = :#{#signatory.isDeleted}, s.person.id = :#{#signatory.person.id} WHERE s.id = :#{#signatory.id}")
    void updateSignatoryWithPerson(@Param("signatory") Signatory signatory);
}
