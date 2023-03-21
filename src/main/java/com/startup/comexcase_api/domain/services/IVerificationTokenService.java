package com.startup.comexcase_api.domain.services;

import com.startup.comexcase_api.domain.dtos.VerificationTokenDTO;
import com.startup.comexcase_api.domain.entities.VerificationTokenEntity;
import org.springframework.stereotype.Service;

public interface IVerificationTokenService {
    VerificationTokenEntity save(VerificationTokenDTO verificationTokenDTO);

    VerificationTokenEntity findByToken(String token);
}
