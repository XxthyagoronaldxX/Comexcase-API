package com.startup.comexcase_api.domain.entities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "item_pedido")
@Getter @Setter @NoArgsConstructor @EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ItemPedidoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    @EqualsAndHashCode.Include
    private UUID id;

    @Column(name = "amount", nullable = false)
    private int amount;

    @Column(name = "price", nullable = false)
    private double price;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "ordered_id", nullable = false)
    private PedidoEntity pedido;
}
