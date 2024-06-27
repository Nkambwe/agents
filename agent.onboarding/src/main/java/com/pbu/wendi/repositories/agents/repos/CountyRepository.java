package com.pbu.wendi.repositories.agents.repos;

import com.pbu.wendi.model.agents.models.County;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CountyRepository  extends JpaRepository<County, Long> {
    boolean existsById(long id);
    boolean existsByCountyName(String countyName);
    boolean existsByCountyNameAndIdNot(String countyName, long id);

    County findById(long id);

    County findByCountyName(@Param("countyName") String countyName);

    @Query("SELECT t.id, t.countyName, t.isDeleted FROM County t WHERE t.isDeleted=false")
    List<County> findActiveCounties();

    @Transactional
    @Modifying
    @Query("UPDATE County t SET t.isDeleted=:status WHERE t.id=:id")
    void markAsDeleted(@Param("id") long id, @Param("status") boolean status);

    @Transactional
    @Modifying
    @Query("UPDATE County t SET t.countyName = :#{#county.countyName}, t.isDeleted = :#{#county.isDeleted} WHERE t.id = :#{#county.id}")
    void updateCounty(@Param("county") County county);

}
