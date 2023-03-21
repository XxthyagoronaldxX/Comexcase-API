package com.startup.comexcase_api;

import com.startup.comexcase_api.domain.dtos.ProviderDTO;
import com.startup.comexcase_api.domain.entities.DealerEntity;
import com.startup.comexcase_api.domain.repositories.IDealerRepository;
import com.startup.comexcase_api.usecases.DealerUsecases;
import com.startup.comexcase_api.usecases.ProviderUsecases;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
public class ProviderIT {
    @Autowired
    private IDealerRepository dealerRepository;
    @Autowired
    private DatabaseCleaner databaseCleaner;
    @LocalServerPort
    private int port;
    private DealerEntity currentDealer;
    private DealerEntity currentDealerProvider;
    private ProviderDTO providerDTO;
    private String tokenDealer;
    private String tokenDealerProvider;

    private RequestSpecification buildConnection() {
        if (this.tokenDealer == null || this.tokenDealerProvider == null) {
            this.tokenDealer = "Bearer " + GetToken.execute(
                    currentDealer.getEmail(),
                    "12345678",
                    port
            );
            this.tokenDealerProvider = "Bearer " + GetToken.execute(
                    currentDealerProvider.getEmail(),
                    "12345678",
                    port
            );
        }

        return RestAssured
                .given()
                .basePath("/api/providers")
                .port(port)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON);
    }

    @BeforeEach
    public void setUp() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        databaseCleaner.clearTables();
        prepareData();
    }

    @Test
    public void should_return_status_200_when_findall_provider() {
        buildConnection()
                .headers("Authorization", tokenDealer)
                .get()
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void should_return_status_201_and_body_when_create_provider() {
        buildConnection()
                .headers("Authorization", tokenDealer)
                .body(providerDTO)
                .post()
                .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    public void should_return_status_416_when_create_provider_cause_dealer_id_already_have_a_provider_account() {
        buildConnection()
                .headers("Authorization", tokenDealerProvider)
                .body(providerDTO)
                .post()
                .then()
                .statusCode(HttpStatus.CONFLICT.value());
    }

    @Test
    public void should_return_status_200_when_update_provider() {
        buildConnection()
                .headers("Authorization", tokenDealerProvider)
                .body(providerDTO)
                .put()
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void should_return_status_403_when_update_provider_cause_the_actual_dealer_does_not_have_provider_account() {
        buildConnection()
                .headers("Authorization", tokenDealer)
                .body(providerDTO)
                .put()
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    public void prepareData() {
        DealerEntity dealerEntity = DealerUsecases.createDealerOne();
        ProviderUsecases.createProvider(dealerEntity);
        currentDealerProvider = dealerRepository.save(dealerEntity);

        dealerEntity = DealerUsecases.createDealerTwo();
        currentDealer = dealerRepository.save(dealerEntity);

        providerDTO = new ProviderDTO();
    }
}
