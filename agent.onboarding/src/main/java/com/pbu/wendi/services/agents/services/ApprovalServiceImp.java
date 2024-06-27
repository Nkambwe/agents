package com.pbu.wendi.services.agents.services;

import com.pbu.wendi.model.agents.models.Approval;
import com.pbu.wendi.model.agents.models.BusinessAgent;
import com.pbu.wendi.model.agents.models.IndividualAgent;
import com.pbu.wendi.repositories.agents.repos.ApprovalRepository;
import com.pbu.wendi.repositories.agents.repos.BusinessRepository;
import com.pbu.wendi.repositories.agents.repos.IndividualRepository;
import com.pbu.wendi.requests.agents.dto.ApprovalRequest;
import com.pbu.wendi.utils.common.AppLoggerService;
import com.pbu.wendi.utils.enums.AgentType;
import com.pbu.wendi.utils.enums.Status;
import com.pbu.wendi.utils.exceptions.GeneralException;
import com.pbu.wendi.utils.exceptions.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class ApprovalServiceImp implements ApprovalService {

    private final AppLoggerService logger;
    private final ModelMapper mapper;
    private final ApprovalRepository approvals;
    private final IndividualRepository singleAgents;
    private final BusinessRepository businessAgents;

    public ApprovalServiceImp(AppLoggerService logger, ModelMapper mapper,
                              ApprovalRepository approvals,
                              IndividualRepository singleAgents,
                              BusinessRepository businessAgents) {
        this.logger = logger;
        this.mapper = mapper;
        this.approvals = approvals;
        this.singleAgents = singleAgents;
        this.businessAgents = businessAgents;
    }

    @Override
    public CompletableFuture<Boolean> existsById(long id) {
        logger.info("Check whether approval exists");
        try{
            return CompletableFuture.completedFuture(approvals.existsById(id));
        } catch(Exception ex){
            logger.info("An error occurred in method 'existsById' in 'ApprovalService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<Boolean> existsByPersonId(long personId) {
        logger.info("Check whether approval exists");
        try{
            return CompletableFuture.completedFuture(approvals.existsByPersonId(personId));
        } catch(Exception ex){
            logger.info("An error occurred in method 'existsByPersonId' in 'ApprovalService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<Boolean> existsByBusinessId(long businessId) {
        logger.info("Check whether approval exists");
        try{
            return CompletableFuture.completedFuture(approvals.existsByBusinessId(businessId));
        } catch(Exception ex){
            logger.info("An error occurred in method 'existsByBusinessId' in 'ApprovalService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<ApprovalRequest> findApprovalById(long id) {
        logger.info(String.format("Retrieving approval with ID %s", id));
        try{

            Optional<Approval> record = approvals.findById(id);
            if (record.isPresent()) {
                ApprovalRequest request = this.mapper.map(record.get(), ApprovalRequest.class);
                return CompletableFuture.completedFuture(request);
            }

            return CompletableFuture.completedFuture(null);
        } catch(Exception ex){
            logger.info("An error occurred in method 'findApprovalById' in 'ApprovalService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<List<ApprovalRequest>> getApprovals(long agentId, AgentType type) {
        logger.info(String.format("Retrieving %s approvals for agent with ID %s", type.toString(),agentId));
        try{
            List<Approval> records;
            if(type == AgentType.BUSINESS){
                records = approvals.findAllByBusinessId(agentId);
            } else {
                records = approvals.findAllByPersonId(agentId);
            }

            List<ApprovalRequest> results;
            if (!records.isEmpty()) {
                results = new ArrayList<>();
                for (Approval a:records) {
                    ApprovalRequest request = this.mapper.map(a, ApprovalRequest.class);
                    results.add(request);
                }

                return CompletableFuture.completedFuture(results);
            }

            return CompletableFuture.completedFuture(null);
        } catch(Exception ex){
            logger.info("An error occurred in method 'getApprovals' in 'ApprovalService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<ApprovalRequest> create(ApprovalRequest approval, AgentType type) throws InterruptedException {
        logger.info("Adding new agent approval");
        try{
            Approval record = this.mapper.map(approval, Approval.class);
            long agentId = approval.getAgentId();

            if(type == AgentType.BUSINESS){
                Optional<BusinessAgent> bAgent = this.businessAgents.findById(agentId);
                if(bAgent.isPresent()){
                    record.setBusiness(bAgent.get());
                } else {
                    throw new NotFoundException("BusinessAgent", "RecordId", agentId);
                }

            } else{
                Optional<IndividualAgent> aAgent = this.singleAgents.findById(agentId);
                if(aAgent.isPresent()){
                    record.setPerson(aAgent.get());
                } else {
                    throw new NotFoundException("IndividualAgent", "RecordId", agentId);
                }
            }

            approvals.save(record);
            approval.setId(record.getId());
            return CompletableFuture.completedFuture(approval);
        }catch(Exception ex){
            logger.info("An error occurred in method 'create' in 'ApprovalService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }


    @Override
    public void update(ApprovalRequest approval) {
        logger.info(String.format("update approval record with ID %s", approval.getId()));
        try{
            Approval record = this.mapper.map(approval, Approval.class);
            record.setCreatedStatus(Status.convertToEnum(approval.getCreatedStatus()));
            record.setRecruitStatus(Status.convertToEnum(approval.getRecruitStatus()));
            record.setReviewStatus(Status.convertToEnum(approval.getReviewStatus()));
            record.setApproveStatus(Status.convertToEnum(approval.getApproveStatus()));
            record.setId(approval.getId());

            //..update record
            approvals.updateApproval(record);

        } catch(Exception ex) {
            logger.info("An error occurred in method 'update' in ApprovalService");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public void softDelete(long id, boolean deleted) {
        try{
            logger.info(String.format("Approval isDeleted value set to %s", deleted ? "true": "false"));
            approvals.softDelete(id, deleted);
        } catch(Exception ex){
            logger.info("An error occurred in method 'softDelete' in 'ApprovalService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public void delete(long id) {
        logger.info(String.format("Delete Approval with ID %s ", id));
        try{
            approvals.deleteById(id);
        } catch(Exception ex){
            logger.info("An error occurred in method 'delete' in 'ApprovalService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }
}
