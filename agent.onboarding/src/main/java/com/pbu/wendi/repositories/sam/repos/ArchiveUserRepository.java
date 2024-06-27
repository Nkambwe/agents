package com.pbu.wendi.repositories.sam.repos;

import com.pbu.wendi.model.sam.models.ArchiveUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArchiveUserRepository extends JpaRepository<ArchiveUser, Long> {
    Optional<ArchiveUser> findByPfNo(String pfNo);
}
