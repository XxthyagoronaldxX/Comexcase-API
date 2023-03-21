package com.startup.comexcase_api.domain.repositories;

import com.startup.comexcase_api.domain.entities.AddressEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IAddressRepository extends JpaRepository<AddressEntity, UUID> {
    Iterable<AddressEntity> findAllByDealerId(UUID dealerId);

    Iterable<AddressEntity> findAllByDealerEmail(String email);

    Page<AddressEntity> findAll (Pageable pageable);
}
