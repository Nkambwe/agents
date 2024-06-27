package com.pbu.wendi.repositories.agents.repos;

import com.pbu.wendi.model.agents.models.IndividualAgent;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IndividualRepository extends JpaRepository<IndividualAgent, Long> {

    boolean existsBySortCode(String sortCode);

    boolean existsByPinNumber(String pin);

    boolean existsByPersonalTin(String tin);

    @Query("SELECT DISTINCT i FROM IndividualAgent i " +
            "LEFT JOIN FETCH i.partners " +
            "LEFT JOIN FETCH i.nextOfKin " +
            "LEFT JOIN FETCH i.signatories " +
            "LEFT JOIN FETCH i.approvals " +
            "LEFT JOIN FETCH i.affiliation " +
            "LEFT JOIN FETCH i.operators WHERE i.id = ?1")
    IndividualAgent findByIdWithRelatedEntities(Long id);

    @Query("SELECT DISTINCT i FROM IndividualAgent i " +
            "LEFT JOIN FETCH i.partners " +
            "LEFT JOIN FETCH i.nextOfKin " +
            "LEFT JOIN FETCH i.signatories " +
            "LEFT JOIN FETCH i.approvals " +
            "LEFT JOIN FETCH i.affiliation " +
            "LEFT JOIN FETCH i.operators WHERE i.sortCode = ?1")
    IndividualAgent findBySortCode(String sortCode);

    @Query("SELECT DISTINCT i FROM IndividualAgent i LEFT JOIN FETCH i.nextOfKin LEFT JOIN FETCH i.partners LEFT JOIN FETCH i.approvals LEFT JOIN FETCH i.affiliation LEFT JOIN FETCH i.operators")
    List<IndividualAgent> findIndividuals();

    @Query("SELECT i FROM IndividualAgent i WHERE i.affiliation.id = ?1")
    List<IndividualAgent> findByAffiliationId(Long affiliationId);

    @Query("SELECT DISTINCT i FROM IndividualAgent i LEFT JOIN FETCH i.approvals LEFT JOIN FETCH i.affiliation LEFT JOIN FETCH i.operators")
    List<IndividualAgent> findAllWithRelatedEntities();

    @Query("SELECT DISTINCT i FROM IndividualAgent i LEFT JOIN FETCH i.approvals LEFT JOIN FETCH i.affiliation LEFT JOIN FETCH i.operators WHERE i.isDeleted=:is_deleted")
    List<IndividualAgent> findAllWithRelatedEntities(@Param("is_deleted") boolean is_deleted);

    @Transactional
    @Modifying
    @Query("UPDATE IndividualAgent i SET i.firstName = :#{#person.firstName}, i.middleName = :#{#person.middleName}, i.surname = :#{#person.surname}, i.birthDate = :#{#person.birthDate}, i.gender = :#{#person.gender}, i.identificationId = :#{#person.identificationId}, i.personalTin = :#{#person.personalTin}, i.primaryContact = :#{#person.primaryContact}, i.secondaryContact = :#{#person.secondaryContact}, i.email = :#{#person.email},i.district = :#{#person.district},i.county = :#{#person.county},i.villageOrParish = :#{#person.villageOrParish} WHERE i.id = :#{#person.id}")
    void update(@Param("person") IndividualAgent person);

    @Transactional
    @Modifying
    @Query("UPDATE IndividualAgent i SET i.isDeleted = :status WHERE i.id = :id")
    void markAsDeleted(@Param("id") long id, @Param("status") boolean status);
}
