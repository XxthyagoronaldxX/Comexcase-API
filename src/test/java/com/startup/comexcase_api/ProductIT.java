package com.startup.comexcase_api;

import com.startup.comexcase_api.domain.dtos.ProductDTO;
import com.startup.comexcase_api.domain.entities.DealerEntity;
import com.startup.comexcase_api.domain.entities.ProductEntity;
import com.startup.comexcase_api.domain.entities.ProviderEntity;
import com.startup.comexcase_api.domain.entities.enums.SalesUnit;
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
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.security.Provider;
import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
public class ProductIT {
    @LocalServerPort
    private int port;
    @Autowired
    private IProductRepository productRepository;
    @Autowired
    private IDealerRepository dealerRepository;
    @Autowired
    private DatabaseCleaner databaseCleaner;
    private ProductDTO productDTO;
    private UUID currentProductId;
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

        return RestAssured
                .given()
                .basePath("/api/products")
                .port(port)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .headers("Authorization", token);
    }

    @Test
    public void should_return_status_200_when_findall_product() {
        buildConnection()
                .get()
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void should_return_status_201_when_create_product() {
        buildConnection()
                .body(productDTO)
                .post()
                .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    public void should_return_status_200_and_confirm_value_when_update_product() {
        buildConnection()
                .pathParam("productId", currentProductId)
                .body(productDTO)
                .put("/{productId}")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void should_return_status_404_when_update_product_cause_product_id_notfound() {
        buildConnection()
                .pathParam("productId", UUID.randomUUID())
                .body(productDTO)
                .put("/{productId}")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void should_return_status_415_when_update_product_cause_invalid_product_id_value() {
        buildConnection()
                .pathParam("productId", "invalid")
                .body(productDTO)
                .put("/{productId}")
                .then()
                .statusCode(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value());
    }

    @Test
    public void should_return_status_204_when_delete_product() {
        buildConnection()
                .pathParam("productId", currentProductId)
                .delete("/{productId}")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        ProductEntity productEntity = productRepository
                .findById(currentProductId)
                .orElseThrow();

        assertThat(productEntity.isDeleted()).isTrue();
    }

    @Test
    public void should_return_status_404_when_delete_product_cause_product_id_notfound() {
        buildConnection()
                .pathParam("productId", UUID.randomUUID())
                .delete("/{productId}")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    public void prepareData() {
        DealerEntity dealerEntity = DealerUsecases.createDealerOne();
        ProviderEntity providerEntity = ProviderUsecases.createProvider(dealerEntity);
        currentDealer = dealerEntity = dealerRepository.save(dealerEntity);

        ProductEntity productEntity = ProductUsecases.createProduct(dealerEntity.getProvider());
        currentProductId = productRepository.save(productEntity).getId();

        productDTO = ProductUsecases.genProductDTO();
    }
}
