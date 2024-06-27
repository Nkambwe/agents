package com.pbu.wendi.repositories.agents.repos;

import com.pbu.wendi.model.agents.models.Affiliation;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AffiliationRepository extends JpaRepository<Affiliation, Long> {
    boolean existsById(long row_id);
    boolean existsByOrgName(String orgName);
    boolean existsByOrgNameAndIdNot(String type_name, long id);
    boolean existsBySortCode(String sortCode);
    boolean existsBySortCodeAndIdNot(String sortCode, long id);
    Affiliation findById(long id);
    @Query("SELECT a.id, a.sortCode, a.orgName, a.isDeleted FROM Affiliation a WHERE a.sortCode=:code")
    Affiliation findBySortCode(@Param("code") String code);
    @Query("SELECT a.id, a.sortCode, a.orgName, a.isDeleted FROM Affiliation a WHERE a.isDeleted=false")
    List<Affiliation> findActiveAffiliations();
    @Transactional
    @Modifying
    @Query("UPDATE Affiliation a SET a.isDeleted = :status WHERE a.id = :id")
    void markAsDeleted(@Param("id") long id, @Param("status") boolean status);
    @Transactional
    @Modifying
    @Query("UPDATE Affiliation a SET a.sortCode = :#{#affiliation.sortCode}, a.orgName = :#{#affiliation.orgName}, a.isDeleted = :#{#affiliation.isDeleted} WHERE a.id = :#{#affiliation.id}")
    void updateAffiliation(@Param("affiliation") Affiliation affiliation);
}
