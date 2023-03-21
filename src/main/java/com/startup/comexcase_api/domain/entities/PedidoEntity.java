package com.startup.comexcase_api.domain.entities;

import com.startup.comexcase_api.domain.entities.enums.PedidoStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "pedido")
@Getter @Setter @NoArgsConstructor @ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PedidoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    @EqualsAndHashCode.Include
    private UUID id;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "pedido_status")
    private PedidoStatus pedidoStatus = PedidoStatus.WAITING;

    @Column(name = "provider_ended")
    private boolean providerEnded = Boolean.FALSE;

    @Column(name = "dealer_ended")
    private boolean dealerEnded = Boolean.FALSE;

    @ManyToOne
    @JoinColumn(name = "dealer_id")
    private DealerEntity dealer;

    @ManyToOne
    @JoinColumn(name = "provider_id")
    private ProviderEntity provider;
}
