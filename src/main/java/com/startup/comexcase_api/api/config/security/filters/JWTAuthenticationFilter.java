package com.startup.comexcase_api.api.config.security.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.startup.comexcase_api.api.config.security.userdetails.CustomUserDetailsDTO;
import com.startup.comexcase_api.domain.entities.DealerEntity;
import com.startup.comexcase_api.api.config.security.userdetails.CustomUserDetails;
import com.startup.comexcase_api.domain.exceptions.EntityNotFoundException;
import com.startup.comexcase_api.domain.exceptions.NoPermissionException;
import com.startup.comexcase_api.domain.repositories.IDealerRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.stream.Collectors;

@Component
@PropertySource("classpath:application.properties")
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final IDealerRepository dealerRepository;
    private static final int TOKEN_EXPIRE = 4_800_000;
    public static String TOKEN_ASSIGN;

    public JWTAuthenticationFilter(
            AuthenticationManager authenticationManager,
            IDealerRepository dealerRepository
    ) {
        this.authenticationManager = authenticationManager;
        this.dealerRepository = dealerRepository;
    }

    @Autowired
    private void setTokenAssign(@Value("${token.assign}") String tokenAssign) {
        TOKEN_ASSIGN = tokenAssign;
    }

    @Override
    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws AuthenticationException {
        try {
            CustomUserDetailsDTO customUserDetailsDTO = new ObjectMapper()
                    .readValue(request.getInputStream(), CustomUserDetailsDTO.class);

            DealerEntity dealerEntity = dealerRepository
                    .findByEmail(customUserDetailsDTO.getUsername())
                    .orElseThrow(() -> new EntityNotFoundException("Dealer not found by Email."));

            CustomUserDetails customUserDetails = new CustomUserDetails(
                    customUserDetailsDTO.getUsername(),
                    customUserDetailsDTO.getPassword(),
                    dealerEntity.getRoles(),
                    !dealerEntity.isDeleted() && dealerEntity.isEnabled()
            );

            if (!customUserDetails.isEnabled())  {
                throw new NoPermissionException("This account isn't confirmed yet.");
            }

            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    customUserDetails.getUsername(),
                    customUserDetails.getPassword(),
                    customUserDetails.getAuthorities()
            ));
        } catch (IOException e) {
            e.printStackTrace();
            throw new AuthenticationServiceException("Falha ao autenticar usuário!", e);
        }
    }

    @Override
    protected void unsuccessfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {
        System.out.println(failed.toString());

        super.unsuccessfulAuthentication(request, response, failed);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        CustomUserDetails customUserDetails = (CustomUserDetails) authResult.getPrincipal();

        String token = JWT.create()
                .withSubject(customUserDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + TOKEN_EXPIRE))
                .withClaim("authorities", customUserDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(Algorithm.HMAC512(TOKEN_ASSIGN));

        response.getWriter().write(token);
        response.getWriter().flush();
    }
}
