package com.startup.comexcase_api.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "dealer")
@SQLDelete(sql = "UPDATE dealer SET is_deleted = true, email = '' WHERE id = ?")
@Getter @Setter @NoArgsConstructor @ToString @EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class DealerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    @EqualsAndHashCode.Include
    private UUID id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "nickname", nullable = false, length = 25)
    private String nickname;

    @Column(name = "email", nullable = false, length = 400, unique = true)
    private String email;

    @Column(name = "img_background_url")
    private String imgBackgroundUrl;

    @Column(name = "img_profile_url")
    private String imgProfileUrl;

    @Column(name = "phone_number", length = 20, nullable = false, unique = true)
    private String phoneNumber;

    @Column(name = "password", length = 100, nullable = false)
    @JsonIgnore
    private String password;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Timestamp updatedAt;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = Boolean.FALSE;

    @Column(name = "is_verified", nullable = false)
    private boolean isVerified = Boolean.FALSE;

    @Column(name = "is_enabled", nullable = false)
    private boolean isEnabled = Boolean.FALSE;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "provider_id", referencedColumnName = "id")
    @JsonIgnoreProperties(value = "dealer")
    private ProviderEntity provider;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<RoleEntity> roles = new ArrayList<>();
}

