package com.startup.comexcase_api.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.startup.comexcase_api.domain.entities.enums.SalesUnit;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "product")
@SQLDelete(sql = "UPDATE product SET is_deleted = true WHERE id = ?")
@Getter @Setter @NoArgsConstructor @EqualsAndHashCode(onlyExplicitlyIncluded = true) @ToString
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    @EqualsAndHashCode.Include
    private UUID id;

    @Column(name = "title")
    private String title;

    @Column(name = "img_product_url")
    private String imgProductUrl;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "sales_unit")
    private SalesUnit salesUnit;

    @Column(name = "price_per_unit", precision = 2)
    private  double pricePerUnit;

    @Column(name = "amount")
    private int amount;

    @Column(name = "is_delivery")
    private boolean isDelivery;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Timestamp updatedAt;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    @OneToMany(mappedBy = "product")
    private List<ParticularityEntity> particularities;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "provider_id")
    @JsonIgnore
    private ProviderEntity provider;
}
