package com.startup.comexcase_api.domain.repositories;

import com.startup.comexcase_api.domain.entities.VerificationTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IVerificationTokenRepository extends JpaRepository<VerificationTokenEntity, UUID> {
    Optional<VerificationTokenEntity> findVerificationTokenEntityByToken(String token);
}
