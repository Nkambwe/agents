package com.pbu.wendi.services.sam.services;

import com.pbu.wendi.model.sam.models.PermissionSet;
import com.pbu.wendi.model.sam.models.Role;
import com.pbu.wendi.repositories.sam.repos.RoleRepository;
import com.pbu.wendi.repositories.sam.repos.SetRepository;
import com.pbu.wendi.utils.requests.sam.dto.RoleRequest;
import com.pbu.wendi.utils.requests.sam.dto.SetRequest;
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
public class RoleServiceImp implements RoleService {
    private final AppLoggerService logger;
    private final ModelMapper mapper;
    private final RoleRepository roles;
    private final SetRepository sets;

    public RoleServiceImp(AppLoggerService logger,
                          ModelMapper mapper,
                          RoleRepository roles,
                          SetRepository sets) {
        this.mapper = mapper;
        this.logger = logger;
        this.roles = roles;
        this.sets = sets;
    }

    @Override
    public boolean exists(long roleId){
        logger.info(String.format("Checking whether roles with roleId '%s' exists",roleId));
        try{
            return roles.checkRole(roleId);
        } catch(Exception ex){
            logger.info("An error occurred in method 'existsById'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }
    @Override
    public boolean nameInUse(String name) {
        logger.info(String.format("Checking whether roles with name '%s' exists",name));
        try{
            return roles.existsByName(name);
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
        logger.info(String.format("Checking whether roles name '%s' is assigned to another ID other than '%s' ",name, id));
        try{
            return roles.existsByNameAndIdNot(name, id);
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
        logger.info(String.format("Checking whether role description '%s' exists", description));
        try{
            return roles.existsByDescription(description);
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
        logger.info(String.format("Checking whether role description '%s' is assigned to another ID other than '%s'", description, id));
        try{
            return roles.existsByDescriptionAndIdNot(description, id);
        } catch(Exception ex){
            logger.info("An error occurred in method 'checkDescriptionDuplication'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<RoleRequest> findById(long id) {
        logger.info(String.format("Retrieving Role with ID %s", id));
        try{

            Role role = roles.findById(id);
            if(role != null){
                RoleRequest record = this.mapper.map(role, RoleRequest.class);
                return CompletableFuture.completedFuture(record);
            }
            return null;
        } catch(Exception ex){
            logger.info("An error occurred in method 'findById' in 'RoleService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<List<RoleRequest>> findRolesAll() {
        List<RoleRequest> records  = new ArrayList<>();
        try{
            //..get role records
            List<Role> roleRecords = roles.findAll();
            if(!roleRecords.isEmpty()){
                for (Role record : roleRecords) {
                    records.add(this.mapper.map(record, RoleRequest.class));
                }
            }
            return CompletableFuture.completedFuture(records);
        } catch(Exception ex){
            logger.info("An error occurred in method 'findRolesAll'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<RoleRequest> create(RoleRequest role) {
        logger.info("Create new role");
        try{
            //map record
            Role record = this.mapper.map(role, Role.class);
            if (role.getPermissions() != null && !role.getPermissions().isEmpty()) {
                List<Long> permissionIds = role.getPermissions().stream()
                        .map(SetRequest::getId)
                        .collect(Collectors.toList());

                List<PermissionSet> fetchedPermissions = sets.findAllById(permissionIds);
                record.setPermissions(new ArrayList<>(fetchedPermissions));
            }

            //save record
            roles.save(record);

            //set role id
            role.setId(record.getId());
            return CompletableFuture.completedFuture(role);
        } catch(Exception ex){
            logger.info("An error occurred in method 'create' in 'RoleService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<RoleRequest>  update(RoleRequest role) {
        logger.info("update role record");
        try{
            roles.update(this.mapper.map(role, Role.class));
            return CompletableFuture.completedFuture(role);
        } catch(Exception ex){
            logger.info("An error occurred in method 'update' in 'RoleService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<RoleRequest> updateRolePermissions(RoleRequest role) {
        logger.info("Update permission set permissions");
        try {
            // Retrieve the existing role record
            Role record = roles.findById(role.getId());
            if(record == null){
                return null;
            }

            // Clear current permissions
            record.getPermissions().clear();

            // Fetch new permissions and set them to the record
            if (record.getPermissions() != null && !record.getPermissions().isEmpty()) {
                List<Long> permissionIds = role.getPermissions().stream()
                        .map(SetRequest::getId)
                        .collect(Collectors.toList());

                List<PermissionSet> fetchedPermissions = sets.findAllById(permissionIds);
                record.setPermissions(new ArrayList<>(fetchedPermissions));
            }

            // Update other fields if necessary
            this.mapper.map(role, record);

            // Save updated record
            roles.save(record);

            // Set role id
            role.setId(record.getId());
            return CompletableFuture.completedFuture(role);
        } catch (Exception ex) {
            logger.info("An error occurred in method 'update' in 'SetService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }

    }

    @Override
    public void softDelete(long id, boolean isDeleted) {
        logger.info("Soft delete role");
        try{
            logger.info(String.format("Role property 'isDeleted' value is set to %s", isDeleted ? "true": "false"));
            roles.markAsDeleted(id, isDeleted);
        } catch(Exception ex){
            logger.info("An error occurred in method 'softDelete' in 'RoleService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public void delete(long id) {
        logger.info("Delete role record");
        try{
            logger.info(String.format("Role with id %s is being deleted", id));
            roles.deleteById(id);
        } catch(Exception ex){
            logger.info("An error occurred in method 'delete' in 'RoleService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }
}

