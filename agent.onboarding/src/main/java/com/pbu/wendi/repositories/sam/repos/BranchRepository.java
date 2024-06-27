package com.pbu.wendi.repositories.sam.repos;

import com.pbu.wendi.model.sam.models.Branch;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BranchRepository extends JpaRepository<Branch, Long> {
    Branch findByBranchName(String name);
    Branch findBySolId(String solId);
    boolean existsBySolId(String solId);
    boolean existsByBranchName(String branchName);
    boolean existsBySolIdAndIdNot(String solId, long id);
    boolean existsByBranchNameAndIdNot(String branchName, long id);
    @Transactional
    @Modifying
    @Query("UPDATE Branch b SET b.branchName = :#{#branch.branchName}, b.isDeleted = :#{#branch.isDeleted}, b.solId = :#{#branch.solId}," +
            " b.isActive = :#{#branch.isActive}, b.createdOn = :#{#branch.createdOn} WHERE b.id = :#{#branch.id}")
    void update(@Param("branch") Branch branch);
    @Transactional
    @Modifying
    @Query("UPDATE Branch b SET b.isDeleted = :status WHERE b.id = :id")
    void markAsDeleted(@Param("id") long id, @Param("status") boolean status);
    @Transactional
    @Modifying
    @Query("UPDATE Branch b SET b.isActive = :status WHERE b.id = :id")
    void activateBranch(@Param("id") long id, @Param("status") boolean status);
}
