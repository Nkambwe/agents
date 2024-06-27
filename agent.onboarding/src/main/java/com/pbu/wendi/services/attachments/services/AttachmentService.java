package com.pbu.wendi.services.attachments.services;

import com.pbu.wendi.requests.attachments.dto.AttachmentRequest;
import com.pbu.wendi.requests.attachments.dto.IdentificationRequest;
import com.pbu.wendi.requests.attachments.dto.TypeRequest;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface AttachmentService {

    //region identification types

    /**
     * Find identification type with specified ID
     * @param id object ID to look for
     **/
    CompletableFuture<TypeRequest> findTypeById(long id);

    /**
     * Get all identification types
     **/
    CompletableFuture<List<TypeRequest>> getTypes();

    /**
     * Check if ID type with specified id exists
     * @param id Type ID to look for
     **/
    CompletableFuture<Boolean> idTypeExists(long id);

    /**
     * Check if ID type with specified name exists
     * @param name Type name to look for
     **/
    boolean checkTypeDuplicateName(String name);
    /**
     * Check if for ID Types with duplicate name with ID other than that provided
     * @param name Type name to look for
     * @param id Type ID to compare to
     **/
    boolean checkTypeDuplicateNameWithDifferentIds(String name, long id);
    /**
     * Add a new identification type
     * @param type object to add
     **/
    CompletableFuture<TypeRequest> createIdType(TypeRequest type) throws InterruptedException;

    /**
     * Update identification type
     * @param type object to update
     **/
    void updateIdType(TypeRequest type);

    /**
     * Soft delete Identification with specified ID
     * @param id for Identification to delete
     * @param is_deleted delete status
     * **/
    void softDeleteIdType(long id, boolean is_deleted);

    /**
     * Soft delete Identification with specified ID
     * @param id for Identification to delete
     * **/
    void deleteIdType(long id);
    //endregion

    //region identifications
    /**
     * Check whether identification with specified ID exists
     * @param id_number object ID number to look for
     **/
    CompletableFuture<Boolean> identificationExists(String id_number);
    /**
     * Check whether identification with specified ID exists
     * @param row_id object ID to look for
     **/
    CompletableFuture<Boolean>  identificationExistsById(long row_id);
    boolean checkIdNumberDuplicateWithDifferentIds(String doc_no, long row_id);
    /**
     * Find identification with specified ID
     * @param row_id object ID to look for
     **/
    CompletableFuture<IdentificationRequest>  findIdentificationById(long row_id);
    /**
     * Get all identifications for specified owner
     * @param owner_id object owner ID to look for
     **/
    CompletableFuture<List<IdentificationRequest>> getIdentifications(long owner_id);
    /**
     * Add a new identification
     * @param identification object to add
     **/
    CompletableFuture<IdentificationRequest> createIdentification(IdentificationRequest identification) throws InterruptedException;
    /**
     * Update identification
     * @param identification object to update
     **/
    void updateIdentification(IdentificationRequest identification);

    /**
     * Soft delete Identification with specified ID
     * @param id for Identification to delete
     * **/
    void deleteIdentification(long id);
    /**
     * Soft delete Identification with specified ID
     * @param row_id for Identification to delete
     * @param is_deleted delete status
     * **/
    void softDeleteIdentification(long row_id, boolean is_deleted);
    //endregion

    //region attachments

    /**
     * Add a list of new attachments
     * @param row_id list of attachments to add
     **/
    CompletableFuture<Boolean> attachmentExists(long row_id);
    /**
     * Check whether another attachment exists with the same name
     * @param owner_id object ID to look for
     **/
    boolean  checkAttachmentDuplicateNameWithSameIds(String name, long owner_id);

    /**
     * Get an attachment with specified ID
     * @param row_id of attachments to look for
     **/
    CompletableFuture<AttachmentRequest>  findAttachmentById(long row_id);

    /**
     * Get all attachments for owner with owner ID specified
     * @param owner_id Owner ID to look for
     **/
    CompletableFuture<List<AttachmentRequest>> getAttachments(long owner_id);

    /**
     * Add a list of new attachments
     * @param attachments list of attachments to add
     **/
    CompletableFuture<AttachmentRequest[]> createAttachments(AttachmentRequest[] attachments) throws InterruptedException;
    /**
     * Update attachment
     * @param attachment object to update
     **/
    void updateAttachment(AttachmentRequest attachment);
    /**
     * Soft delete attachment with specified ID
     * @param id for attachment to delete
     * **/
    void deleteAttachment(long id);

    /**
     * Soft delete attachment with specified ID
     * @param id for attachment to delete
     * @param is_deleted delete status
     * **/
    void softDeleteAttachment(long id, boolean is_deleted);
    //endregion
}

