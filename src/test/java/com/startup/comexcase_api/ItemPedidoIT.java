package com.startup.comexcase_api;

import com.startup.comexcase_api.domain.dtos.ItemPedidoDTO;
import com.startup.comexcase_api.domain.entities.*;
import com.startup.comexcase_api.domain.entities.enums.SalesUnit;
import com.startup.comexcase_api.domain.repositories.IDealerRepository;
import com.startup.comexcase_api.domain.repositories.IItemPedidoRepository;
import com.startup.comexcase_api.domain.repositories.IPedidoRepository;
import com.startup.comexcase_api.domain.repositories.IProductRepository;
import com.startup.comexcase_api.usecases.*;
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
public class ItemPedidoIT {
    @LocalServerPort
    private int port;
    @Autowired
    private DatabaseCleaner databaseCleaner;
    @Autowired
    private IItemPedidoRepository itemPedidoRepository;
    @Autowired
    private IPedidoRepository pedidoRepository;
    @Autowired
    private IProductRepository productRepository;
    @Autowired
    private IDealerRepository dealerRepository;
    private ItemPedidoDTO itemPedidoDTO;
    private DealerEntity currentDealerOne;
    private DealerEntity currentDealerTwo;
    private DealerEntity currentDealerThree;
    private UUID currentProductAlreadyUsedId;
    private UUID currentProductId;
    private UUID currentPedidoId;
    private String tokenDealerProvider;
    private String tokenDealer;
    private String tokenRandomDealer;

    @BeforeEach
    public void setUp() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        databaseCleaner.clearTables();
        prepareData();
    }

    private RequestSpecification buildConnection() {
        if (this.tokenDealerProvider == null || this.tokenDealer == null || this.tokenRandomDealer == null) {
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
            this.tokenRandomDealer = "Bearer " + GetToken.execute(
                    currentDealerThree.getEmail(),
                    "12345678",
                    port
            );
        }

        return RestAssured.given()
                .basePath("/api/items-pedido")
                .port(port)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON);
    }

    @Test
    public void should_return_status_201_when_create_itempedido() {
        buildConnection()
                .headers("Authorization", tokenDealer)
                .body(itemPedidoDTO)
                .post()
                .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    public void should_return_status_404_when_create_itempedido_cause_pedidoid_notfound() {
        itemPedidoDTO.setPedidoId(UUID.randomUUID().toString());

        buildConnection()
                .headers("Authorization", tokenDealer)
                .body(itemPedidoDTO)
                .post()
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void should_return_status_403_when_create_itempedido_cause_the_current_user_does_not_have_permission_to_create_in_this_pedido() {
        buildConnection()
                .headers("Authorization", tokenDealerProvider)
                .body(itemPedidoDTO)
                .post()
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void should_return_status_400_when_create_itempedido_cause_amount_ishigherthan_limit() {
        itemPedidoDTO.setAmount(100);

        buildConnection()
                .headers("Authorization", tokenDealer)
                .body(itemPedidoDTO)
                .post()
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void should_return_status_404_when_create_itempedido_cause_productid_notfound() {
        itemPedidoDTO.setProductId(UUID.randomUUID().toString());

        buildConnection()
                .headers("Authorization", tokenDealer)
                .body(itemPedidoDTO)
                .post()
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void should_return_status_409_when_create_itempedido_cause_productid_already_in_use() {
        itemPedidoDTO.setProductId(currentProductAlreadyUsedId.toString());

        buildConnection()
                .headers("Authorization", tokenDealer)
                .body(itemPedidoDTO)
                .post()
                .then()
                .statusCode(HttpStatus.CONFLICT.value());
    }

    @Test
    public void should_return_status_200_when_findallbypedidoid_dealer() {
        buildConnection()
                .headers("Authorization", tokenDealer)
                .pathParam("pedidoId", currentPedidoId)
                .get("/pedido/{pedidoId}")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void should_return_status_200_when_findallbypedidoid_provider() {
        buildConnection()
                .headers("Authorization", tokenDealerProvider)
                .pathParam("pedidoId", currentPedidoId)
                .get("/pedido/{pedidoId}")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void should_return_status_403_when_findallbypedidoid_cause_this_dealer_does_not_have_permission_to_see_this_pedido() {
        buildConnection()
                .headers("Authorization", tokenRandomDealer)
                .pathParam("pedidoId", currentPedidoId)
                .get("/pedido/{pedidoId}")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @Test
    public void should_return_status_404_when_findallbypedidoid_cause_pedidoid_notfound() {
        buildConnection()
                .headers("Authorization", tokenDealer)
                .pathParam("pedidoId", UUID.randomUUID())
                .get("/pedido/{pedidoId}")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    public void prepareData() {
        // CRIANDO UM REVENDEDOR RANDOM
        DealerEntity dealerEntity = DealerUsecases.createDealerThree();
        currentDealerThree = dealerRepository.save(dealerEntity);

        // CRIANDO UM REVENDEDOR/FORNECEDOR COM DOIS PRODUTOS.
        dealerEntity = DealerUsecases.createDealerOne();
        ProviderEntity providerEntity = ProviderUsecases.createProvider(dealerEntity);
        currentDealerOne = dealerEntity = dealerRepository.save(dealerEntity);

        ProductEntity productEntity = ProductUsecases.createProduct(providerEntity);
        productEntity = productRepository.save(productEntity);
        currentProductAlreadyUsedId = productEntity.getId();

        ProductEntity productEntity2 = ProductUsecases.createProduct(providerEntity);
        productEntity2 = productRepository.save(productEntity2);
        currentProductId = productEntity2.getId();

        // CRIANDO UM REVENDEDOR QUE IRA ADQUIRIR OS PRODUTOS.
        dealerEntity = DealerUsecases.createDealerTwo();
        currentDealerTwo = dealerEntity = dealerRepository.save(dealerEntity);

        PedidoEntity pedidoEntity = PedidoUsecases.createPedido(dealerEntity, providerEntity);
        pedidoEntity = pedidoRepository.save(pedidoEntity);
        currentPedidoId = pedidoEntity.getId();

        ItemPedidoEntity itemPedidoEntity = ItemPedidoUsecases.createItemPedido(pedidoEntity, productEntity);
        itemPedidoRepository.save(itemPedidoEntity);

        itemPedidoDTO = new ItemPedidoDTO();
        itemPedidoDTO.setPedidoId(currentPedidoId.toString());
        itemPedidoDTO.setProductId(currentProductId.toString());
        itemPedidoDTO.setAmount(10);
    }
}
