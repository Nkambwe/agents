package com.pbu.wendi.repositories.agents.repos;

import com.pbu.wendi.model.agents.models.BusinessAgent;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BusinessRepository extends JpaRepository<BusinessAgent, Long> {

    boolean existsBySortCode(String sortCode);

    boolean existsByPinNumber(String pin);

    boolean existsByBusinessTin(String tin);

    boolean existsByBusinessNameAndIdNot(String businessName, long id);

    @Query("SELECT DISTINCT b FROM BusinessAgent b " +
            "LEFT JOIN FETCH b.signatories " +
            "LEFT JOIN FETCH b.approvals " +
            "LEFT JOIN FETCH b.affiliation " +
            "LEFT JOIN FETCH b.operators WHERE b.id = ?1")
    BusinessAgent findByIdWithRelatedEntities(Long id);

    @Query("SELECT DISTINCT b FROM BusinessAgent b " +
            "LEFT JOIN FETCH b.signatories " +
            "LEFT JOIN FETCH b.approvals " +
            "LEFT JOIN FETCH b.affiliation " +
            "LEFT JOIN FETCH b.operators WHERE b.sortCode = ?1")
    BusinessAgent findBySortCode(String sortCode);

    @Query("SELECT DISTINCT b FROM BusinessAgent b " +
            "LEFT JOIN FETCH b.signatories " +
            "LEFT JOIN FETCH b.approvals " +
            "LEFT JOIN FETCH b.affiliation " +
            "LEFT JOIN FETCH b.operators WHERE b.businessName = ?1")
    BusinessAgent findByBusinessName(String businessName);

    @Query("SELECT b FROM BusinessAgent b WHERE b.affiliation.id = ?1")
    List<BusinessAgent> findByAffiliationId(Long affiliationId);

    @Query("SELECT DISTINCT b FROM BusinessAgent b LEFT JOIN FETCH b.approvals LEFT JOIN FETCH b.affiliation LEFT JOIN FETCH b.operators")
    List<BusinessAgent> findAllWithRelatedEntities();

    @Query("SELECT DISTINCT b FROM BusinessAgent b LEFT JOIN FETCH b.approvals LEFT JOIN FETCH b.affiliation LEFT JOIN FETCH b.operators WHERE b.isDeleted=:is_deleted")
    List<BusinessAgent> findAllWithRelatedEntities(@Param("is_deleted") boolean is_deleted);

    @Transactional
    @Modifying
    @Query("UPDATE BusinessAgent b SET b.regNo = :#{#business.regNo}, b.registeredName = :#{#business.registeredName}, b.sameName = :#{#business.sameName}, b.businessName = :#{#business.businessName}, b.businessNature = :#{#business.businessNature}, b.incorporatedOn = :#{#business.incorporatedOn}, b.businessTin = :#{#business.businessTin}, b.businessTel = :#{#business.businessTel}, b.postalOffice = :#{#business.postalOffice}, b.yearsInBusiness = :#{#business.yearsInBusiness}, b.outlets = :#{#business.outlets}, b.district = :#{#business.district}, b.villageOrParishOrCounty = :#{#business.villageOrParishOrCounty}, b.physicalAddress = :#{#business.physicalAddress}, b.longitude = :#{#business.longitude}, b.latitude = :#{#business.latitude} WHERE b.id = :#{#business.id}")
    void update(@Param("business") BusinessAgent business);

    @Transactional
    @Modifying
    @Query("UPDATE BusinessAgent b SET b.isDeleted = :status WHERE b.id = :id")
    void markAsDeleted(@Param("id") long id, @Param("status") boolean status);
}

