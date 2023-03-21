package com.startup.comexcase_api.domain.services.impl;

import com.startup.comexcase_api.domain.dtos.ProviderDTO;
import com.startup.comexcase_api.domain.entities.DealerEntity;
import com.startup.comexcase_api.domain.entities.ProviderEntity;
import com.startup.comexcase_api.domain.exceptions.EntityConflictException;
import com.startup.comexcase_api.domain.exceptions.EntityNotFoundException;
import com.startup.comexcase_api.domain.repositories.IDealerRepository;
import com.startup.comexcase_api.domain.repositories.IProviderRepository;
import com.startup.comexcase_api.domain.services.IProviderService;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProviderService implements IProviderService {
    private final IProviderRepository providerRepository;
    private final IDealerRepository dealerRepository;

    public ProviderService(IProviderRepository providerRepository, IDealerRepository dealerRepository) {
        this.providerRepository = providerRepository;
        this.dealerRepository = dealerRepository;
    }

    @Override
    @Transactional
    public ProviderEntity save(ProviderDTO providerDTO, String email) {
        System.out.println(email);

        DealerEntity dealerEntity = dealerRepository
                .findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Dealer not found with this Email."));

        if (dealerEntity.getProvider() != null)
            throw new EntityConflictException("The dealer already have provider account.");

        ProviderEntity providerEntity = new ProviderEntity();

        providerEntity.setImgBackgroudUrl(providerDTO.getImgBackgroudUrl());
        providerEntity.setImgLogoUrl(providerDTO.getImgLogoUrl());

        return providerRepository.save(providerEntity);
    }

    @Override
    @Transactional
    public ProviderEntity update(ProviderDTO providerDTO, String email) {
        ProviderEntity providerEntity = providerRepository
                .findByDealerEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("The dealer does not have provider account."));

        providerEntity.setImgBackgroudUrl(providerDTO.getImgBackgroudUrl());
        providerEntity.setImgLogoUrl(providerDTO.getImgLogoUrl());

        return providerRepository.save(providerEntity);
    }

    @Override
    public Page<ProviderEntity> findAll(Pageable pageable) {
        return providerRepository.findAll(pageable);
    }
}
