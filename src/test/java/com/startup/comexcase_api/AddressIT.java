package com.startup.comexcase_api;

import com.startup.comexcase_api.domain.dtos.AddressDTO;
import com.startup.comexcase_api.domain.entities.AddressEntity;
import com.startup.comexcase_api.domain.entities.DealerEntity;
import com.startup.comexcase_api.domain.repositories.IAddressRepository;
import com.startup.comexcase_api.domain.repositories.IDealerRepository;
import com.startup.comexcase_api.usecases.AddressUsecases;
import com.startup.comexcase_api.usecases.DealerUsecases;
import com.startup.comexcase_api.util.DatabaseCleaner;
import com.startup.comexcase_api.util.GetToken;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import java.util.UUID;

@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class AddressIT {
    @LocalServerPort
    private int port;
    @Autowired
    private DatabaseCleaner databaseCleaner;
    @Autowired
    private IDealerRepository dealerRepository;
    @Autowired
    private IAddressRepository addressRepository;
    private AddressDTO addressDTO;
    private UUID currentAddressID;
    private UUID anotherAddressID;
    private DealerEntity currentDealer;
    private String token;

    @BeforeEach
    public void setUp() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        databaseCleaner.clearTables();
        prepareData();
    }

    private RequestSpecification buildConnection() {
        if (this.token == null) {
            this.token = "Bearer " + GetToken.execute(
                    currentDealer.getEmail(),
                    "12345678",
                    port
            );
        }

        return RestAssured.given()
                .basePath("/api/addresses")
                .port(port)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .headers("Authorization", this.token);
    }

    @Test
    public void should_return_status_201_and_body_when_create_address() {
        buildConnection()
                .body(addressDTO)
                .post()
                .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    public void should_return_status_200_and_body_when_update_address() {
        buildConnection()
                .pathParam("id", currentAddressID)
                .body(addressDTO)
                .put("/{id}")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void should_return_status_404_when_update_address_cause_address_id_notfound() {
        buildConnection()
                .pathParam("id", UUID.randomUUID())
                .body(addressDTO)
                .put("/{id}")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void should_return_status_403_when_update_address_cause_address_does_not_belong_the_dealer() {
        buildConnection()
                .pathParam("id", anotherAddressID)
                .body(addressDTO)
                .put("/{id}")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void should_return_status_200_and_body_when_findall_address_of_the_current_user() {
        buildConnection()
                .get()
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void should_return_status_200_when_findallbydealerid_address() {
        buildConnection()
                .pathParam("id", currentDealer.getId())
                .get("/dealer/{id}")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void should_return_status_404_when_findallbyclienteid_address_cause_dealerid_notfound() {
        buildConnection()
                .pathParam("id", UUID.randomUUID())
                .get("/dealer/{id}")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void should_return_status_200_and_confirm_if_address_was_removed_when_remove_address() {
        buildConnection()
                .pathParam("id", currentAddressID)
                .delete("/{id}")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        Optional<AddressEntity> addressEntityOptional = addressRepository.findById(currentAddressID);

        assertThat(addressEntityOptional.isPresent()).isFalse();
    }

    @Test
    public void should_return_status_404_when_remove_address_cause_address_id_notfound() {
        buildConnection()
                .pathParam("id", UUID.randomUUID())
                .delete("/{id}")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void should_return_status_403_when_remove_address_cause_address_does_not_belong_the_current_user() {
        buildConnection()
                .pathParam("id", anotherAddressID)
                .delete("/{id}")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    private void prepareData() {
        // CRIA UM REVENDEDOR E ENDEREÇO
        DealerEntity dealerEntity = DealerUsecases.createDealerOne();

        currentDealer = dealerEntity = dealerRepository.save(dealerEntity);

        AddressEntity addressEntity = AddressUsecases.createAddress(dealerEntity);

        currentAddressID =  addressRepository.save(addressEntity).getId();

        // CRIA UM SEGUNDO REVENDEDOR E ENDEREÇO
        dealerEntity = DealerUsecases.createDealerTwo();

        dealerEntity = dealerRepository.save(dealerEntity);

        addressEntity = AddressUsecases.createAddress(dealerEntity);

        anotherAddressID =  addressRepository.save(addressEntity).getId();

        // PREECHE INFORMAÇÕES NO DTO DO ADDRESS PARA CRIAÇÃO DE UM NOVO ENDEREÇO
        addressDTO = AddressUsecases.genAddressDTO();
    }
}
