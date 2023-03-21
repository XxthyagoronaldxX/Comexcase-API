package com.startup.comexcase_api.domain.services.impl;

import com.startup.comexcase_api.domain.dtos.PedidoDTO;
import com.startup.comexcase_api.domain.entities.DealerEntity;
import com.startup.comexcase_api.domain.entities.ProviderEntity;
import com.startup.comexcase_api.domain.entities.PedidoEntity;
import com.startup.comexcase_api.domain.entities.enums.PedidoStatus;
import com.startup.comexcase_api.domain.exceptions.EntityNotFoundException;
import com.startup.comexcase_api.domain.exceptions.InvalidArgumentException;
import com.startup.comexcase_api.domain.exceptions.NoPermissionException;
import com.startup.comexcase_api.domain.repositories.IDealerRepository;
import com.startup.comexcase_api.domain.repositories.IProviderRepository;
import com.startup.comexcase_api.domain.repositories.IPedidoRepository;
import com.startup.comexcase_api.domain.services.IPedidoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PedidoService implements IPedidoService {
    private final IPedidoRepository pedidoRepository;
    private final IDealerRepository dealerRepository;
    private final IProviderRepository providerRepository;

    public PedidoService(IPedidoRepository pedidoRepository, IDealerRepository dealerRepository, IProviderRepository providerRepository) {
        this.pedidoRepository = pedidoRepository;
        this.dealerRepository = dealerRepository;
        this.providerRepository = providerRepository;
    }

    @Override
    public PedidoEntity save(PedidoDTO pedidoDTO, String email) {
        UUID providerId = UUID.fromString(pedidoDTO.getProviderId());

        DealerEntity dealerEntity = dealerRepository
                .findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Dealer not found by PhoneNumber."));

        providerRepository
                .findByDealerEmail(email)
                .ifPresent(v -> {
                    throw new InvalidArgumentException("Dealer can't order by himself.");
                });

        ProviderEntity providerEntity = providerRepository
                .findById(providerId)
                .orElseThrow(() -> new EntityNotFoundException("Provider not found by id."));

        PedidoEntity pedidoEntity = new PedidoEntity();

        pedidoEntity.setDealer(dealerEntity);
        pedidoEntity.setProvider(providerEntity);

        return pedidoRepository.save(pedidoEntity);
    }

    @Override
    public PedidoEntity accept(UUID pedidoId, String email) {
        PedidoEntity pedidoEntity = pedidoRepository
                .findById(pedidoId)
                .orElseThrow(() -> new EntityNotFoundException("Pedido not found by id."));

        ProviderEntity providerEntity = providerRepository
                .findByDealerEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Provider not found by Phone Number."));

        if (!pedidoEntity.getProvider().getId().equals(providerEntity.getId()))
            throw new NoPermissionException("This provider don't have permission to accept this order.");

        if (PedidoStatus.DONE == pedidoEntity.getPedidoStatus())
            throw new InvalidArgumentException("The provider can't accept an order already done.");

        pedidoEntity.setPedidoStatus(PedidoStatus.ACCEPTED);

        return pedidoRepository.save(pedidoEntity);
    }

    @Override
    public PedidoEntity unaccept(UUID pedidoId, String email) {
        PedidoEntity pedidoEntity = pedidoRepository
                .findById(pedidoId)
                .orElseThrow(() -> new EntityNotFoundException("Pedido not found by id."));

        ProviderEntity providerEntity = providerRepository
                .findByDealerEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Provider not found by Phone Number."));

        if (!pedidoEntity.getProvider().getId().equals(providerEntity.getId()))
            throw new NoPermissionException("This provider don't have permission to unaccept this order.");

        if (PedidoStatus.DONE == pedidoEntity.getPedidoStatus())
            throw new InvalidArgumentException("The provider can't unaccept an order already done.");

        pedidoEntity.setPedidoStatus(PedidoStatus.UNACCEPTED);

        return pedidoRepository.save(pedidoEntity);
    }

    @Override
    public Page<PedidoEntity> findAll(Pageable pageable) {
        return pedidoRepository.findAll(pageable);
    }

    @Override
    public Page<PedidoEntity> findAllByDealer(String email, Pageable pageable) {
        DealerEntity dealerEntity = dealerRepository
                .findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Dealer not found by id."));

        return pedidoRepository.findAllByDealer(dealerEntity, pageable);
    }

    @Override
    public Page<PedidoEntity> findAllByProvider(String email, Pageable pageable) {
        ProviderEntity providerEntity = providerRepository
                .findByDealerEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Provider not found by id."));

        return pedidoRepository.findAllByProvider(providerEntity, pageable);
    }
}
