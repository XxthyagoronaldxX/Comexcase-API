package com.startup.comexcase_api.usecases;

import com.startup.comexcase_api.domain.entities.ItemPedidoEntity;
import com.startup.comexcase_api.domain.entities.PedidoEntity;
import com.startup.comexcase_api.domain.entities.ProductEntity;

public class ItemPedidoUsecases {
    public static ItemPedidoEntity createItemPedido(PedidoEntity pedido, ProductEntity product) {
        ItemPedidoEntity itemPedidoEntity = new ItemPedidoEntity();

        itemPedidoEntity.setPedido(pedido);
        itemPedidoEntity.setProduct(product);
        itemPedidoEntity.setAmount(5);
        itemPedidoEntity.setPrice(product.getPricePerUnit());

        return itemPedidoEntity;
    }
}
