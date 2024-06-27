package com.pbu.wendi.repositories.agents.repos;

import com.pbu.wendi.model.agents.models.Partner;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface PartnerRepository extends JpaRepository<Partner, Long> {
    boolean existsByFullNameAndAgentId(String fullName, Long agentId);
    boolean existsByIdAndFullNameAndAgentId(Long id, String fullName, Long agentId);
    boolean existsByFullNameAndAgentIdAndIdNot(String fullName, Long agentId,Long id);
    Optional<Partner> findByFullNameAndAgent_Id(String fullName, Long agentId);
    List<Partner> findByAgent_Id(Long agentId);
    @Transactional
    @Modifying
    @Query("UPDATE Partner p SET p.fullName = :#{#partner.fullName}, p.birthDate = :#{#partner.birthDate}, p.contactNo = :#{#partner.contactNo} WHERE p.id = :#{#partner.id}")
    void update(@Param("partner") Partner partner);

    @Transactional
    @Modifying
    @Query("UPDATE Partner p SET p.isDeleted = :status WHERE p.id = :id")
    void markAsDeleted(@Param("id") long id, @Param("status") boolean status);
}
