package com.startup.comexcase_api.domain.services;

import com.startup.comexcase_api.domain.dtos.ProviderDTO;
import com.startup.comexcase_api.domain.entities.ProviderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IProviderService {
    ProviderEntity save(ProviderDTO providerDTO, String phoneNumber);

    ProviderEntity update(ProviderDTO providerDTO, String phoneNumber);

    Page<ProviderEntity> findAll(Pageable pageable);
}
