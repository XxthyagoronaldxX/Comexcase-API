package com.startup.comexcase_api.usecases;

import com.startup.comexcase_api.domain.entities.DealerEntity;
import com.startup.comexcase_api.domain.entities.ProviderEntity;
import com.startup.comexcase_api.domain.entities.RoleEntity;

public class ProviderUsecases {
    public static ProviderEntity createProvider(DealerEntity dealerEntity) {
        ProviderEntity providerEntity = new ProviderEntity();

        providerEntity.setDealer(dealerEntity);
        dealerEntity.setProvider(providerEntity);
        dealerEntity.getRoles().add(RoleEntity.createProviderRole());

        return providerEntity;
    }
}
