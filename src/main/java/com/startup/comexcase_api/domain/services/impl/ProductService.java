package com.startup.comexcase_api.domain.services.impl;

import com.startup.comexcase_api.domain.dtos.ProductDTO;
import com.startup.comexcase_api.domain.entities.ProductEntity;
import com.startup.comexcase_api.domain.entities.ProviderEntity;
import com.startup.comexcase_api.domain.exceptions.EntityNotFoundException;

import com.startup.comexcase_api.domain.exceptions.NoPermissionException;
import com.startup.comexcase_api.domain.repositories.IProductRepository;
import com.startup.comexcase_api.domain.repositories.IProviderRepository;
import com.startup.comexcase_api.domain.services.IProductService;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProductService implements IProductService {
    private final IProductRepository productRepository;
    private final IProviderRepository providerRepository;

    public ProductService(IProductRepository productRepository, IProviderRepository providerRepository) {
        this.productRepository = productRepository;
        this.providerRepository = providerRepository;
    }

    @Override
    public Page<ProductEntity> findAllByProviderId(UUID providerId, Pageable pageable) {
        providerRepository
                .findById(providerId)
                .orElseThrow(() -> new EntityNotFoundException("Provider not found by id."));

        return productRepository.findAllByProviderId(providerId, pageable);
    }

    @Override
    public Page<ProductEntity> findAllByProviderEmail(String email, Pageable pageable) {
        ProviderEntity providerEntity = providerRepository
                .findByDealerEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Provider not found by dealer phone number."));

        return productRepository.findAllByProvider(providerEntity, pageable);
    }

    @Override
    public Page<ProductEntity> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public ProductEntity save(ProductDTO productDTO, String email) {
        ProviderEntity providerEntity = providerRepository
                .findByDealerEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Provider not found by dealer phone number."));

        ProductEntity productEntity = new ProductEntity();

        BeanUtils.copyProperties(productDTO, productEntity);
        productEntity.setProvider(providerEntity);

        return productRepository.save(productEntity);
    }

    @Override
    @Transactional
    public ProductEntity update(ProductDTO productDTO, UUID id, String email) {
        ProductEntity productEntity = productRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found by Id."));

        if (!productEntity.getProvider().getDealer().getEmail().equals(email))
            throw new NoPermissionException("This provider does not have permission to modify this product.");

        BeanUtils.copyProperties(productDTO, productEntity);

        return productRepository.save(productEntity);
    }

    @Override
    @Transactional
    public void remove(UUID id, String email) {
        ProductEntity productEntity = productRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found by Id."));

        if (!productEntity.getProvider().getDealer().getEmail().equals(email))
            throw new NoPermissionException("The provider does not have permission to remove this product.");

        productRepository.delete(productEntity);
    }
}
