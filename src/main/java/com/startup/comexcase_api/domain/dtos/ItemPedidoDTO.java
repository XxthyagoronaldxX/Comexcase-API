package com.startup.comexcase_api.domain.dtos;

import com.startup.comexcase_api.domain.dtos.validators.UUID;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ItemPedidoDTO {
    @Min(0)
    private int amount;
    @UUID
    private String productId;
    @UUID
    private String pedidoId;
}
