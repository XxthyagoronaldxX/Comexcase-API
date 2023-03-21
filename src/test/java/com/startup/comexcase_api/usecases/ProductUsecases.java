package com.startup.comexcase_api.usecases;

import com.startup.comexcase_api.domain.dtos.ProductDTO;
import com.startup.comexcase_api.domain.entities.ProductEntity;
import com.startup.comexcase_api.domain.entities.ProviderEntity;
import com.startup.comexcase_api.domain.entities.enums.SalesUnit;

import java.math.BigDecimal;

public class ProductUsecases {
    public static ProductEntity createProduct(ProviderEntity providerEntity) {
        ProductEntity productEntity = new ProductEntity();

        productEntity.setTitle("Açai");
        productEntity.setDelivery(false);
        productEntity.setAmount(20);
        productEntity.setSalesUnit(SalesUnit.L);
        productEntity.setDescription("Açai de qualidade, amazonia 100%.");
        productEntity.setPricePerUnit(16.00);
        productEntity.setProvider(providerEntity);
        providerEntity.getProducts().add(productEntity);

        return productEntity;
    }

    public static ProductDTO genProductDTO() {
        ProductDTO productDTO = new ProductDTO();

        productDTO.setTitle("Morango");
        productDTO.setAmount(25);
        productDTO.setPricePerUnit(BigDecimal.valueOf(6.76));
        productDTO.setDescription("Morango doce de qualidade!");
        productDTO.setDelivery(false);
        productDTO.setSalesUnit(SalesUnit.KG);

        return productDTO;
    }
}
