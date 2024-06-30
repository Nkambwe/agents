package com.pbu.wendi.services.attachments.services;

import com.pbu.wendi.model.attachments.models.Attachment;
import com.pbu.wendi.model.attachments.models.Identification;
import com.pbu.wendi.model.attachments.models.IdentificationType;
import com.pbu.wendi.repositories.attachments.repos.AttachmentRepository;
import com.pbu.wendi.repositories.attachments.repos.IdentificationRepository;
import com.pbu.wendi.repositories.attachments.repos.IdentificationTypeRepository;
import com.pbu.wendi.utils.requests.attachments.dto.AttachmentRequest;
import com.pbu.wendi.utils.requests.attachments.dto.IdentificationRequest;
import com.pbu.wendi.utils.requests.attachments.dto.TypeRequest;
import com.pbu.wendi.utils.common.AppLoggerService;
import com.pbu.wendi.utils.enums.AttachmentType;
import com.pbu.wendi.utils.exceptions.GeneralException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class AttachmentServiceImp implements AttachmentService {
    private final AppLoggerService logger;
    private final ModelMapper mapper;
    private final AttachmentRepository attachmentRepo;
    private final IdentificationRepository identificationRepo;
    private final IdentificationTypeRepository idTypeRepo;
    public AttachmentServiceImp(
            AppLoggerService logger,
            ModelMapper mapper,
            AttachmentRepository attachmentRepo,
            IdentificationRepository identificationRepo,
            IdentificationTypeRepository idTypeRepo){
        this.logger = logger;
        this.mapper = mapper;
        this.attachmentRepo = attachmentRepo;
        this.identificationRepo = identificationRepo;
        this.idTypeRepo = idTypeRepo;
    }

    //region Identification type
    @Transactional
    @Override
    public CompletableFuture<TypeRequest> findTypeById(long id){
        logger.info(String.format("Retrieving identification type with ID %s", id));
        try{

            IdentificationType type = idTypeRepo.findById(id);
            if(type != null){
                TypeRequest record = this.mapper.map(type, TypeRequest.class);
                return CompletableFuture.completedFuture(record);
            }
            return null;
        } catch(Exception ex){
            logger.info("An error occurred in method 'findTypeById'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Transactional
    @Override
    public CompletableFuture<List<TypeRequest>> getTypes(){
        logger.info("Retrieving all identifications types");
        List<TypeRequest> records  = new ArrayList<>();
        try{
            List<IdentificationType> types = idTypeRepo.findAll();
            if(!types.isEmpty()){
                for (IdentificationType record : types) {
                    records.add(this.mapper.map(record, TypeRequest.class));
                }
            }

            return CompletableFuture.completedFuture(records);
        } catch(Exception ex){
            logger.info("An error occurred in method 'getTypes'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Transactional
    @Override
    public CompletableFuture<Boolean> idTypeExists(long row_id){
        logger.info("Check if ID type exists");
        try{
            return CompletableFuture.completedFuture(idTypeRepo.existsById(row_id));
        } catch(Exception ex){
            logger.info("An error occurred in method 'idTypeExists'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }
    public boolean checkTypeDuplicateName(String name){
        logger.info("Check if ID type exists");
        try{
            return idTypeRepo.existsByTypeName(name);
        } catch(Exception ex){
            logger.info("An error occurred in method 'checkTypeDuplicateName'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    public boolean checkTypeDuplicateNameWithDifferentIds(String name, long id){
        logger.info("Check if ID type exists");
        try{
            return idTypeRepo.existsByTypeNameAndIdNot(name, id);
        } catch(Exception ex){
            logger.info("An error occurred in method 'checkTypeDuplicateNameWithDifferentIds'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Transactional
    @Override
    public CompletableFuture<TypeRequest> createIdType(TypeRequest type) {
        logger.info("Adding new identification type");
        try{
            //...check if id type name is taken
            //map record
            IdentificationType record = this.mapper.map(type, IdentificationType.class);

            //save record
            idTypeRepo.save(record);
            type.setId(record.getId());
            //set identification type id
            return CompletableFuture.completedFuture(type);
        }catch(Exception ex){
            logger.info("An error occurred in method 'createIdType'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    public void updateIdType(TypeRequest type){
        logger.info("update identification type");
        try{
            idTypeRepo.updateType(this.mapper.map(type, IdentificationType.class));
        } catch(Exception ex){
            logger.info("An error occurred in method 'updateIdType'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    public void softDeleteIdType(long id, boolean is_deleted){
        logger.info("Delete ID type");
        try{
            logger.info(String.format("ID Type is_deleted value set to %s", is_deleted ? "true": "false"));
            idTypeRepo.markAsDeleted(id, is_deleted);
        } catch(Exception ex){
            logger.info("An error occurred in method 'softDeleteIdType'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    public void deleteIdType(long id){
        logger.info("Delete ID type");
        try{
            logger.info(String.format("Delete ID Type with id %s", id));
            idTypeRepo.deleteById(id);
        } catch(Exception ex){
            logger.info("An error occurred in method 'deleteIdType'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    //endregion

    //region Identification

    @Transactional
    @Override
    public CompletableFuture<IdentificationRequest>  findIdentificationById(long row_id) {
        logger.info(String.format("Retrieving identification with ID %s", row_id));
        try{

            Identification identification = identificationRepo.findById(row_id);
            if(identification != null){
                IdentificationRequest record = this.mapper.map(identification, IdentificationRequest.class);
                return CompletableFuture.completedFuture(record);
            }
            return null;
        } catch(Exception ex){
            logger.info("An error occurred in method 'findIdentificationById'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Transactional
    @Override
    public CompletableFuture<List<IdentificationRequest>> getIdentifications(long owner_id) {
        logger.info(String.format("Retrieving all identifications for owner ID %s", owner_id));
        List<IdentificationRequest> records  = new ArrayList<>();
        try{
            List<Identification> identifications = identificationRepo.findAllByOwnerId(owner_id);
            if(!identifications.isEmpty()){
                for (Identification record : identifications) {
                    records.add(this.mapper.map(record, IdentificationRequest.class));
                }
            }

            return CompletableFuture.completedFuture(records);
        } catch(Exception ex){
            logger.info("An error occurred in method 'getIdentifications'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Transactional
    @Override
    public CompletableFuture<Boolean> identificationExists(String id_number){
        logger.info("Check if ID exists");
        try{
            return CompletableFuture.completedFuture(identificationRepo.existsByIdNo(id_number));
        } catch(Exception ex){
            logger.info("An error occurred in method 'identificationExists'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Transactional
    @Override
    public CompletableFuture<Boolean>  identificationExistsById(long row_id){
        logger.info("Check if Identification exists");
        try{
            return CompletableFuture.completedFuture(identificationRepo.existsById(row_id));
        } catch(Exception ex){
            logger.info("An error occurred in method 'identificationExistsById'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    public boolean checkIdNumberDuplicateWithDifferentIds(String id_no, long id){
        logger.info("Check if ID document exists");
        try{
            return identificationRepo.existsByIdNoAndIdNot(id_no, id);
        } catch(Exception ex){
            logger.info("An error occurred in method 'checkIdNumberDuplicateWithDifferentIds'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Transactional
    @Override
    public CompletableFuture<IdentificationRequest> createIdentification(IdentificationRequest identification) {
        logger.info("Adding new identification document");
        try{
            //map record
            Identification record = this.mapper.map(identification, Identification.class);

            //...assign document type
            record.setDocType(AttachmentType.convertToEnum(identification.getDocType()));

            //...save record
            identificationRepo.save(record);
            identification.setId(record.getId());
            identification.setOwnerId(record.getOwnerId());
            //set identification record id
            return CompletableFuture.completedFuture(identification);
        }catch(Exception ex){
            logger.info("An error occurred in method 'addIdentificationType'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    public void updateIdentification(IdentificationRequest identification){
        logger.info("update identification");
        try{
            identificationRepo.updateIdentification(this.mapper.map(identification, Identification.class));
        } catch(Exception ex){
            logger.info("An error occurred in method 'updateIdentification'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    public void softDeleteIdentification(long row_id, boolean is_deleted){
        logger.info("Soft Delete identification");
        try{
            logger.info(String.format("Identification with id %s is_deleted is set to %s", row_id, is_deleted ? "true": "false"));
            identificationRepo.markAsDeleted(row_id, is_deleted);
        } catch(Exception ex){
            logger.info("An error occurred in method 'softDeleteIdentification'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    public void deleteIdentification(long id){
        logger.info("Delete identification");
        try{
            logger.info(String.format("Identification with id %s is being deleted", id));
            identificationRepo.deleteById(id);
        } catch(Exception ex){
            logger.info("An error occurred in method 'deleteIdentification'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    //endregion

    //region Attachments
    @Transactional
    @Override
    public CompletableFuture<AttachmentRequest>  findAttachmentById(long row_id) {
        logger.info(String.format("Retrieving attachment with ID %s", row_id));
        try{

            Attachment attachment = attachmentRepo.findById(row_id);
            if(attachment != null){
                AttachmentRequest record = this.mapper.map(attachment, AttachmentRequest.class);
                return CompletableFuture.completedFuture(record);
            }
            return null;
        } catch(Exception ex){
            logger.info("An error occurred in method 'findByRowId'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Transactional
    @Override
    public CompletableFuture<List<AttachmentRequest>> getAttachments(long owner_id) {
        logger.info(String.format("Retrieving all attachments for owner ID %s", owner_id));
        List<AttachmentRequest> records  = new ArrayList<>();
        try{
            List<Attachment> attachments = attachmentRepo.findAllByOwnerId(owner_id);
            if(!attachments.isEmpty()){
                for (Attachment record : attachments) {
                    records.add(this.mapper.map(record, AttachmentRequest.class));
                }
            }

            return CompletableFuture.completedFuture(records);
        } catch(Exception ex){
            logger.info("An error occurred in method 'getAttachments'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Transactional
    @Override
    public CompletableFuture<Boolean>  attachmentExists(long row_id){
        logger.info("Check if attachment exists");
        try{
            return CompletableFuture.completedFuture(attachmentRepo.existsById(row_id));
        } catch(Exception ex){
            logger.info("An error occurred in method 'attachmentExists'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    public boolean checkAttachmentDuplicateNameWithSameIds(String name, long id){
        logger.info("Check if ID type exists");
        try{
            return attachmentRepo.existsAttachmentByAttDescrAndOwnerId(name, id);
        } catch(Exception ex){
            logger.info("An error occurred in method 'checkAttachmentDuplicateNameWithSameIds'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Transactional
    @Override
    public CompletableFuture<AttachmentRequest[]> createAttachments(AttachmentRequest[] attachments) {
        logger.info("Adding new file attachment");
        List<Attachment> results = new ArrayList<>();

        try{
            // Map each attachment request to an Attachment object
            for (AttachmentRequest record : attachments) {
                Attachment attachment = mapper.map(record, Attachment.class);
                //...assign document type
                attachment.setDocType(AttachmentType.convertToEnum(record.getDocType()));
                //...add attachment to the list
                results.add(attachment);
            }

            // Save all attachments in bulk
            attachmentRepo.saveAll(results);

            //set attachment ids
            AttachmentRequest[] records = new AttachmentRequest[attachments.length];
            for(int i =0; i<results.size() - 1;i++){
                Attachment a = results.get(i);
                records[i] = mapper.map(a,AttachmentRequest.class);
            }

            return CompletableFuture.completedFuture(records);
        }catch(Exception ex){
            logger.info("An error occurred in method 'createAttachments'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    public void updateAttachment(AttachmentRequest attachment){
        logger.info("update attachment");
        try{
            attachmentRepo.updateAttachment(this.mapper.map(attachment, Attachment.class));
        } catch(Exception ex){
            logger.info("An error occurred in method 'updateAttachment'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    public void softDeleteAttachment(long id, boolean is_deleted){
        logger.info("Delete attachment");
        try{
            logger.info(String.format("Attachment is_deleted value set to %s", is_deleted ? "true": "false"));
            attachmentRepo.markAsDeleted(id, is_deleted);
        } catch(Exception ex){
            logger.info("An error occurred in method 'softDeleteAttachment'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    public void deleteAttachment(long id){
        logger.info("Delete attachment");
        try{
            logger.info(String.format("Attachment with id %s is being deleted", id));
            attachmentRepo.deleteById(id);
        } catch(Exception ex){
            logger.info("An error occurred in method 'deleteAttachment'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }
    //endregion
}

