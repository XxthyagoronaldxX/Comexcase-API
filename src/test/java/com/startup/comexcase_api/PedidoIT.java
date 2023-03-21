package com.startup.comexcase_api;

import com.startup.comexcase_api.domain.dtos.PedidoDTO;
import com.startup.comexcase_api.domain.entities.DealerEntity;
import com.startup.comexcase_api.domain.entities.ProviderEntity;
import com.startup.comexcase_api.domain.entities.PedidoEntity;
import com.startup.comexcase_api.domain.entities.enums.PedidoStatus;
import com.startup.comexcase_api.domain.repositories.IDealerRepository;
import com.startup.comexcase_api.domain.repositories.IPedidoRepository;
import com.startup.comexcase_api.usecases.DealerUsecases;
import com.startup.comexcase_api.usecases.PedidoUsecases;
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

import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
public class PedidoIT {
    @LocalServerPort
    private int port;
    @Autowired
    private IDealerRepository dealerRepository;
    @Autowired
    private IPedidoRepository pedidoRepository;
    @Autowired
    private DatabaseCleaner databaseCleaner;
    private PedidoDTO pedidoDTO;
    private DealerEntity currentDealerOne;
    private DealerEntity currentDealerTwo;
    private ProviderEntity currentProvider;
    private PedidoEntity currentPedido;
    private PedidoEntity currentPedidoDone;
    private String tokenDealer;
    private String tokenDealerProvider;

    @BeforeEach
    public void setUp() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        databaseCleaner.clearTables();
        prepareData();
    }

    private RequestSpecification buildConnection() {
        if (this.tokenDealer == null || this.tokenDealerProvider == null) {
            this.tokenDealerProvider = "Bearer " + GetToken.execute(
                    currentDealerOne.getEmail(),
                    "12345678",
                    port
            );
            this.tokenDealer = "Bearer " + GetToken.execute(
                    currentDealerTwo.getEmail(),
                    "12345678",
                    port
            );
        }

        return RestAssured
                .given()
                .basePath("/api/pedidos")
                .port(port)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON);
    }

    @Test
    public void should_return_status_201_when_create_pedido() {
        buildConnection()
                .headers("Authorization", tokenDealer)
                .body(pedidoDTO)
                .post()
                .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    public void should_return_status_404_when_create_pedido_cause_providerid_notfound() {
        pedidoDTO.setProviderId(UUID.randomUUID().toString());

        buildConnection()
                .headers("Authorization", tokenDealer)
                .body(pedidoDTO)
                .post()
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void should_return_status_400_when_create_pedido_cause_the_dealer_cannot_order_himself() {
        buildConnection()
                .headers("Authorization", tokenDealerProvider)
                .body(pedidoDTO)
                .post()
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void should_return_status_200_when_accept_pedido() {
        buildConnection()
                .headers("Authorization", tokenDealerProvider)
                .pathParam("pedidoId", currentPedido.getId())
                .patch("/accept/{pedidoId}")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void should_return_status_404_when_accept_pedido_cause_pedidoid_notfound() {
        buildConnection()
                .headers("Authorization", tokenDealerProvider)
                .pathParam("pedidoId", UUID.randomUUID())
                .patch("/accept/{pedidoId}")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void should_return_status_400_when_accept_pedido_cause_pedido_had_already_done() {
        buildConnection()
                .headers("Authorization", tokenDealerProvider)
                .pathParam("pedidoId", currentPedidoDone.getId())
                .patch("/accept/{pedidoId}")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void should_return_status_403_when_accept_purchase_cause_the_dealer_or_provider_does_not_have_permission_to_accept_this_pedido() {
        buildConnection()
                .headers("Authorization", tokenDealer)
                .pathParam("pedidoId", currentPedido.getId())
                .patch("/accept/{pedidoId}")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void should_return_status_200_when_unaccept_pedido() {
        buildConnection()
                .headers("Authorization", tokenDealerProvider)
                .pathParam("pedidoId", currentPedido.getId())
                .patch("/unaccept/{pedidoId}")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void should_return_status_404_when_unaccept_pedido_cause_pedidoid_notfound() {
        buildConnection()
                .headers("Authorization", tokenDealerProvider)
                .pathParam("pedidoId", UUID.randomUUID())
                .patch("/unaccept/{pedidoId}")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void should_return_status_400_when_unaccept_pedido_cause_pedido_had_already_done() {
        buildConnection()
                .headers("Authorization", tokenDealerProvider)
                .pathParam("pedidoId", currentPedidoDone.getId())
                .patch("/unaccept/{pedidoId}")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void should_return_status_403_when_unaccept_purchase_cause_the_dealer_or_provider_does_not_have_permission_to_uaccept_this_pedido() {
        buildConnection()
                .headers("Authorization", tokenDealer)
                .pathParam("pedidoId", currentPedido.getId())
                .patch("/accept/{pedidoId}")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void should_return_status_200_when_findallbydealer_pedido() {
        buildConnection()
                .headers("Authorization", tokenDealer)
                .get("/dealer")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void should_return_status_200_when_findallbyprovider_pedido() {
        buildConnection()
                .headers("Authorization", tokenDealerProvider)
                .get("/provider")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void should_return_status_404_when_findallbydealerid_purchase_cause_dealerid_notfound() {
        buildConnection()
                .pathParam("dealerId", UUID.randomUUID())
                .get("/dealer/{dealerId}")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    public void prepareData() {
        // CRIANDO UM REVENDEDOR/FORNECEDOR
        DealerEntity dealerEntity = DealerUsecases.createDealerOne();
        ProviderEntity providerEntity = ProviderUsecases.createProvider(dealerEntity);
        currentDealerOne = dealerRepository.save(dealerEntity);
        currentProvider = providerEntity;

        // CRIANDO UM REVENDEDOR
        dealerEntity = DealerUsecases.createDealerTwo();
        currentDealerTwo = dealerRepository.save(dealerEntity);

        // CRIANDO UM PEDIDO
        PedidoEntity pedidoEntity = PedidoUsecases.createPedido(dealerEntity, providerEntity);
        currentPedido = pedidoRepository.save(pedidoEntity);

        // CRIANDO UM PEDIDO JÁ REALIZADO
        pedidoEntity = PedidoUsecases.createPedido(dealerEntity, providerEntity);
        pedidoEntity.setPedidoStatus(PedidoStatus.DONE);
        currentPedidoDone = pedidoRepository.save(pedidoEntity);

        // CRIANDO UM PEDIDODTO
        pedidoDTO = new PedidoDTO();
        pedidoDTO.setProviderId(currentProvider.getId().toString());
    }
}
