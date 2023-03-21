package com.startup.comexcase_api.api.config.security.userdetails;

import com.startup.comexcase_api.domain.entities.DealerEntity;
import com.startup.comexcase_api.domain.repositories.IDealerRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final IDealerRepository dealerRepository;

    public CustomUserDetailsService(IDealerRepository dealerRepository) {
        this.dealerRepository = dealerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<DealerEntity> dealerEntityOptional = dealerRepository.findByEmail(email);

        return new CustomUserDetails(dealerEntityOptional.orElse(new DealerEntity()));
    }
}
