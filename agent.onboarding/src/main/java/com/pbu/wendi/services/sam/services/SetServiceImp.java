package com.pbu.wendi.services.sam.services;

import com.pbu.wendi.model.sam.models.Permission;
import com.pbu.wendi.model.sam.models.PermissionSet;
import com.pbu.wendi.repositories.sam.repos.PermissionRepository;
import com.pbu.wendi.repositories.sam.repos.SetRepository;
import com.pbu.wendi.requests.sam.dto.PermissionRequest;
import com.pbu.wendi.requests.sam.dto.SetRequest;
import com.pbu.wendi.utils.common.AppLoggerService;
import com.pbu.wendi.utils.exceptions.GeneralException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class SetServiceImp implements SetService {

    private final ModelMapper mapper;
    private final AppLoggerService logger;
    private final SetRepository sets;
    private final PermissionRepository permissions;

    public SetServiceImp(AppLoggerService logger, ModelMapper mapper, SetRepository sets, PermissionRepository permissions) {
        this.mapper = mapper;
        this.sets = sets;
        this.logger = logger;
        this.permissions = permissions;
    }

    @Override
    public boolean nameInUse(String name) {
        logger.info(String.format("Checking whether permission set with name '%s' exists",name));
        try{
            return sets.existsBySetName(name);
        } catch(Exception ex){
            logger.info("An error occurred in method 'nameInUse'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public boolean checkNameDuplication(String name, long id) {
        logger.info(String.format("Checking whether permission set name '%s' is assigned to another ID other than '%s' ",name, id));
        try{
            return sets.existsBySetNameAndIdNot(name, id);
        } catch(Exception ex){
            logger.info("An error occurred in method 'checkNameDuplication'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public boolean descriptionInUse(String description) {
        logger.info(String.format("Checking whether permission set description '%s' exists", description));
        try{
            return sets.existsByDescription(description);
        } catch(Exception ex){
            logger.info("An error occurred in method 'descriptionInUse'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public boolean checkDescriptionDuplication(String description, long id) {
        logger.info(String.format("Checking whether permission set description '%s' is assigned to another ID other than '%s'", description, id));
        try{
            return sets.existsByDescriptionAndIdNot(description, id);
        } catch(Exception ex){
            logger.info("An error occurred in method 'checkDescriptionDuplication'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<List<SetRequest>> findAll(){
        logger.info("Retrieving all permission sets");
        List<SetRequest> records  = new ArrayList<>();
        try{
            List<PermissionSet> recordsList = sets.findAll();
            if(!recordsList.isEmpty()){
                for (PermissionSet record : recordsList) {
                    records.add(this.mapper.map(record, SetRequest.class));
                }
            }

            return CompletableFuture.completedFuture(records);
        } catch(Exception ex){
            logger.info("An error occurred in method 'findAll' in 'SetService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<SetRequest> findById(long id) {
        logger.info(String.format("Retrieving Permission set with ID %s", id));
        try{

            PermissionSet set = sets.findById(id);
            if(set != null){
                SetRequest record = this.mapper.map(set, SetRequest.class);
                return CompletableFuture.completedFuture(record);
            }
            return null;
        } catch(Exception ex){
            logger.info("An error occurred in method 'findById' in 'SetService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<SetRequest> findBySetName(String name) {
        logger.info(String.format("Retrieving permission set with set name %s", name));
        try{

            PermissionSet set = sets.findBySetName(name);
            if(set != null){
                SetRequest record = this.mapper.map(set, SetRequest.class);
                return CompletableFuture.completedFuture(record);
            }
            return null;
        } catch(Exception ex){
            logger.info("An error occurred in method 'findBySetName' in 'SetService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public void softDelete(long id, boolean deleted) {
        logger.info("Soft delete permission set");
        try{
            logger.info(String.format("Permission set property 'isDeleted' value is set to %s", deleted ? "true": "false"));
            sets.markAsDeleted(id, deleted);
        } catch(Exception ex){
            logger.info("An error occurred in method 'softDelete' in 'SetService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public void lockSet(long id, boolean isLocked) {
        logger.info("Soft delete permission set");
        try{
            logger.info(String.format("Permission set property 'isLocked' value is set to %s", isLocked ? "true": "false"));
            sets.lockSet(id, isLocked);
        } catch(Exception ex){
            logger.info("An error occurred in method 'lockSet' in 'SetService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<SetRequest> create(SetRequest permission) {
        logger.info("Create new permission set");
        try{
            //map record
            PermissionSet record = this.mapper.map(permission, PermissionSet.class);
            if (permission.getPermissions() != null && !permission.getPermissions().isEmpty()) {
                List<Long> permissionIds = permission.getPermissions().stream()
                        .map(PermissionRequest::getId)
                        .collect(Collectors.toList());

                List<Permission> fetchedPermissions = permissions.findAllById(permissionIds);
                record.setPermissions(new ArrayList<>(fetchedPermissions));
            }

            //save record
            sets.save(record);

            //set permission set id
            permission.setId(record.getId());
            return CompletableFuture.completedFuture(permission);
        } catch(Exception ex){
            logger.info("An error occurred in method 'create' in 'SetService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public void delete(long id) {
        logger.info("Delete permission set");
        try{
            logger.info(String.format("Delete permission set with id %s", id));
            sets.deleteById(id);
        } catch(Exception ex){
            logger.info("An error occurred in method 'delete' in 'SetService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<SetRequest> updateSetPermissions(SetRequest permission) {
        logger.info("Update permission set permissions");
        try {
            // Retrieve the existing PermissionSet record
            PermissionSet record = sets.findById(permission.getId());
            if(record == null){
                return null;
            }

            // Clear current permissions
            record.getPermissions().clear();

            // Fetch new permissions and set them to the record
            if (permission.getPermissions() != null && !permission.getPermissions().isEmpty()) {
                logger.info("Adding permission set permissions");
                List<Long> permissionIds = permission.getPermissions().stream()
                        .map(PermissionRequest::getId)
                        .collect(Collectors.toList());

                List<Permission> fetchedPermissions = permissions.findAllById(permissionIds);
                record.setPermissions(new ArrayList<>(fetchedPermissions));
            }

            // Update other fields if necessary
            this.mapper.map(permission, record);

            // Save updated record
            sets.save(record);

            // Set permission set id
            permission.setId(record.getId());
            return CompletableFuture.completedFuture(permission);
        } catch (Exception ex) {
            logger.info("An error occurred in method 'update' in 'SetService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }

    }

    @Override
    public CompletableFuture<SetRequest> update(SetRequest permission) {
        logger.info("update permission set record");
        try{
            sets.updateSet(this.mapper.map(permission, PermissionSet.class));
            return CompletableFuture.completedFuture(permission);
        } catch(Exception ex){
            logger.info("An error occurred in method 'update' in 'SetService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }
}

