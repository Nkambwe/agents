package com.pbu.wendi.services.agents.services;

import com.pbu.wendi.utils.requests.agents.dto.CountyRequest;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface CountyService {
    CompletableFuture<Boolean> countyExists(long id);
    CompletableFuture<Boolean> existsByName(String countyName);
    CompletableFuture<Boolean>  existsByNameAndNotId(String countyName, Long id);
    CompletableFuture<CountyRequest> findCountyById(long id);
    CompletableFuture<CountyRequest> findCountyByName(String countyName);
    CompletableFuture<List<CountyRequest>> getAllCounties();
    CompletableFuture<List<CountyRequest>> getActiveCounties();
    CompletableFuture<CountyRequest> create(CountyRequest county) throws InterruptedException;
    void update(CountyRequest county);
    void softDelete(long id, boolean deleted);
    void delete(long id);
}
