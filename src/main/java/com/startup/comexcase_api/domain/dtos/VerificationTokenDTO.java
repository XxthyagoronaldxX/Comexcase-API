package com.startup.comexcase_api.domain.dtos;

import com.startup.comexcase_api.domain.entities.DealerEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class VerificationTokenDTO {
    @NotBlank
    private String token;
    @NotNull
    private DealerEntity dealer;
}
