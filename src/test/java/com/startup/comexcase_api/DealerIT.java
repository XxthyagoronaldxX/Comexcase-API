package com.startup.comexcase_api;

import com.startup.comexcase_api.domain.dtos.dealer.CreateDealerDTO;
import com.startup.comexcase_api.domain.dtos.dealer.UpdateDealerDTO;
import com.startup.comexcase_api.domain.entities.DealerEntity;
import com.startup.comexcase_api.domain.entities.ProductEntity;
import com.startup.comexcase_api.domain.entities.ProviderEntity;
import com.startup.comexcase_api.domain.repositories.IDealerRepository;
import com.startup.comexcase_api.domain.repositories.IProductRepository;
import com.startup.comexcase_api.usecases.DealerUsecases;
import com.startup.comexcase_api.usecases.ProductUsecases;
import com.startup.comexcase_api.usecases.ProviderUsecases;
import com.startup.comexcase_api.util.DatabaseCleaner;
import com.startup.comexcase_api.util.GetToken;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.*;

import java.util.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
public class DealerIT {
    @Autowired
    private DatabaseCleaner databaseCleaner;
    @Autowired
    private IDealerRepository dealerRepository;
    @Autowired
    private IProductRepository productRepository;
    @LocalServerPort
    private int port;
    private DealerEntity currentDealer;
    private CreateDealerDTO createDealerDTO;
    private UpdateDealerDTO updateDealerDTO;
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

        return RestAssured
                .given()
                .basePath("/api/dealers")
                .port(port)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .headers("Authorization", this.token);
    }

    @Test
    void should_return_status_200_when_findall_dealer() {
        buildConnection()
                .get()
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void should_return_status_200_when_findbyid_dealer() {
        buildConnection()
                .pathParam("dealerId", currentDealer.getId())
                .get("/{dealerId}")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void should_return_status_404_when_findbyid_dealer_cause_id_notfound() {
        buildConnection()
                .pathParam("dealerId", UUID.randomUUID())
                .get("/{dealerId}")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void should_return_status_201_and_body_when_create_dealer() {
        buildConnection()
                .body(createDealerDTO)
                .post()
                .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    void should_return_status_409_and_body_when_create_dealer_cause_phonenumber_conflict() {
        createDealerDTO.setPhoneNumber(currentDealer.getPhoneNumber());

        buildConnection()
                .body(createDealerDTO)
                .post()
                .then()
                .statusCode(HttpStatus.CONFLICT.value())
                .and()
                .body("", Matchers.hasKey("message"));
    }

    @Test
    void should_return_status_200_and_body_when_update_dealer() {
        buildConnection()
                .body(updateDealerDTO)
                .put()
                .then()
                .statusCode(HttpStatus.OK.value())
                .and()
                .body("", Matchers.hasKey("name"));
    }

    @Test
    void should_return_status_200_and_confirm_if_dealer_was_deleted_when_remove_dealer() {
        buildConnection()
                .delete()
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        Optional<DealerEntity> dealerEntityOptional = dealerRepository.findById(currentDealer.getId());

        assertThat(dealerEntityOptional.isEmpty()).isFalse();

        DealerEntity dealerEntity = dealerEntityOptional.get();

        assertThat(dealerEntity.getEmail().isEmpty()).isTrue();
        assertThat(dealerEntity.isDeleted()).isTrue();

        ProviderEntity providerEntity = dealerEntity.getProvider();

        assertThat(providerEntity.isDeleted()).isTrue();

        Page<ProductEntity> productEntityList = productRepository.findAllByProvider(providerEntity, Pageable.unpaged());

        for (ProductEntity productEntity : productEntityList)
            assertThat(productEntity.isDeleted()).isTrue();
    }

    private void prepareData() {
        DealerEntity dealerEntity = DealerUsecases.createDealerOne();
        ProviderEntity providerEntity = ProviderUsecases.createProvider(dealerEntity);
        ProductEntity productEntity = ProductUsecases.createProduct(providerEntity);

        currentDealer = dealerRepository.save(dealerEntity);

        dealerEntity = DealerUsecases.createDealerTwo();

        dealerRepository.save(dealerEntity);

        createDealerDTO = DealerUsecases.genCreateDealerDTO();
        updateDealerDTO = DealerUsecases.genUpdateDealerDTO();
    }
}
