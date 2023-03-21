package com.startup.comexcase_api.domain.repositories;

import com.startup.comexcase_api.domain.entities.DealerEntity;
import com.startup.comexcase_api.domain.entities.PedidoEntity;
import com.startup.comexcase_api.domain.entities.ProviderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IPedidoRepository extends JpaRepository<PedidoEntity, UUID> {
    Page<PedidoEntity> findAllByDealer(DealerEntity dealer, Pageable pageable);

    Page<PedidoEntity> findAllByProvider(ProviderEntity provider, Pageable pageable);
}
