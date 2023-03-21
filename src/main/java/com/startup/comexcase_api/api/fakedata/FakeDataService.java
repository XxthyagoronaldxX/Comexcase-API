package com.startup.comexcase_api.api.fakedata;

import com.startup.comexcase_api.domain.entities.*;
import com.startup.comexcase_api.domain.entities.enums.PedidoStatus;
import com.startup.comexcase_api.domain.entities.enums.SalesUnit;
import com.startup.comexcase_api.domain.repositories.*;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FakeDataService {
    private final IDealerRepository dealerRepository;
    private final IAddressRepository addressRepository;
    private final IProductRepository productRepository;
    private final IPedidoRepository orderedRepository;
    private final IItemPedidoRepository itemOrderedRepository;
    private final PasswordEncoder passwordEncoder;

    public FakeDataService(IDealerRepository dealerRepository, IAddressRepository addressRepository, IProductRepository productRepository, IPedidoRepository orderedRepository, IItemPedidoRepository itemOrderedRepository, PasswordEncoder passwordEncoder) {
        this.dealerRepository = dealerRepository;
        this.addressRepository = addressRepository;
        this.productRepository = productRepository;
        this.orderedRepository = orderedRepository;
        this.itemOrderedRepository = itemOrderedRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void fakeDataInit() {
        // CRIA UM REVENDEDOR
        DealerEntity dealerEntity = new DealerEntity();

        dealerEntity.setName("Thyago Ronald");
        dealerEntity.setNickname("TCKUBIRIM");
        dealerEntity.setEmail("thyagoronald8@gmail.com");
        dealerEntity.setPhoneNumber("(+55) 91 98284-3054");
        dealerEntity.setPassword(passwordEncoder.encode("12345678"));
        dealerEntity.setDeleted(false);
        dealerEntity.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        dealerEntity.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
        dealerEntity.getRoles().add(RoleEntity.createDealerRole());

        dealerEntity = dealerRepository.save(dealerEntity);

        // CRIA UM SEGUNDO REVENDEDOR
        DealerEntity dealerEntity_2 = new DealerEntity();

        dealerEntity_2.setName("Silvia Cristina");
        dealerEntity_2.setNickname("Moranguinho");
        dealerEntity_2.setEmail("silviamss6538@gmail.com");
        dealerEntity_2.setPhoneNumber("(+55) 91 98554-3054");
        dealerEntity_2.setPassword(passwordEncoder.encode("12345678"));
        dealerEntity_2.setDeleted(false);
        dealerEntity_2.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        dealerEntity_2.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
        dealerEntity_2.getRoles().add(RoleEntity.createDealerRole());

        dealerEntity_2 = dealerRepository.save(dealerEntity_2);

        // CRIA UM ENDEREÇO
        AddressEntity addressEntity = new AddressEntity();

        addressEntity.setZipCode("66670-000");
        addressEntity.setHouseNumber("59");
        addressEntity.setCountry("Brasil");
        addressEntity.setState("Pará");
        addressEntity.setCity("Belém");
        addressEntity.setNeighborhood("Coqueiro");
        addressEntity.setDealer(dealerEntity);

        addressRepository.save(addressEntity);
        dealerEntity = dealerRepository.save(dealerEntity);

        // CRIA UM FORNECEDOR
        ProviderEntity providerEntity = new ProviderEntity();

        dealerEntity.setProvider(providerEntity);
        dealerEntity.getRoles().add(RoleEntity.createProviderRole());

        dealerEntity = dealerRepository.save(dealerEntity);

        // ADICIONA UM PRODUTO
        ProductEntity productEntity = new ProductEntity();

        productEntity.setTitle("Açai");
        productEntity.setDelivery(false);
        productEntity.setAmount(20);
        productEntity.setSalesUnit(SalesUnit.L);
        productEntity.setDescription("Açai de qualidade, amazonia 100%.");
        productEntity.setPricePerUnit(16.00);
        productEntity.setProvider(dealerEntity.getProvider());

        productEntity = productRepository.save(productEntity);
        dealerEntity = dealerRepository.save(dealerEntity);

        // GERA UM PEDIDO
        PedidoEntity pedidoEntity = new PedidoEntity();

        pedidoEntity.setPedidoStatus(PedidoStatus.WAITING);
        pedidoEntity.setDealer(dealerEntity_2);
        pedidoEntity.setProvider(dealerEntity.getProvider());
        pedidoEntity.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));

        pedidoEntity = orderedRepository.save(pedidoEntity);

        // GERA UM ITEM PARA O PEDIDO
        ItemPedidoEntity itemPedidoEntity = new ItemPedidoEntity();

        itemPedidoEntity.setProduct(productEntity);
        itemPedidoEntity.setPedido(pedidoEntity);
        itemPedidoEntity.setAmount(10);
        itemPedidoEntity.setPrice(25.0);

        itemOrderedRepository.save(itemPedidoEntity);
    }
}
