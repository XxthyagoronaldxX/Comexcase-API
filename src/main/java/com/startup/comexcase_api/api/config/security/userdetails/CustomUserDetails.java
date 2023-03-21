package com.startup.comexcase_api.api.config.security.userdetails;

import com.startup.comexcase_api.domain.entities.DealerEntity;
import com.startup.comexcase_api.domain.entities.RoleEntity;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@ToString
public class CustomUserDetails implements UserDetails, Serializable {
    private final String username;
    private final String password;
    private final List<GrantedAuthority> authorities;
    private final boolean isEnable;

    public CustomUserDetails(String username, String password, List<RoleEntity> roles, boolean isEnable) {
        this.username = username;
        this.password = password;
        this.authorities = roles
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
        this.isEnable = isEnable;
    }

    public CustomUserDetails(DealerEntity dealer) {
        this.username = dealer.getEmail();
        this.password = dealer.getPassword();
        this.authorities = dealer
                .getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
        this.isEnable = !dealer.isDeleted();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.isEnable;
    }
}
