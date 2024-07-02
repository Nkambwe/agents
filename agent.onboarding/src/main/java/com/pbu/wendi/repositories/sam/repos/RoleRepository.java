package com.pbu.wendi.repositories.sam.repos;

import com.pbu.wendi.model.sam.models.Role;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoleRepository extends JpaRepository<Role, Long> {
    boolean existsById(long id);
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name, long id);
    boolean existsByDescription(String description);
    boolean existsByDescriptionAndIdNot(String description, long id);
    @Query("SELECT COUNT(r) > 0 FROM Role r WHERE r.id = :id")
    boolean checkRole(@Param("id") long id);
    Role findById(long id);
    @Transactional
    @Modifying
    @Query("UPDATE Role r SET r.name = :#{#role.name}, r.isDeleted = :#{#role.isDeleted}, r.description = :#{#role.description} WHERE r.id = :#{#role.id}")
    void update(@Param("role") Role role);
    @Transactional
    @Modifying
    @Query("UPDATE Role r SET r.isDeleted = :status WHERE r.id = :id")
    void markAsDeleted(@Param("id") long id, @Param("status") boolean status);
}
