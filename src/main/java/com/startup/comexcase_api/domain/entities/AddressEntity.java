package com.startup.comexcase_api.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.UUID;
@Entity
@Table(name = "address")
@Getter @Setter @NoArgsConstructor @EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AddressEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    @EqualsAndHashCode.Include
    private UUID id;

    @Column(name = "zip_code", length = 10, nullable = false)
    private String zipCode;

    @Column(name = "state", length = 25, nullable = false)
    private String state;

    @Column(name = "country", length = 25, nullable = false)
    private String country;

    @Column(name = "neighborhood", length = 25, nullable = false)
    private String neighborhood;

    @Column(name = "house_number", length = 10, nullable = false)
    private String houseNumber;

    @Column(name = "city", length = 25, nullable = false)
    private String city;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Timestamp updatedAt;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "dealer_id", nullable = false)
    @JsonIgnore
    private DealerEntity dealer;
}
