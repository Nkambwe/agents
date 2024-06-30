package com.pbu.wendi.services.agents.services;

import com.pbu.wendi.model.agents.models.IndividualAgent;
import com.pbu.wendi.model.agents.models.Partner;
import com.pbu.wendi.repositories.agents.repos.PartnerRepository;
import com.pbu.wendi.requests.agents.dto.IndividualRequest;
import com.pbu.wendi.requests.agents.dto.PartnerRequest;
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
public class PartnerServiceImp implements PartnerService {
    private final AppLoggerService logger;
    private final ModelMapper mapper;
    private final PartnerRepository partners;

    public PartnerServiceImp(AppLoggerService logger, ModelMapper mapper, PartnerRepository partners) {
        this.logger = logger;
        this.mapper = mapper;
        this.partners = partners;
    }

    @Override
    public CompletableFuture<Boolean> partnerExists(long id) {
        logger.info("Check whether partner exists");
        try{
            return CompletableFuture.completedFuture(partners.existsById(id));
        } catch(Exception ex){
            logger.info("An error occurred in method 'partnerExists' in 'PartnerService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<Boolean> existsByFullNameAndAgentId(String fullName, Long agentId){
        logger.info(String.format("Retrieving Partner with name %s and agent Id %s", fullName, agentId));
        try{
            return CompletableFuture.completedFuture(partners.existsByFullNameAndAgentId(fullName, agentId));
        } catch(Exception ex){
            logger.info("An error occurred in method 'existsByFullNameAndAgentId' in 'PartnerService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<Boolean> existsByIdAndFullNameAndAgentId(Long id, String fullName, Long agentId){
        logger.info(String.format("Retrieving Partner with name '%s' and agent Id '%s' and partner ID '%s'", fullName, agentId, id));
        try{
            return CompletableFuture.completedFuture(partners.existsByIdAndFullNameAndAgentId(id, fullName, agentId));
        } catch(Exception ex){
            logger.info("An error occurred in method 'existsByIdAndFullNameAndAgentId' in 'PartnerService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<Boolean> existsByFullNameAndAgentIdNotId(String fullName, Long agentId, Long id) {
        logger.info(String.format("Retrieving partner with name %s and agent Id %s and not ID %s", fullName, agentId, id));
        try{
            return CompletableFuture.completedFuture(partners.existsByFullNameAndAgentIdAndIdNot(fullName, agentId,id));
        } catch(Exception ex){
            logger.info("An error occurred in method 'existsByFullNameAndAgent_IdNotId' in 'PartnerService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<PartnerRequest> findPartnerById(long id) {
        logger.info(String.format("Retrieving partner with ID %s", id));
        try{

            Optional<Partner> record = partners.findById(id);
            if (record.isPresent()) {
                PartnerRequest request = this.mapper.map(record.get(), PartnerRequest.class);
                return CompletableFuture.completedFuture(request);
            }

            return CompletableFuture.completedFuture(null);
        } catch(Exception ex){
            logger.info("An error occurred in method 'findPartnerById' in 'PartnerService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<PartnerRequest> findPartnerByFullName(String name, long agentId) {
        logger.info(String.format("Retrieving partner with name %s and agent ID %s", name, agentId));
        try{

            Optional<Partner> record = partners.findByFullNameAndAgent_Id(name, agentId);
            if (record.isPresent()) {
                PartnerRequest request = this.mapper.map(record.get(), PartnerRequest.class);
                return CompletableFuture.completedFuture(request);
            }

            return CompletableFuture.completedFuture(null);
        } catch(Exception ex){
            logger.info("An error occurred in method 'findPartnerByFullName' in 'PartnerService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<List<PartnerRequest>> getPartners(long agentId) {
        logger.info("Retrieve individual agent partners");
        List<PartnerRequest> records  = new ArrayList<>();
        try{
            //..get partner's records
            List<Partner> p_records = partners.findByAgent_Id(agentId);
            if(!p_records.isEmpty()){
                for (Partner record : p_records) {
                    records.add(this.mapper.map(record, PartnerRequest.class));
                }
            }

            return CompletableFuture.completedFuture(records);
        } catch(Exception ex){
            logger.info("An error occurred in method 'getPartners' in 'PartnerService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<PartnerRequest> create(PartnerRequest partner, IndividualRequest agent) throws InterruptedException {
        logger.info("Adding new agent partner");
        try{
            Partner record = this.mapper.map(partner, Partner.class);

            IndividualAgent agentRec = mapper.map(agent, IndividualAgent.class);
            record.setAgent(agentRec);
            partners.save(record);

            //return record
            record.setId(record.getId());
            return CompletableFuture.completedFuture(partner);
        }catch(Exception ex){
            logger.info("An error occurred in method 'create' in 'PartnerService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public void update(PartnerRequest partner, IndividualRequest agent) {
        logger.info("update agent partner");
        try {
            Partner record = this.mapper.map(partner, Partner.class);
            record.setId(partner.getId());

            //..agent record
            IndividualAgent agentRec = mapper.map(agent, IndividualAgent.class);
            record.setAgent(agentRec);
            partners.update(record);
        } catch(Exception ex){
            logger.info("An error occurred in method 'update' in PartnerService");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public void softDelete(long id, boolean deleted) {
        logger.info("Soft Delete partner");
        try{
            logger.info(String.format("Partner isDeleted value set to %s", deleted ? "true": "false"));
            partners.markAsDeleted(id, deleted);
        } catch(Exception ex){
            logger.info("An error occurred in method 'softDelete' in 'PartnerService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public void delete(long id) {
        logger.info("Delete Partner");
        try{
            logger.info(String.format("Delete Partner with id %s", id));
            partners.deleteById(id);
        } catch(Exception ex){
            logger.info("An error occurred in method 'delete' in 'PartnerService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }
}
