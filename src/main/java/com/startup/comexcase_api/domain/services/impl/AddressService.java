package com.startup.comexcase_api.domain.services.impl;

import com.startup.comexcase_api.domain.dtos.AddressDTO;
import com.startup.comexcase_api.domain.entities.AddressEntity;
import com.startup.comexcase_api.domain.entities.DealerEntity;
import com.startup.comexcase_api.domain.exceptions.EntityNotFoundException;
import com.startup.comexcase_api.domain.exceptions.NoPermissionException;
import com.startup.comexcase_api.domain.repositories.IAddressRepository;
import com.startup.comexcase_api.domain.repositories.IDealerRepository;
import com.startup.comexcase_api.domain.services.IAddressService;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AddressService implements IAddressService {
    private final IAddressRepository addressRepository;
    private final IDealerRepository dealerRepository;

    public AddressService(IAddressRepository addressRepository, IDealerRepository dealerRepository) {
        this.addressRepository = addressRepository;
        this.dealerRepository = dealerRepository;
    }

    @Transactional
    @Override
    public AddressEntity save(AddressDTO addressDTO, String email) {
        DealerEntity dealerEntity = dealerRepository
                .findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Dealer not found by PhoneNumber."));

        AddressEntity addressEntity = new AddressEntity();

        BeanUtils.copyProperties(addressDTO, addressEntity);

        addressEntity.setDealer(dealerEntity);

        return this.addressRepository.save(addressEntity);
    }

    @Override
    public Iterable<AddressEntity> findAll(Pageable pageable) {
        return addressRepository.findAll(pageable);
    }

    @Override
    public Optional<AddressEntity> findById(UUID addressId) {
        return addressRepository.findById(addressId);
    }

    @Override
    public Iterable<AddressEntity> findAllByDealerEmail(String email) {
        return addressRepository.findAllByDealerEmail(email);
    }

    @Override
    public Iterable<AddressEntity> findAllByDealerId(UUID dealerId) {
        dealerRepository
                .findById(dealerId)
                .orElseThrow(() -> new EntityNotFoundException("Dealer not found by Id."));

        return addressRepository.findAllByDealerId(dealerId);
    }

    @Transactional
    @Override
    public AddressEntity update(AddressDTO addressDTO, UUID id, String email) {
        AddressEntity addressEntity = addressRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Address not found by Id."));

        if (!addressEntity.getDealer().getEmail().equals(email))
            throw new NoPermissionException("The dealer don't have permission to modify this address.");

        BeanUtils.copyProperties(addressDTO, addressEntity);

        return addressRepository.save(addressEntity);
    }

    @Transactional
    @Override
    public void remove(UUID id, String email) {
        AddressEntity addressEntity = addressRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Address not found by Id."));

        if (!addressEntity.getDealer().getEmail().equals(email))
            throw new NoPermissionException("The dealer don't have permission to remove this address.");

        addressRepository.deleteById(id);
    }
}
