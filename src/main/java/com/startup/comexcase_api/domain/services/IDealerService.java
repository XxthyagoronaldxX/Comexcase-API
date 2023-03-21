package com.startup.comexcase_api.domain.services;

import com.startup.comexcase_api.domain.dtos.dealer.CreateDealerDTO;
import com.startup.comexcase_api.domain.dtos.dealer.UpdateDealerDTO;
import com.startup.comexcase_api.domain.entities.DealerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IDealerService {
    DealerEntity save(CreateDealerDTO dealerDTO);
    DealerEntity update(UpdateDealerDTO dealerDTO, String email);
    DealerEntity findByDealerId(UUID dealerId);
    Page<DealerEntity> findAll(Pageable pageable);
    void remove(String email);
}
