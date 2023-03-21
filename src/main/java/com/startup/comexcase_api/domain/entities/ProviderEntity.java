package com.startup.comexcase_api.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "provider")
@SQLDelete(sql = "UPDATE provider SET is_deleted = true WHERE id = ?")
@Getter @Setter @NoArgsConstructor @EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ProviderEntity {
    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "img_logo_url")
    private String imgLogoUrl;

    @Column(name = "img_background_url")
    private String imgBackgroudUrl;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    @OneToOne(mappedBy = "provider", fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = "provider")
    private DealerEntity dealer;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "provider", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<ProductEntity> products = new ArrayList<>();
}
