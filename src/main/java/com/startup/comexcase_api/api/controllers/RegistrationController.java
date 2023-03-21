package com.startup.comexcase_api.api.controllers;

import com.startup.comexcase_api.domain.entities.DealerEntity;
import com.startup.comexcase_api.domain.entities.VerificationTokenEntity;
import com.startup.comexcase_api.domain.exceptions.NoPermissionException;
import com.startup.comexcase_api.domain.repositories.IDealerRepository;
import com.startup.comexcase_api.domain.services.IVerificationTokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.function.EntityResponse;

import java.util.Calendar;
import java.util.Locale;

@RestController
@RequestMapping("/api/registration")
public class RegistrationController {
    private final IVerificationTokenService verificationTokenService;
    private final IDealerRepository dealerRepository;

    public RegistrationController(IVerificationTokenService verificationTokenService, IDealerRepository dealerRepository) {
        this.verificationTokenService = verificationTokenService;
        this.dealerRepository = dealerRepository;
    }

    @GetMapping("/confirm")
    public ResponseEntity<?> confirmRegistration(@RequestParam("token") String token) {
        VerificationTokenEntity verificationTokenEntity = verificationTokenService.findByToken(token);

        DealerEntity dealerEntity = verificationTokenEntity.getDealer();

        Calendar calendar = Calendar.getInstance();

        if ((verificationTokenEntity.getExpiryDate().getTime() - calendar.getTime().getTime()) <= 0) {
            throw new NoPermissionException("Token that was sent to email is expired.");
        }

        dealerEntity.setEnabled(true);

        dealerRepository.save(dealerEntity);

        return ResponseEntity.ok("The account was activated.");
    }
}
