package com.pbu.wendi.repositories.agents.repos;

import com.pbu.wendi.model.agents.models.Operator;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OperatorRepository extends JpaRepository<Operator, Long> {
    boolean existsById(long id);
    boolean existsByOperatorNameAndPersonIdAndIdNot(String name, Long personId, long id);
    boolean existsByOperatorNameAndBusinessIdAndIdNot(String name, Long businessId, long id);
    boolean existsByIdNin(String nin);
    boolean existsByIdNinAndIdNot(String nin, long id);
    boolean existsByOperatorNameAndBusiness_Id(String operatorName, Long businessId);
    boolean existsByOperatorNameAndPerson_Id(String operatorName, Long personId);
    List<Operator> findByPerson_Id(Long personId);
    List<Operator> findByBusiness_Id(Long businessId);

    @Transactional
    @Modifying
    @Query("UPDATE Operator o SET o.operatorName=:#{#operator.operatorName}, o.gender=:#{#operator.gender}, o.phoneNo=:#{#operator.phoneNo}, o.idNin=:#{#operator.idNin}, o.channels=:#{#operator.channels}, o.bankName=:#{#operator.bankName}, o.accountNo=:#{#operator.accountNo}, o.accountName=:#{#operator.accountName}, o.hasCrime=:#{#operator.hasCrime}, o.crimeRef=:#{#operator.crimeRef}, o.hasActiveCrime =:#{#operator.hasActiveCrime}, o.activeCrimeRef=:#{#operator.activeCrimeRef} WHERE o.id=:#{#operator.id}")
    void update(@Param("operator") Operator operator);

    @Transactional
    @Modifying
    @Query("UPDATE Operator o SET o.isDeleted = :status WHERE o.id = :id")
    void markAsDeleted(@Param("id") long id, @Param("status") boolean status);
}
