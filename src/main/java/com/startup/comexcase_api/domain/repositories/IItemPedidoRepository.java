package com.startup.comexcase_api.domain.repositories;

import com.startup.comexcase_api.domain.entities.ItemPedidoEntity;
import com.startup.comexcase_api.domain.entities.PedidoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IItemPedidoRepository extends JpaRepository<ItemPedidoEntity, UUID> {
    Optional<ItemPedidoEntity> findByPedidoIdAndProductId(UUID pedidoId, UUID productId);
    Page<ItemPedidoEntity> findAllByPedido(PedidoEntity pedido, Pageable pageable);
}
