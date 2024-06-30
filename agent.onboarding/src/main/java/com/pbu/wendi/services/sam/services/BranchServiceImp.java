package com.pbu.wendi.services.sam.services;

import com.pbu.wendi.model.sam.models.Branch;
import com.pbu.wendi.repositories.sam.repos.BranchRepository;
import com.pbu.wendi.requests.sam.dto.BranchRequest;
import com.pbu.wendi.utils.common.AppLoggerService;
import com.pbu.wendi.utils.exceptions.GeneralException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class BranchServiceImp implements BranchService  {
    private final AppLoggerService logger;
    private final ModelMapper mapper;
    private final BranchRepository branches;

    public BranchServiceImp(AppLoggerService logger, ModelMapper mapper, BranchRepository branches) {
        this.mapper = mapper;
        this.branches = branches;
        this.logger = logger;
    }

    @Override
    public boolean checkIfExistsById(long id) {
        logger.info(String.format("Checking whether branch with id '%s' exists",id));
        try{
            return branches.existsById(id);
        } catch(Exception ex){
            logger.info("An error occurred in method 'checkIfExistsById' in 'BranchService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public boolean checkIfExistsBySolId(String solId) {
        logger.info(String.format("Checking whether branch with solID '%s' exists",solId));
        try{
            return branches.existsBySolId(solId);
        } catch(Exception ex){
            logger.info("An error occurred in method 'checkIfExistsById' in 'BranchService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public boolean checkSolIdDuplication(String solId, long branchId) {
        logger.info(String.format("Checking whether branch solID '%s' is duplicated",solId));
        try{
            return branches.existsBySolIdAndIdNot(solId, branchId);
        } catch(Exception ex){
            logger.info("An error occurred in method 'checkIfExistsById' in 'BranchService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public boolean checkIfExistsByName(String branchName) {
        logger.info(String.format("Checking whether with name '%s' exists",branchName));
        try{
            return branches.existsByBranchName(branchName);
        } catch(Exception ex){
            logger.info("An error occurred in method 'checkIfExistsByName' in 'BranchService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public boolean checkNameDuplication(String branchName, long branchId) {
        logger.info(String.format("Checking whether with name '%s' exists",branchName));
        try{
            return branches.existsByBranchNameAndIdNot(branchName, branchId);
        } catch(Exception ex){
            logger.info("An error occurred in method 'checkIfExistsByName' in 'BranchService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<BranchRequest> findById(long id) {
        logger.info(String.format("Find branch with id '%s' exists",id));
        try{
            Optional<Branch> branch = branches.findById(id);
            if(branch.isPresent()){
                BranchRequest record = this.mapper.map(branch.get(), BranchRequest.class);
                return CompletableFuture.completedFuture(record);
            }
            return CompletableFuture.completedFuture(null);
        } catch(Exception ex){
            logger.info("An error occurred in method 'findById' in 'BranchService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<BranchRequest> findBySolId(String solId) {
        logger.info(String.format("Find branch with solid '%s'",solId));
        try{
            Branch branch = branches.findBySolId(solId);
            if(branch != null){
                BranchRequest record = this.mapper.map(branch, BranchRequest.class);
                return CompletableFuture.completedFuture(record);
            }
            return CompletableFuture.completedFuture(null);
        } catch(Exception ex){
            logger.info("An error occurred in method 'findBySolId' in 'BranchService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<BranchRequest> findByName(String name) {
        logger.info(String.format("Find branch with name '%s'",name));
        try{
            Branch branch = branches.findByBranchName(name);
            if(branch != null){
                BranchRequest record = this.mapper.map(branch, BranchRequest.class);
                return CompletableFuture.completedFuture(record);
            }
            return CompletableFuture.completedFuture(null);
        } catch(Exception ex){
            logger.info("An error occurred in method 'findByName' in 'BranchService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<List<BranchRequest>> getAll() {
        logger.info("Retrieving all branches");
        List<BranchRequest> records  = new ArrayList<>();
        try{
            List<Branch> bRecords = branches.findAll();
            if(!bRecords.isEmpty()){
                for (Branch record : bRecords) {
                    records.add(this.mapper.map(record, BranchRequest.class));
                }
            }
            return CompletableFuture.completedFuture(records);
        } catch(Exception ex){
            logger.info("An error occurred in method 'getAll' in 'BranchService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public void activateBranch(long id, boolean status) {
        logger.info("Activate branch");
        try{
            logger.info(String.format("Branch property 'isActive' value is set to %s", status ? "true": "false"));
            branches.activateBranch(id, status);
        } catch(Exception ex){
            logger.info("An error occurred in method 'activateBranch'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<BranchRequest> create(BranchRequest branch) throws InterruptedException {
        try{
            Branch record = this.mapper.map(branch, Branch.class);
            branches.save(record);
            branch.setId(record.getId());
            return CompletableFuture.completedFuture(branch);
        } catch(Exception ex){
            logger.info("An error occurred in method 'create' in 'BranchService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public void updateBranch(BranchRequest branch) {
        logger.info("update branch record");
        try{
            branches.update(this.mapper.map(branch, Branch.class));
        } catch(Exception ex){
            logger.info("An error occurred in method 'updateAffiliation'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public void deleteBranch(long id) {
        logger.info("Delete branch record");
        try{
            logger.info(String.format("Branch with id %s is being deleted", id));
            branches.deleteById(id);
        } catch(Exception ex){
            logger.info("An error occurred in method 'deleteBranch'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }


    @Override
    public void softDelete(long id, boolean isDeleted) {
        logger.info("Soft delete branch");
        try{
            logger.info(String.format("Branch property 'isDeleted' value is set to %s", isDeleted ? "true": "false"));
            branches.markAsDeleted(id, isDeleted);
        } catch(Exception ex){
            logger.info("An error occurred in method 'softDelete'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }
}
