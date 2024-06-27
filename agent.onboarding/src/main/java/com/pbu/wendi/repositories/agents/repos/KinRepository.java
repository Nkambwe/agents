package com.pbu.wendi.repositories.agents.repos;

import com.pbu.wendi.model.agents.models.Kin;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface KinRepository extends JpaRepository<Kin, Long> {
    boolean existsByFullNameAndAgentId(String fullName, Long agentId);
    boolean existsByIdAndFullNameAndAgentId(Long id, String fullName, Long agentId);
    Optional<Kin> findByFullNameAndAgent_Id(String fullName, Long agentId);
    List<Kin> findByAgent_Id(Long agentId);

    @Transactional
    @Modifying
    @Query("UPDATE Kin k SET k.fullName = :#{#kin.fullName}, k.address = :#{#kin.address}, k.email = :#{#kin.email}, k.relationship = :#{#kin.relationship}, k.phoneNo = :#{#kin.phoneNo} WHERE k.id = :#{#kin.id}")
    void update(@Param("kin") Kin kin);

    @Transactional
    @Modifying
    @Query("UPDATE Kin k SET k.isDeleted = :status WHERE k.id = :id")
    void markAsDeleted(@Param("id") long id, @Param("status") boolean status);
}
