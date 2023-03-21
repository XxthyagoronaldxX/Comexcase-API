package com.startup.comexcase_api.domain.services;

import com.startup.comexcase_api.domain.dtos.AddressDTO;
import com.startup.comexcase_api.domain.entities.AddressEntity;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface IAddressService {
    AddressEntity save(AddressDTO addressDTO, String email);
    AddressEntity update(AddressDTO addressDTO, UUID id, String email);
    Iterable<AddressEntity> findAll(Pageable pageable);
    Optional<AddressEntity> findById(UUID addressId);
    Iterable<AddressEntity> findAllByDealerEmail(String email);
    Iterable<AddressEntity> findAllByDealerId(UUID dealerId);
    void remove(UUID id, String email);
}
