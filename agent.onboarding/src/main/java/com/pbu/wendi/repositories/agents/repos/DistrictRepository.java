package com.pbu.wendi.repositories.agents.repos;

import com.pbu.wendi.model.agents.models.District;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DistrictRepository extends JpaRepository<District, Long> {
    boolean existsById(long id);
    boolean existsByDistrictName(String districtName);
    boolean existsByDistrictNameAndIdNot(String districtName, long id);

    District findById(long id);
    District findByDistrictName(@Param("name") String name);

    @Query("SELECT d.id, d.districtName, d.isDeleted FROM District d WHERE d.isDeleted=false")
    List<District> findActiveDistricts();

    @Transactional
    @Modifying
    @Query("UPDATE District d SET d.isDeleted = :status WHERE d.id = :id")
    void markAsDeleted(@Param("id") long id, @Param("status") boolean status);

    @Transactional
    @Modifying
    @Query("UPDATE District d SET d.districtName = :#{#district.districtName}, d.isDeleted = :#{#district.isDeleted} WHERE d.id = :#{#district.id}")
    void updateDistrict(@Param("district") District district);
}
