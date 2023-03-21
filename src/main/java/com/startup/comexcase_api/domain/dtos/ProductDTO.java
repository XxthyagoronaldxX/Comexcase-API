package com.startup.comexcase_api.domain.dtos;

import com.startup.comexcase_api.domain.entities.enums.SalesUnit;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter
public class ProductDTO {
    @NotBlank
    private String title;
    private String imgProductUrl;
    private String description;
    @NotNull
    private SalesUnit salesUnit;
    @Min(0)
    private BigDecimal pricePerUnit;
    @Min(0)
    private int amount;
    @NotNull
    private boolean isDelivery;
}
