package com.startup.comexcase_api.domain.services;

import com.startup.comexcase_api.domain.dtos.ItemPedidoDTO;
import com.startup.comexcase_api.domain.entities.ItemPedidoEntity;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IItemPedidoService {
    Iterable<ItemPedidoEntity> findAllByPedidoId(UUID pedidoId, String email, Pageable pageable);
    ItemPedidoEntity save(ItemPedidoDTO itemPedidoDTO, String email);
}
