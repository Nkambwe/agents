package com.pbu.wendi.services.agents.services;

import com.pbu.wendi.requests.agents.dto.WalletRequest;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface WalletService {
    CompletableFuture<Boolean> walletExists(long id);
    CompletableFuture<Boolean> existsByName(String name);
    CompletableFuture<Boolean>  existsByNameAndNotId(String name, Long id);
    CompletableFuture<WalletRequest> findWalletById(long id);
    CompletableFuture<WalletRequest> findWalletByName(String name);
    CompletableFuture<List<WalletRequest>> getAllWallet();
    CompletableFuture<List<WalletRequest>> getActiveWallets();
    CompletableFuture<WalletRequest> create(WalletRequest wallet) throws InterruptedException;
    void update(WalletRequest wallet);
    void softDelete(long id, boolean deleted);
    void delete(long id);
}
