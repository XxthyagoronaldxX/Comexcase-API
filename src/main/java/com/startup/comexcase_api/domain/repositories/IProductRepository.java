package com.startup.comexcase_api.domain.repositories;

import com.startup.comexcase_api.domain.entities.ProductEntity;
import com.startup.comexcase_api.domain.entities.ProviderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IProductRepository extends JpaRepository<ProductEntity, UUID> {
    Page<ProductEntity> findAllByProviderId(UUID providerId, Pageable pageable);
    Page<ProductEntity> findAllByProvider(ProviderEntity provider, Pageable pageable);
}
