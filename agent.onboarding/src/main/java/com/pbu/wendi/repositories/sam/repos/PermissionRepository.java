package com.pbu.wendi.repositories.sam.repos;

import com.pbu.wendi.model.sam.models.Permission;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    boolean existsById(long row_id);
    boolean existsByName(String permissionName);
    boolean existsByNameAndIdNot(String permissionName, long id);
    boolean existsByDescription(String description);
    boolean existsByDescriptionAndIdNot(String description, long id);

    @Query("SELECT p FROM Permission p JOIN p.permissionSets ps WHERE ps.id = :setId")
    List<Permission> findAllByPermissionSetId(@Param("setId") long setId);

    @Query("SELECT p FROM Permission p " +
            "JOIN p.permissionSets ps " +
            "JOIN ps.roles r " +
            "WHERE r.id = :roleId")
    List<Permission> findPermissionsByRoleId(@Param("roleId") Long roleId);

    @Transactional
    @Modifying
    @Query("UPDATE Permission p SET p.isLocked = :isLocked WHERE p.id = :id")
    void lockPermission(@Param("id") long id, @Param("isLocked") boolean isLocked);

    @Transactional
    @Modifying
    @Query("UPDATE Permission p SET p.name = :#{#permission.setName}, p.isDeleted = :#{#permission.isDeleted}, p.isLocked = :#{#permission.isLocked} WHERE p.id = :#{#permission.id}")
    void update(@Param("permission") Permission permission);
}
