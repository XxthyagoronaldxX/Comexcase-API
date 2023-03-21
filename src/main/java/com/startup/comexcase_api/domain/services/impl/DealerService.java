package com.startup.comexcase_api.domain.services.impl;

import com.startup.comexcase_api.domain.dtos.dealer.CreateDealerDTO;
import com.startup.comexcase_api.domain.dtos.dealer.UpdateDealerDTO;
import com.startup.comexcase_api.domain.entities.DealerEntity;
import com.startup.comexcase_api.domain.entities.RoleEntity;
import com.startup.comexcase_api.domain.exceptions.EntityConflictException;
import com.startup.comexcase_api.domain.exceptions.EntityNotFoundException;
import com.startup.comexcase_api.domain.repositories.IDealerRepository;
import com.startup.comexcase_api.domain.services.IDealerService;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DealerService implements IDealerService {
    final IDealerRepository dealerRepository;
    final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public DealerService(IDealerRepository dealerRepository) {
        this.dealerRepository = dealerRepository;
    }

    @Override
    @Transactional
    public DealerEntity save(CreateDealerDTO createDealerDTO) {
        dealerRepository
                .findByEmail(createDealerDTO.getEmail())
                .ifPresent(dealer -> {
                    throw new EntityConflictException("Can't save a dealer with this email, cause its already exists.");
                });

        dealerRepository
                .findByPhoneNumber(createDealerDTO.getPhoneNumber())
                .ifPresent(dealer -> {
                    throw new EntityConflictException("Can't save a dealer with this phone number, cause its already exists.");
                });

        DealerEntity dealerEntity = new DealerEntity();

        BeanUtils.copyProperties(createDealerDTO, dealerEntity, "password");

        dealerEntity.setPassword(passwordEncoder.encode(createDealerDTO.getPassword()));

        RoleEntity roleEntity = RoleEntity.createDealerRole();

        dealerEntity.getRoles().add(roleEntity);

        return this.dealerRepository.save(dealerEntity);
    }

    @Override
    @Transactional
    public DealerEntity update(UpdateDealerDTO updateDealerDTO, String email) {
        DealerEntity dealerEntity = dealerRepository
                .findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Dealer not found by email."));

        BeanUtils.copyProperties(updateDealerDTO, dealerEntity);

        dealerEntity.setPassword(passwordEncoder.encode(dealerEntity.getPassword()));

        return dealerRepository.save(dealerEntity);
    }

    @Override
    public DealerEntity findByDealerId(UUID dealerId) {
        return dealerRepository
                .findById(dealerId)
                .orElseThrow(() -> new EntityNotFoundException("Dealer not found by id."));
    }

    @Override
    public Page<DealerEntity> findAll(Pageable pageable) {
        return this.dealerRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public void remove(String email) {
        DealerEntity dealerEntity = dealerRepository
                .findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Dealer not found by PhoneNumber."));

        dealerRepository.deleteById(dealerEntity.getId());
    }
}
