package com.pbu.wendi.services.agents.services;

import com.pbu.wendi.requests.agents.dto.DistrictRequest;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface DistrictService {
    CompletableFuture<Boolean> districtExists(long id);
    CompletableFuture<Boolean> existsByName(String name);
    CompletableFuture<Boolean>  existsByNameAndNotId(String name, Long id);
    CompletableFuture<DistrictRequest> findDistrictById(long id);
    CompletableFuture<DistrictRequest>  findDistrictByName(String name);
    CompletableFuture<List<DistrictRequest>> getAllDistricts();
    CompletableFuture<List<DistrictRequest>> getActiveDistricts();
    CompletableFuture<DistrictRequest> create(DistrictRequest district) throws InterruptedException;
    void update(DistrictRequest district);
    void softDelete(long id, boolean deleted);
    void delete(long id);
}
