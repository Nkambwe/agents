package com.pbu.wendi.repositories.sam.repos;

import com.pbu.wendi.model.sam.models.PermissionSet;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SetRepository extends JpaRepository<PermissionSet, Long> {
    boolean existsById(long row_id);
    boolean existsBySetName(String orgName);
    boolean existsBySetNameAndIdNot(String setName, long id);
    boolean existsByDescription(String description);
    boolean existsByDescriptionAndIdNot(String description, long id);

    PermissionSet findById(long id);
    PermissionSet findBySetName(String name);

    @Transactional
    @Modifying
    @Query("UPDATE PermissionSet a SET a.isDeleted = :status WHERE a.id = :id")
    void markAsDeleted(@Param("id") long id, @Param("status") boolean status);

    @Transactional
    @Modifying
    @Query("UPDATE PermissionSet s SET s.isLocked = :isLocked WHERE s.id = :id")
    void lockSet(@Param("id") long id, @Param("isLocked") boolean isLocked);

    @Query("SELECT p FROM PermissionSet p JOIN p.roles rs WHERE rs.id = :roleId")
    List<PermissionSet> findAllByRoleId(@Param("roleId") long roleId);
    @Transactional
    @Modifying
    @Query("UPDATE PermissionSet a SET a.setName = :#{#permission.setName}, a.isDeleted = :#{#permission.isDeleted}, a.description = :#{#permission.description}, a.isLocked = :#{#permission.isLocked} WHERE a.id = :#{#permission.id}")
    void updateSet(@Param("permission") PermissionSet permission);
}
