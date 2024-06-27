package com.pbu.wendi.repositories.agents.repos;

import com.pbu.wendi.model.agents.models.Parish;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ParishRepository extends JpaRepository<Parish, Long> {
    boolean existsById(long id);
    boolean existsByParishName(String parishName);
    boolean existsByParishNameAndIdNot(String parishName, long id);

    Parish findById(long id);
    Parish findByParishName(@Param("name") String name);

    @Query("SELECT t.id, t.parishName, t.isDeleted FROM Parish t WHERE t.isDeleted=false")
    List<Parish> findActiveParish();

    @Transactional
    @Modifying
    @Query("UPDATE Parish t SET t.isDeleted=:status WHERE t.id=:id")
    void markAsDeleted(@Param("id") long id, @Param("status") boolean status);

    @Transactional
    @Modifying
    @Query("UPDATE Parish t SET t.parishName = :#{#parish.parishName}, t.isDeleted = :#{#parish.isDeleted} WHERE t.id = :#{#parish.id}")
    void updateParish(@Param("parish") Parish parish);

}
