package com.pbu.wendi.services.sam.services;

import com.pbu.wendi.model.sam.models.Permission;
import com.pbu.wendi.repositories.sam.repos.PermissionRepository;
import com.pbu.wendi.requests.sam.dto.PermissionRequest;
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
public class PermissionServiceImp implements PermissionService {
    private final ModelMapper mapper;
    private final PermissionRepository permissions;
    private final AppLoggerService logger;

    public PermissionServiceImp(AppLoggerService logger, ModelMapper mapper, PermissionRepository permissions) {
        this.mapper = mapper;
        this.permissions = permissions;
        this.logger = logger;
    }

    @Override
    public boolean nameInUse(String name) {
        logger.info(String.format("Checking whether permission with name '%s' exists",name));
        try{
            return permissions.existsByName(name);
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
        logger.info(String.format("Checking whether permission name '%s' is assigned to another ID other than '%s' ",name, id));
        try{
            return permissions.existsByName(name);
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
        logger.info(String.format("Checking whether permission description '%s' exists", description));
        try{
            return permissions.existsByDescription(description);
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
        logger.info(String.format("Checking whether permission description '%s' is assigned to another ID other than '%s'", description, id));
        try{
            return permissions.existsByDescriptionAndIdNot(description, id);
        } catch(Exception ex){
            logger.info("An error occurred in method 'checkDescriptionDuplication'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<PermissionRequest> findById(long id) {
        logger.info(String.format("Retrieving a permission by ID %s", id));
        try{

            Optional<Permission> record = permissions.findById(id);
            if(record.isPresent()){
                PermissionRequest request = this.mapper.map(record, PermissionRequest.class);
                return CompletableFuture.completedFuture(request);
            }
            return null;
        } catch(Exception ex){
            logger.info("An error occurred in method 'findById'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<List<PermissionRequest>> findPermissions() {
        logger.info("Retrieving all permissions%s");
        List<PermissionRequest> records  = new ArrayList<>();
        try{
            List<Permission> recordsList = permissions.findAll();
            if(!recordsList.isEmpty()){
                for (Permission record : recordsList) {
                    records.add(this.mapper.map(record, PermissionRequest.class));
                }
            }

            return CompletableFuture.completedFuture(records);
        } catch(Exception ex){
            logger.info("An error occurred in method 'findPermissions' in 'PermissionService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<List<PermissionRequest>> findPermissions(long setId) {
        logger.info(String.format("Retrieving all permissions for PermissionSet with ID %s", setId));
        List<PermissionRequest> records  = new ArrayList<>();
        try{
            List<Permission> recordsList = permissions.findAllByPermissionSetId(setId);
            if(!recordsList.isEmpty()){
                for (Permission record : recordsList) {
                    records.add(this.mapper.map(record, PermissionRequest.class));
                }
            }

            return CompletableFuture.completedFuture(records);
        } catch(Exception ex){
            logger.info("An error occurred in method 'findPermissions' in 'PermissionService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<List<PermissionRequest>> findPermissionsByRoleId(Long roleId){
        logger.info(String.format("Retrieving all permissions for Role with ID %s", roleId));
        List<PermissionRequest> records  = new ArrayList<>();
        try{
            List<Permission> recordsList = permissions.findPermissionsByRoleId(roleId);
            if(!recordsList.isEmpty()){
                for (Permission record : recordsList) {
                    records.add(this.mapper.map(record, PermissionRequest.class));
                }
            }

            return CompletableFuture.completedFuture(records);
        } catch(Exception ex){
            logger.info("An error occurred in method 'findPermissionsByRoleId' in 'PermissionService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public void lockSetPermissions(long setId, boolean isLocked) {
        logger.info("Lock all permissions in permission set");
        try{
            List<Permission> records = permissions.findAllByPermissionSetId(setId);
            if(!records.isEmpty()){
                for (Permission p : records){
                    permissions.lockPermission(p.getId(), isLocked);
                }
            }
        } catch(Exception ex){
            logger.info("An error occurred in method 'lockSetPermissions' in 'PermissionService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public void update(PermissionRequest permission) {
        logger.info("update permission record");
        try{
            permissions.update(this.mapper.map(permission, Permission.class));
        } catch(Exception ex){
            logger.info("An error occurred in method 'update' in 'PermissionService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }
}
