package com.pbu.wendi.repositories.agents.repos;


import com.pbu.wendi.model.agents.models.AgentSettings;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface SettingRepository  extends JpaRepository<AgentSettings, Long> {
    Boolean existsById(long id);
    boolean existsByParamName(String paramName);
    Optional<AgentSettings> findByParamName(String paramName);
    @Query("SELECT sr FROM AgentSettings sr WHERE sr.paramName IN :paramNames")
    List<AgentSettings> findAllWhereParamNameIn(@Param("paramNames") List<String> paramNames);
    @Transactional
    @Modifying
    @Query("UPDATE AgentSettings a SET a.paramName=:#{#setting.paramName}, a.paramValue=:#{#setting.paramValue}, a.description=:#{#setting.description}, a.isDeleted=:#{#setting.isDeleted} WHERE a.id=:#{#setting.id}")
    void updateSettings(AgentSettings setting);
    @Transactional
    @Modifying
    @Query("UPDATE AgentSettings a SET a.isDeleted = :status WHERE a.id = :id")
    void softDelete(long id, boolean status);
}