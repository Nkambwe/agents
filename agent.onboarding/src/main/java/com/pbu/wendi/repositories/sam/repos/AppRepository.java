package com.pbu.wendi.repositories.sam.repos;

import com.pbu.wendi.model.sam.models.App;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AppRepository  extends JpaRepository<App, Long> {
    boolean existsById(long id);
    boolean existsByName(String name);
    App findById(long id);
    App findByName(String name);
    List<App> findAllByUsersId(long userId);
    boolean existsByNameAndIdNot(String name, long id);
    @Transactional
    @Modifying
    @Query("UPDATE App a SET a.isDeleted = :status WHERE a.id = :id")
    void markAsDeleted(@Param("id") long id, @Param("status") boolean status);

    @Transactional
    @Modifying
    @Query("UPDATE App a SET a.name = :#{#app.name}, a.isDeleted = :#{#app.isDeleted}, a.description = :#{#app.description} WHERE a.id = :#{#app.id}")
    void update(@Param("app") App app);
}
