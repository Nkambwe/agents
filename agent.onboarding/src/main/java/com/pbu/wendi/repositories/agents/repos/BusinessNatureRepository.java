package com.pbu.wendi.repositories.agents.repos;

import com.pbu.wendi.model.agents.models.BusinessNature;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BusinessNatureRepository extends JpaRepository<BusinessNature, Long> {
    boolean existsById(long id);
    boolean existsByNature(String nature);
    boolean existsByNatureAndIdNot(String nature, long id);

    BusinessNature findById(long id);
    BusinessNature findByNature(@Param("nature") String nature);

    @Query("SELECT t.id, t.parishName, t.isDeleted FROM Parish t WHERE t.isDeleted=false")
    List<BusinessNature> findActiveBusinessNatures();

    @Transactional
    @Modifying
    @Query("UPDATE BusinessNature t SET t.isDeleted=:status WHERE t.id=:id")
    void markAsDeleted(@Param("id") long id, @Param("status") boolean status);

    @Transactional
    @Modifying
    @Query("UPDATE BusinessNature t SET t.nature = :#{#parish.nature}, t.isDeleted = :#{#parish.isDeleted} WHERE t.id = :#{#parish.id}")
    void updateBusinessNatures(@Param("parish") BusinessNature parish);

}
