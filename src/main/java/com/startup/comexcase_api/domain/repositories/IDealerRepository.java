package com.startup.comexcase_api.domain.repositories;

import com.startup.comexcase_api.domain.entities.DealerEntity;
import com.startup.comexcase_api.domain.entities.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IDealerRepository extends JpaRepository<DealerEntity, UUID> {
    Optional<DealerEntity> findByEmail(String email);
    Optional<DealerEntity> findByPhoneNumber(String phoneNumber);
}
