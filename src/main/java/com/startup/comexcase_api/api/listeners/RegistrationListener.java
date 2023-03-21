package com.startup.comexcase_api.api.listeners;

import com.startup.comexcase_api.api.events.OnRegistrationCompleteEvent;
import com.startup.comexcase_api.domain.dtos.VerificationTokenDTO;
import com.startup.comexcase_api.domain.entities.DealerEntity;
import com.startup.comexcase_api.domain.services.IDealerService;
import com.startup.comexcase_api.domain.services.IVerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {
    private final IVerificationTokenService verificationTokenService;

    private final MessageSource messages;

    private final JavaMailSender mailSender;

    public RegistrationListener(
            IVerificationTokenService verificationTokenService,
            MessageSource messages,
            JavaMailSender mailSender
    ) {
        this.verificationTokenService = verificationTokenService;
        this.messages = messages;
        this.mailSender = mailSender;
    }

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        DealerEntity dealer = event.getDealer();
        String token = UUID.randomUUID().toString();

        VerificationTokenDTO verificationTokenDTO = new VerificationTokenDTO();

        verificationTokenDTO.setDealer(dealer);
        verificationTokenDTO.setToken(token);

        verificationTokenService.save(verificationTokenDTO);

        String recipientAddress = dealer.getEmail();
        String subject = "Confirmação de cadastro.";
        String confirmationUrl
                = event.getAppUrl() + "/api/registration/confirm?token=" + token;
        String message = "Click no link para confirmar o seu registro: ";

        SimpleMailMessage email = new SimpleMailMessage();

        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + "\r\n" + "http://localhost:8080" + confirmationUrl);

        mailSender.send(email);
    }
}
