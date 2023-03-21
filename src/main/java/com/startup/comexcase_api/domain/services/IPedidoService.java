package com.startup.comexcase_api.domain.services;

import com.startup.comexcase_api.domain.dtos.PedidoDTO;
import com.startup.comexcase_api.domain.entities.PedidoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IPedidoService {
    PedidoEntity save(PedidoDTO pedidoDTO, String email);
    PedidoEntity accept(UUID pedidoId, String email);
    PedidoEntity unaccept(UUID pedidoId, String email);
    Page<PedidoEntity> findAll(Pageable pageable);
    Page<PedidoEntity> findAllByDealer(String email, Pageable pageable);
    Page<PedidoEntity> findAllByProvider(String email, Pageable pageable);
}
