package com.pbu.wendi.repositories.agents.repos;

import com.pbu.wendi.model.agents.models.Wallet;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
    boolean existsById(long id);
    boolean existsByWalletName(String walletName);
    boolean existsByWalletNameAndIdNot(String walletName, long id);

    Wallet findById(long id);
    Wallet findByWalletName(@Param("name") String name);

    @Query("SELECT t.id, t.walletName, t.isDeleted FROM Wallet t WHERE t.isDeleted=false")
    List<Wallet> findActiveWallet();

    @Transactional
    @Modifying
    @Query("UPDATE Wallet t SET t.isDeleted = :status WHERE t.id = :id")
    void markAsDeleted(@Param("id") long id, @Param("status") boolean status);

    @Transactional
    @Modifying
    @Query("UPDATE Wallet t SET t.walletName = :#{#wallet.walletName}, t.isDeleted = :#{#wallet.isDeleted} WHERE t.id = :#{#wallet.id}")
    void updateWallet(@Param("wallet") Wallet wallet);
}
