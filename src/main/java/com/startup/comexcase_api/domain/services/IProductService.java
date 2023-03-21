package com.startup.comexcase_api.domain.services;

import com.startup.comexcase_api.domain.dtos.ProductDTO;
import com.startup.comexcase_api.domain.entities.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IProductService {
    Page<ProductEntity> findAllByProviderId(UUID providerId, Pageable pageable);
    Page<ProductEntity> findAllByProviderEmail(String email, Pageable pageable);
    Page<ProductEntity> findAll(Pageable pageable);
    ProductEntity save(ProductDTO productDTO, String email);
    ProductEntity update(ProductDTO productDTO, UUID id, String email);
    void remove(UUID id, String email);
}
