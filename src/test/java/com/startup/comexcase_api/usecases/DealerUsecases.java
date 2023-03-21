package com.startup.comexcase_api.usecases;

import com.startup.comexcase_api.domain.dtos.dealer.CreateDealerDTO;
import com.startup.comexcase_api.domain.dtos.dealer.UpdateDealerDTO;
import com.startup.comexcase_api.domain.entities.DealerEntity;
import com.startup.comexcase_api.domain.entities.RoleEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DealerUsecases {
    private final static PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static DealerEntity createDealerOne() {
        DealerEntity dealerEntity = new DealerEntity();

        dealerEntity.setName("Thyago Ronald Santos da Silva");
        dealerEntity.setNickname("TCKUBIRIM");
        dealerEntity.setEmail("thyagoronald8@gmail.com");
        dealerEntity.setPhoneNumber("(+55) 91 98284-5460");
        dealerEntity.setPassword(passwordEncoder.encode("12345678"));
        dealerEntity.getRoles().add(RoleEntity.createDealerRole());

        return dealerEntity;
    }

    public static DealerEntity createDealerTwo() {
        DealerEntity dealerEntity = new DealerEntity();

        dealerEntity.setName("Fabio Nascimento Figueiredo");
        dealerEntity.setNickname("fnascimento");
        dealerEntity.setEmail("fnascimento23@gmail.com");
        dealerEntity.setPhoneNumber("(+55) 91 92304-2133");
        dealerEntity.setPassword(passwordEncoder.encode("12345678"));
        dealerEntity.getRoles().add(RoleEntity.createDealerRole());

        return dealerEntity;
    }

    public static DealerEntity createDealerThree() {
        DealerEntity dealerEntity = new DealerEntity();

        dealerEntity.setName("Silvia Cristina Miranda Santos");
        dealerEntity.setNickname("moranguinho03");
        dealerEntity.setEmail("moranguino3@gmail.com");
        dealerEntity.setPhoneNumber("(+55) 91 92304-2113");
        dealerEntity.setPassword(passwordEncoder.encode("12345678"));
        dealerEntity.getRoles().add(RoleEntity.createDealerRole());

        return dealerEntity;
    }

    public static CreateDealerDTO genCreateDealerDTO() {
        CreateDealerDTO createDealerDTO = new CreateDealerDTO();

        createDealerDTO.setName("Thyago Ronald Santos da Silva");
        createDealerDTO.setNickname("TCKUBIRIM");
        createDealerDTO.setEmail("thyagoronald28@gmail.com");
        createDealerDTO.setPhoneNumber("(+55) 91 98284-5443");
        createDealerDTO.setPassword("12345678");

        return createDealerDTO;
    }

    public static UpdateDealerDTO genUpdateDealerDTO() {
        UpdateDealerDTO updateDealerDTO = new UpdateDealerDTO();

        updateDealerDTO.setName("Roberto Testando");
        updateDealerDTO.setNickname("RobTest");
        updateDealerDTO.setEmail("robtest8@gmail.com");
        updateDealerDTO.setPassword("12345678");

        return updateDealerDTO;
    }
}
