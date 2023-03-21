package com.startup.comexcase_api.domain.services.impl;

import com.startup.comexcase_api.domain.dtos.ItemPedidoDTO;
import com.startup.comexcase_api.domain.entities.ItemPedidoEntity;
import com.startup.comexcase_api.domain.entities.PedidoEntity;
import com.startup.comexcase_api.domain.entities.ProductEntity;
import com.startup.comexcase_api.domain.exceptions.EntityConflictException;
import com.startup.comexcase_api.domain.exceptions.EntityNotFoundException;
import com.startup.comexcase_api.domain.exceptions.InvalidArgumentException;
import com.startup.comexcase_api.domain.exceptions.NoPermissionException;
import com.startup.comexcase_api.domain.repositories.IItemPedidoRepository;
import com.startup.comexcase_api.domain.repositories.IPedidoRepository;
import com.startup.comexcase_api.domain.repositories.IProductRepository;
import com.startup.comexcase_api.domain.services.IItemPedidoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ItemPedidoService implements IItemPedidoService {
    private final IItemPedidoRepository itemPedidoRepository;
    private final IPedidoRepository pedidoRepository;
    private final IProductRepository productRepository;

    public ItemPedidoService(IItemPedidoRepository itemPedidoRepository, IPedidoRepository pedidoRepository, IProductRepository productRepository) {
        this.itemPedidoRepository = itemPedidoRepository;
        this.pedidoRepository = pedidoRepository;
        this.productRepository = productRepository;
    }

    @Override
    public Page<ItemPedidoEntity> findAllByPedidoId(UUID pedidoId, String email, Pageable pageable) {
        PedidoEntity pedidoEntity = pedidoRepository
                .findById(pedidoId)
                .orElseThrow(() -> new EntityNotFoundException("Pedido not found by id."));

        if (!pedidoEntity.getDealer().getEmail().equals(email) &&
                !pedidoEntity.getProvider().getDealer().getEmail().equals(email))
            throw new NoPermissionException("This dealer or provider don't have permission to see the products of this pedido.");

        return itemPedidoRepository.findAllByPedido(pedidoEntity, pageable);
    }

    @Override
    public ItemPedidoEntity save(ItemPedidoDTO itemPedidoDTO, String email) {
        PedidoEntity pedidoEntity = pedidoRepository
                .findById(UUID.fromString(itemPedidoDTO.getPedidoId()))
                .orElseThrow(() -> new EntityNotFoundException("Pedido not found by id."));

        if (!pedidoEntity.getDealer().getEmail().equals(email))
            throw new NoPermissionException("This dealer don't have permission to add a product to this pedido.");

        ProductEntity productEntity = productRepository
                .findById(UUID.fromString(itemPedidoDTO.getProductId()))
                .orElseThrow(() -> new EntityNotFoundException("Product not found by id."));

        itemPedidoRepository.findByPedidoIdAndProductId(pedidoEntity.getId(), productEntity.getId())
                .ifPresent(v -> {
                    throw new EntityConflictException("Product already exists in Pedido.");
                });

        if (itemPedidoDTO.getAmount() > productEntity.getAmount())
            throw new InvalidArgumentException("The amount is higher than the stock of this product.");

        ItemPedidoEntity itemPedidoEntity = new ItemPedidoEntity();

        itemPedidoEntity.setPedido(pedidoEntity);
        itemPedidoEntity.setProduct(productEntity);
        itemPedidoEntity.setAmount(itemPedidoDTO.getAmount());
        itemPedidoEntity.setPrice(productEntity.getPricePerUnit());

        return itemPedidoRepository.save(itemPedidoEntity);
    }
}
