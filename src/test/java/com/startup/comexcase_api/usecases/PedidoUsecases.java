package com.startup.comexcase_api.usecases;

import com.startup.comexcase_api.domain.entities.DealerEntity;
import com.startup.comexcase_api.domain.entities.PedidoEntity;
import com.startup.comexcase_api.domain.entities.ProviderEntity;
import com.startup.comexcase_api.domain.entities.enums.PedidoStatus;

public class PedidoUsecases {
    public static PedidoEntity createPedido(DealerEntity dealer, ProviderEntity provider) {
        PedidoEntity pedidoEntity = new PedidoEntity();

        pedidoEntity.setDealer(dealer);
        pedidoEntity.setProvider(provider);
        pedidoEntity.setPedidoStatus(PedidoStatus.INPROGRESS);

        return pedidoEntity;
    }
}
