package com.startup.comexcase_api.domain.entities;

import com.startup.comexcase_api.domain.entities.enums.Roles;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "role")
@Getter @Setter @NoArgsConstructor
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "name")
    private String name;

    private RoleEntity(String name) {
        this.name = name;
    }

    public static RoleEntity createDealerRole() {
        return new RoleEntity(Roles.DEALER.getValue());
    }

    public static RoleEntity createProviderRole() { return new RoleEntity(Roles.PROVIDER.getValue()); }
}
