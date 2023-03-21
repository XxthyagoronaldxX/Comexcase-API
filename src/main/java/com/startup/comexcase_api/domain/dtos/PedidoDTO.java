package com.startup.comexcase_api.domain.dtos;

import com.startup.comexcase_api.domain.dtos.validators.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PedidoDTO {
    @UUID
    private String providerId;
}
