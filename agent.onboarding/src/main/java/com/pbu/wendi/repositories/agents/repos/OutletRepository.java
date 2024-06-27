package com.pbu.wendi.repositories.agents.repos;

import com.pbu.wendi.model.agents.models.Outlet;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OutletRepository extends JpaRepository<Outlet, Long> {
    boolean existsById(long id);
    boolean existsByOutletName(String telecomName);
    boolean existsByOutletNameAndIdNot(String telecomName, long id);

    Outlet findById(long id);
    Outlet findByOutletName(@Param("name") String name);

    @Query("SELECT t.id, t.outletName, t.isDeleted FROM Outlet t WHERE t.isDeleted=false")
    List<Outlet> findActiveOutlets();

    @Transactional
    @Modifying
    @Query("UPDATE Outlet t SET t.isDeleted = :status WHERE t.id = :id")
    void markAsDeleted(@Param("id") long id, @Param("status") boolean status);

    @Transactional
    @Modifying
    @Query("UPDATE Outlet t SET t.outletName = :#{#outlet.outletName}, t.isDeleted = :#{#outlet.isDeleted} WHERE t.id = :#{#outlet.id}")
    void updateTelecom(@Param("telecom") Outlet outlet);
}
