package com.pbu.wendi.services.agents.services;

import com.pbu.wendi.model.agents.models.Bank;
import com.pbu.wendi.repositories.agents.repos.BankRepository;
import com.pbu.wendi.requests.agents.dto.BankRequest;
import com.pbu.wendi.utils.common.AppLoggerService;
import com.pbu.wendi.utils.exceptions.GeneralException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class BankServiceImp implements BankService {

    private final AppLoggerService logger;
    private final ModelMapper mapper;
    private final BankRepository repository;

    public BankServiceImp(AppLoggerService logger, ModelMapper mapper, BankRepository repository) {
        this.logger = logger;
        this.mapper = mapper;
        this.repository = repository;
    }
    @Override
    public CompletableFuture<Boolean> bankExists(long id) {
        logger.info(String.format("Checking bank with id '%s' exists",id));
        try{
            return CompletableFuture.completedFuture(repository.existsById(id));
        } catch(Exception ex){
            logger.info("An error occurred in method 'bankExists'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public boolean checkBankDuplicateName(String name) {
        logger.info(String.format("Checking whether another bank with name '%s' exists",name));
        try{
            return repository.existsByBankName(name);
        } catch(Exception ex){
            logger.info("An error occurred in method 'checkBankDuplicateName'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public boolean checkBankDuplicateNameWithDifferentIds(String name, long id) {
        logger.info(String.format("Checking whether another bank with name '%s' but not id '%s' exists",name, id));
        try{
            return repository.existsByBankNameAndIdNot(name, id);
        } catch(Exception ex){
            logger.info("An error occurred in method 'checkBankDuplicateNameWithDifferentIds'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public boolean checkBankDuplicateSortCode(String sort_code) {
        logger.info(String.format("Checking whether another bank with sort code '%s' exists",sort_code));
        try{
            return repository.existsBySortCode(sort_code);
        } catch(Exception ex){
            logger.info("An error occurred in method 'checkBankDuplicateSortCode'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public boolean checkBankDuplicateSortCodeWithDifferentIds(String sort_code, long id) {
        logger.info(String.format("Checking whether another bank with sort code '%s' but not id '%s' exists",sort_code, id));
        try{
            return repository.existsBySortCodeAndIdNot(sort_code, id);
        } catch(Exception ex){
            logger.info("An error occurred in method 'checkBankDuplicateSortCodeWithDifferentIds'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<BankRequest> findBankById(long id) {
        logger.info(String.format("Retrieving bank with ID %s", id));
        try{

            Bank bank = repository.findById(id);
            if(bank != null){
                BankRequest record = this.mapper.map(bank, BankRequest.class);
                return CompletableFuture.completedFuture(record);
            }
            return null;
        } catch(Exception ex){
            logger.info("An error occurred in method 'findBankById'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<BankRequest> findBankBySortCode(String sort_code) {
        logger.info(String.format("Retrieving bank with sort code %s", sort_code));
        try{

            Bank bank = repository.findBySortCode(sort_code);
            if(bank != null){
                BankRequest record = this.mapper.map(bank, BankRequest.class);
                return CompletableFuture.completedFuture(record);
            }
            return null;
        } catch(Exception ex){
            logger.info("An error occurred in method 'findBankBySortCode'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<List<BankRequest>> getBanks() {
        logger.info("Retrieving all affiliations");
        List<BankRequest> records  = new ArrayList<>();
        try{
            List<Bank> banks = repository.findAll();
            if(!banks.isEmpty()){
                for (Bank record : banks) {
                    records.add(this.mapper.map(record, BankRequest.class));
                }
            }

            return CompletableFuture.completedFuture(records);
        } catch(Exception ex){
            logger.info("An error occurred in method 'getBanks'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<List<BankRequest>> getActiveBanks() {
        logger.info("Retrieving all active affiliations");
        List<BankRequest> activeRecords  = new ArrayList<>();
        try{
            List<Bank> banks = repository.findActiveBanks();
            if(!banks.isEmpty()){
                for (Bank record : banks) {
                    activeRecords.add(this.mapper.map(record, BankRequest.class));
                }
            }

            return CompletableFuture.completedFuture(activeRecords);
        } catch(Exception ex){
            logger.info("An error occurred in method 'getActiveBanks'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<BankRequest> createBank(BankRequest bank) throws InterruptedException {
        logger.info("Create new bank");
        try{
            //map record
            Bank record = this.mapper.map(bank, Bank.class);

            //save record
            repository.save(record);

            //set identification type id
            bank.setId(record.getId());
            return CompletableFuture.completedFuture(bank);
        } catch(Exception ex){
            logger.info("An error occurred in method 'createBank'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public void updateBank(BankRequest bank) {
        logger.info("update bank record");
        try{
            repository.updateBank(this.mapper.map(bank, Bank.class));
        } catch(Exception ex){
            logger.info("An error occurred in method 'updateBank'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public void softDeleteBank(long id, boolean is_deleted) {
        logger.info("Soft delete bank");
        try{
            logger.info(String.format("Bank property 'is_deleted' value is set to %s", is_deleted ? "true": "false"));
            repository.markAsDeleted(id, is_deleted);
        } catch(Exception ex){
            logger.info("An error occurred in method 'softDeleteBank'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public void deleteBank(long id) {
        logger.info("Delete bank record");
        try{
            logger.info(String.format("Bank with id %s is being deleted", id));
            repository.deleteById(id);
        } catch(Exception ex){
            logger.info("An error occurred in method 'deleteBank'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }
}
