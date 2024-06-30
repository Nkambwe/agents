package com.pbu.wendi.services.agents.services;

import com.pbu.wendi.model.agents.models.IndividualAgent;
import com.pbu.wendi.model.agents.models.Kin;
import com.pbu.wendi.repositories.agents.repos.KinRepository;
import com.pbu.wendi.utils.requests.agents.dto.IndividualRequest;
import com.pbu.wendi.utils.requests.agents.dto.KinRequest;
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
public class KinServiceImp implements KinService {

    private final AppLoggerService logger;
    private final ModelMapper mapper;
    private final KinRepository kins;

    public KinServiceImp(AppLoggerService logger, ModelMapper mapper, KinRepository kins) {
        this.logger = logger;
        this.mapper = mapper;
        this.kins = kins;
    }

    @Override
    public CompletableFuture<Boolean> kinExists(long id) {
        logger.info("Check if Next Of Kin exists");
        try{
            return CompletableFuture.completedFuture(kins.existsById(id));
        } catch(Exception ex){
            logger.info("An error occurred in method 'kinExists' in 'KinService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<Boolean>  existsByFullNameAndAgentId(String fullName, Long agentId){
        logger.info(String.format("Retrieving Next Of Kin with name %s and agent Id %s", fullName, agentId));
        try{
            return CompletableFuture.completedFuture(kins.existsByFullNameAndAgentId(fullName, agentId));
        } catch(Exception ex){
            logger.info("An error occurred in method 'existsByFullNameAndAgent_IdNotId' in 'KinService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<Boolean>  existsByFullNameAndAgentIdNotId(String fullName, Long agentId, Long id){
        logger.info(String.format("Retrieving Next Of Kin with name %s and agent Id %s and ID %s", fullName, agentId, id));
        try{
            return CompletableFuture.completedFuture(kins.existsByIdAndFullNameAndAgentId(id, fullName, agentId));
        } catch(Exception ex){
            logger.info("An error occurred in method 'existsByFullNameAndAgent_IdNotId' in 'KinService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<KinRequest> findKinById(long id) {
        logger.info(String.format("Retrieving Next Of Kin with ID %s", id));
        try{

            Optional<Kin> record = kins.findById(id);
            if (record.isPresent()) {
                KinRequest request = this.mapper.map(record.get(), KinRequest.class);
                return CompletableFuture.completedFuture(request);
            }

            return CompletableFuture.completedFuture(null);
        } catch(Exception ex){
            logger.info("An error occurred in method 'findKinById' in 'KinService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<KinRequest> findKinByName(String name, long agentId) {
        logger.info(String.format("Retrieving Next Of Kin with name %s and agent ID %s", name, agentId));
        try{

            Optional<Kin> record = kins.findByFullNameAndAgent_Id(name, agentId);
            if (record.isPresent()) {
                KinRequest request = this.mapper.map(record.get(), KinRequest.class);
                return CompletableFuture.completedFuture(request);
            }

            return CompletableFuture.completedFuture(null);
        } catch(Exception ex){
            logger.info("An error occurred in method 'findKinByName' in 'KinService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<List<KinRequest>> getKins(long agentId) {
        logger.info("Retrieve individual agent next of kin");
        List<KinRequest> records  = new ArrayList<>();
        try{
            //..get kin records
            List<Kin> k_records = kins.findByAgent_Id(agentId);
            if(!k_records.isEmpty()){
                for (Kin record : k_records) {
                    records.add(this.mapper.map(record, KinRequest.class));
                }
            }

            return CompletableFuture.completedFuture(records);
        } catch(Exception ex){
            logger.info("An error occurred in method 'getKins' in 'KinService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public CompletableFuture<KinRequest> create(KinRequest kin, IndividualRequest agent) throws InterruptedException {
        logger.info("Adding new agent next of kin");
        try{
            Kin record = this.mapper.map(kin, Kin.class);

            IndividualAgent agentRec = mapper.map(agent, IndividualAgent.class);
            record.setAgent(agentRec);

            kins.save(record);
            record.setId(record.getId());
            return CompletableFuture.completedFuture(kin);
        }catch(Exception ex){
            logger.info("An error occurred in method 'create' in 'KinService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public void update(KinRequest kin, IndividualRequest agent) {
        logger.info("update Next of Kin");
        try{
            Kin record = this.mapper.map(kin, Kin.class);
            record.setId(kin.getId());

            IndividualAgent agentRec = mapper.map(agent, IndividualAgent.class);
            record.setAgent(agentRec);
            kins.update(record);
        } catch(Exception ex){
            logger.info("An error occurred in method 'update' in KinService");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public void softDelete(long id, boolean deleted) {
        logger.info("Soft Delete Next Of Kin");
        try{
            logger.info(String.format("Next of Kin is_deleted value set to %s", deleted ? "true": "false"));
            kins.markAsDeleted(id, deleted);
        } catch(Exception ex){
            logger.info("An error occurred in method 'softDelete' in 'KinService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }

    @Override
    public void delete(long id) {
        logger.info("Delete Next Of Kin");
        try{
            logger.info(String.format("Delete Next Of Kin with id %s", id));
            kins.deleteById(id);
        } catch(Exception ex){
            logger.info("An error occurred in method 'delete' in 'KinService'");
            logger.error(ex.getMessage());
            logger.info("Stacktrace::");
            logger.stackTrace(Arrays.toString(ex.getStackTrace()));
            throw new GeneralException(String.format("%s", ex.getMessage()));
        }
    }
}
