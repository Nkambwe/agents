package com.pbu.wendi.services.sam.services;

import com.pbu.wendi.model.sam.models.ArchiveUser;
import com.pbu.wendi.repositories.sam.repos.ArchiveUserRepository;
import com.pbu.wendi.requests.sam.dto.ArchiveUserRequest;
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
public class ArchivedUserServiceImp implements ArchivedUserService {
    private final AppLoggerService logger;
    private final ModelMapper mapper;
    private final ArchiveUserRepository archives;

    public ArchivedUserServiceImp(AppLoggerService logger, ModelMapper mapper, ArchiveUserRepository archives) {
        this.mapper = mapper;
        this.archives = archives;
        this.logger = logger;
    }

    @Override
    public CompletableFuture<List<ArchiveUserRequest>> findAll() {
        logger.info("Retrieving all archived users");
        List<ArchiveUserRequest> records  = new ArrayList<>();
        try{
            List<ArchiveUser> archiveRecords = archives.findAll();
            if(!archiveRecords.isEmpty()){
                for (ArchiveUser record : archiveRecords) {
                    records.add(this.mapper.map(record, ArchiveUserRequest.class));
                }
            }

            return CompletableFuture.completedFuture(records);
        } catch(Exception ex){
            logger.info("An error occurred in method 'findAll' in 'ArchivedUserService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<ArchiveUserRequest> findById(long id) {
        logger.info(String.format("Retrieving archived user record with ID %s", id));
        try {
            Optional<ArchiveUser> uArchive = archives.findById(id);
            if(uArchive.isPresent()){
                ArchiveUserRequest record = this.mapper.map(uArchive.get(), ArchiveUserRequest.class);
                return CompletableFuture.completedFuture(record);
            }

            return CompletableFuture.completedFuture(null);
        } catch(Exception ex){
            logger.info("An error occurred in method 'findById' in 'ArchivedUserService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<ArchiveUserRequest> findByPfNo(String pfNo) {
        logger.info(String.format("Retrieving archived user record with PF NO. %s", pfNo));
        try {
            Optional<ArchiveUser> uArchive = archives.findByPfNo(pfNo);
            if(uArchive.isPresent()){
                ArchiveUserRequest record = this.mapper.map(uArchive.get(), ArchiveUserRequest.class);
                return CompletableFuture.completedFuture(record);
            }
            return CompletableFuture.completedFuture(null);
        } catch(Exception ex){
            logger.info("An error occurred in method 'findByPfNo' in 'ArchivedUserService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<ArchiveUserRequest> create(ArchiveUserRequest log) throws InterruptedException {
        logger.info("Create new archived user record");
        try{
            //map record
            ArchiveUser record = this.mapper.map(log, ArchiveUser.class);
            //save record
            archives.save(record);
            //set identification type id
            log.setId(record.getId());
            return CompletableFuture.completedFuture(log);
        } catch(Exception ex){
            logger.info("An error occurred in method 'create' in 'ArchivedUserService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public void delete(long id) {
        logger.info("Delete archived user record");
        try{
            logger.info(String.format("Archived user with id %s is being deleted", id));
            archives.deleteById(id);
        } catch(Exception ex){
            logger.info("An error occurred in method 'delete' in 'ArchivedUserService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }
}

