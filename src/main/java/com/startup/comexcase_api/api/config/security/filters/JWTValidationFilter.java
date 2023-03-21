package com.startup.comexcase_api.api.config.security.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class JWTValidationFilter extends BasicAuthenticationFilter {
    public static final String HEADER_ATTR = "Authorization";
    public static final String ATTR_PREFIX = "Bearer ";

    public JWTValidationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
        String attr = request.getHeader(HEADER_ATTR);

        if (attr == null || !attr.startsWith(ATTR_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        String token = attr.replace(ATTR_PREFIX, "");

        UsernamePasswordAuthenticationToken authenticationToken = getAuthenticationToken(token);

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthenticationToken(String token) {
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(JWTAuthenticationFilter.TOKEN_ASSIGN)).build().verify(token);

        String username = decodedJWT.getSubject();

        if (username == null) {
            return null;
        }

        List<String> authoritiesList = decodedJWT.getClaim("authorities").asList(String.class);

        List<GrantedAuthority> grantedAuthorityList = authoritiesList.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
        
        return new UsernamePasswordAuthenticationToken(
                username,
                "",
                grantedAuthorityList
        );
    }
}
