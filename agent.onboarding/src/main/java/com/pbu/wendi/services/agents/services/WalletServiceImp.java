package com.pbu.wendi.services.agents.services;

import com.pbu.wendi.model.agents.models.Wallet;
import com.pbu.wendi.repositories.agents.repos.WalletRepository;
import com.pbu.wendi.requests.agents.dto.WalletRequest;
import com.pbu.wendi.utils.common.AppLoggerService;
import com.pbu.wendi.utils.exceptions.GeneralException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class WalletServiceImp implements WalletService {
    private final AppLoggerService logger;
    private final ModelMapper mapper;
    private final WalletRepository wallets;

    public WalletServiceImp(AppLoggerService logger, ModelMapper mapper, WalletRepository wallets) {
        this.logger = logger;
        this.mapper = mapper;
        this.wallets = wallets;
    }

    @Override
    public CompletableFuture<Boolean> walletExists(long id) {
        logger.info("Check if wallet exists");
        try{
            return CompletableFuture.completedFuture(wallets.existsById(id));
        } catch(Exception ex){
            logger.info("An error occurred in method 'walletExists' in 'WalletService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<Boolean> existsByName(String name) {
        logger.info("Check if wallet exists");
        try{
            return CompletableFuture.completedFuture(wallets.existsByWalletName(name));
        } catch(Exception ex){
            logger.info("An error occurred in method 'existsByName' in 'WalletService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<Boolean> existsByNameAndNotId(String name, Long id) {
        logger.info(String.format("Retrieving Wallet with name %s and agent Id %s", name, id));
        try{
            return CompletableFuture.completedFuture(wallets.existsByWalletNameAndIdNot(name, id));
        } catch(Exception ex){
            logger.info("An error occurred in method 'existsByNameAndNotId' in 'WalletService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<WalletRequest> findWalletById(long id) {
        logger.info(String.format("Retrieving Wallet with ID %s", id));
        try{

            Wallet record = wallets.findById(id);
            if (record != null) {
                WalletRequest request = this.mapper.map(record, WalletRequest.class);
                return CompletableFuture.completedFuture(request);
            }

            return CompletableFuture.completedFuture(null);
        } catch(Exception ex){
            logger.info("An error occurred in method 'findWalletById' in 'WalletService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<WalletRequest> findWalletByName(String name) {
        logger.info(String.format("Retrieving Wallet with name %s", name));
        try{

            Wallet record = wallets.findByWalletName(name);
            if (record != null) {
                WalletRequest request = this.mapper.map(record, WalletRequest.class);
                return CompletableFuture.completedFuture(request);
            }

            return CompletableFuture.completedFuture(null);
        } catch(Exception ex){
            logger.info("An error occurred in method 'findWalletByName' in 'WalletService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<List<WalletRequest>> getAllWallet() {
        logger.info("Retrieve all Wallets");
        List<WalletRequest> records  = new ArrayList<>();
        try{
            //..get wallet records
            List<Wallet> walletRecords = wallets.findAll();
            if(!walletRecords.isEmpty()){
                for (Wallet record : walletRecords) {
                    records.add(this.mapper.map(record, WalletRequest.class));
                }
            }

            return CompletableFuture.completedFuture(records);
        } catch(Exception ex){
            logger.info("An error occurred in method 'getAllWallet' in 'WalletService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<List<WalletRequest>> getActiveWallets() {
        logger.info("Retrieve active wallets");
        List<WalletRequest> records  = new ArrayList<>();
        try{
            //..get telecom records
            List<Wallet>telecomRecords = wallets.findActiveWallet();
            if(!telecomRecords.isEmpty()){
                for (Wallet record : telecomRecords) {
                    records.add(this.mapper.map(record, WalletRequest.class));
                }
            }

            return CompletableFuture.completedFuture(records);
        } catch(Exception ex){
            logger.info("An error occurred in method 'getActiveWallets' in 'WalletService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<WalletRequest> create(WalletRequest outlet) throws InterruptedException {
        logger.info("Adding new wallet");
        try{
            Wallet record = this.mapper.map(outlet, Wallet.class);
            wallets.save(record);
            record.setId(record.getId());
            return CompletableFuture.completedFuture(outlet);
        }catch(Exception ex){
            logger.info("An error occurred in method 'create' in 'WalletService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public void update(WalletRequest wallet) {
        logger.info("update wallet");
        try{
            Wallet record = this.mapper.map(wallet, Wallet.class);
            record.setId(wallet.getId());
            wallets.updateWallet(record);
        } catch(Exception ex){
            logger.info("An error occurred in method 'update' in WalletService");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public void softDelete(long id, boolean deleted) {
        logger.info("Soft Delete wallet");
        try{
            logger.info(String.format("Wallet is_deleted value set to %s", deleted ? "true": "false"));
            wallets.markAsDeleted(id, deleted);
        } catch(Exception ex){
            logger.info("An error occurred in method 'softDelete' in 'WalletService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public void delete(long id) {
        logger.info("Delete wallet");
        try{
            logger.info(String.format("Delete wallet with id %s", id));
            wallets.deleteById(id);
        } catch(Exception ex){
            logger.info("An error occurred in method 'delete' in 'WalletService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }
}
