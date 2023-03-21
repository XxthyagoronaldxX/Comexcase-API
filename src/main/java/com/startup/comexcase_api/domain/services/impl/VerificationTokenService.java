package com.startup.comexcase_api.domain.services.impl;

import com.startup.comexcase_api.domain.dtos.VerificationTokenDTO;
import com.startup.comexcase_api.domain.entities.VerificationTokenEntity;
import com.startup.comexcase_api.domain.exceptions.EntityNotFoundException;
import com.startup.comexcase_api.domain.repositories.IVerificationTokenRepository;
import com.startup.comexcase_api.domain.services.IVerificationTokenService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public class VerificationTokenService implements IVerificationTokenService {
    private final IVerificationTokenRepository verificationTokenRepository;

    public VerificationTokenService(IVerificationTokenRepository verificationTokenRepository) {
        this.verificationTokenRepository = verificationTokenRepository;
    }

    @Override
    public VerificationTokenEntity save(@Valid VerificationTokenDTO verificationTokenDTO) {
        VerificationTokenEntity verificationTokenEntity = new VerificationTokenEntity();

        verificationTokenEntity.setToken(verificationTokenDTO.getToken());
        verificationTokenEntity.setDealer(verificationTokenDTO.getDealer());

        return verificationTokenRepository.save(verificationTokenEntity);
    }

    @Override
    public VerificationTokenEntity findByToken(String token) {
        return verificationTokenRepository
                .findVerificationTokenEntityByToken(token)
                .orElseThrow(() -> new EntityNotFoundException("VerificationToken not found by Token."));
    }


}
