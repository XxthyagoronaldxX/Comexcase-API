package com.startup.comexcase_api.usecases;

import com.startup.comexcase_api.domain.dtos.AddressDTO;
import com.startup.comexcase_api.domain.entities.AddressEntity;
import com.startup.comexcase_api.domain.entities.DealerEntity;

public class AddressUsecases {
    public static AddressEntity createAddress(DealerEntity dealerEntity) {
        AddressEntity addressEntity = new AddressEntity();

        addressEntity.setCity("Belém");
        addressEntity.setCountry("Brasil");
        addressEntity.setState("Pará");
        addressEntity.setNeighborhood("Coqueiro");
        addressEntity.setDealer(dealerEntity);
        addressEntity.setZipCode("66670-213");
        addressEntity.setHouseNumber("59");

        return addressEntity;
    }

    public static AddressDTO genAddressDTO() {
        AddressDTO addressDTO = new AddressDTO();

        addressDTO.setCity("Belém");
        addressDTO.setCountry("Brasil");
        addressDTO.setState("Pará");
        addressDTO.setNeighborhood("Coqueiro");
        addressDTO.setZipCode("66670-213");
        addressDTO.setHouseNumber("59");

        return addressDTO;
    }
}
