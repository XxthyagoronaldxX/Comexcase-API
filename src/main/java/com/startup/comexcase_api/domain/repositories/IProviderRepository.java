package com.startup.comexcase_api.domain.repositories;

import com.startup.comexcase_api.domain.entities.ProviderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IProviderRepository extends JpaRepository<ProviderEntity, UUID> {
    Optional<ProviderEntity> findByDealerEmail(String email);
}
