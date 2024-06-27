package com.pbu.wendi.repositories.agents.repos;

import com.pbu.wendi.model.agents.models.Bank;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BankRepository extends JpaRepository<Bank, Long> {
    boolean existsById(long id);
    boolean existsByBankName(String bankName);
    boolean existsByBankNameAndIdNot(String bankName, long id);
    boolean existsBySortCode(String sortCode);
    boolean existsBySortCodeAndIdNot(String sortCode, long id);
    Bank findById(long id);
    @Query("SELECT b.id, b.sortCode, b.bankName, b.isDeleted FROM Bank b WHERE b.sortCode=:code")
    Bank findBySortCode(@Param("code") String code);
    @Query("SELECT b.id, b.sortCode, b.bankName, b.isDeleted FROM Bank b WHERE b.isDeleted=false")
    List<Bank> findActiveBanks();
    @Transactional
    @Modifying
    @Query("UPDATE Bank b SET b.isDeleted = :status WHERE b.id = :id")
    void markAsDeleted(@Param("id") long id, @Param("status") boolean status);
    @Transactional
    @Modifying
    @Query("UPDATE Bank b SET b.sortCode = :#{#bank.sortCode}, b.bankName = :#{#bank.bankName}, b.isDeleted = :#{#bank.isDeleted} WHERE b.id = :#{#bank.id}")
    void updateBank(@Param("bank") Bank bank);
}
