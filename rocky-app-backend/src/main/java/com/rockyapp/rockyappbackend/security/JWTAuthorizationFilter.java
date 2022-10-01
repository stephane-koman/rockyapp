package com.rockyapp.rockyappbackend.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class JWTAuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {

        if (Arrays.asList("/api/v1/refreshToken", "/api/v1/version").contains(request.getServletPath())) {
            filterChain.doFilter(request, response);
        } else {
            String authorizationToken = request.getHeader(SecurityConstants.HEADER_STRING);

            if (authorizationToken == null || !authorizationToken.startsWith(SecurityConstants.TOKEN_PREFIX)) {
                filterChain.doFilter(request, response);
                return;
            }
            String jwtToken = authorizationToken.substring(7);
            Algorithm algo = Algorithm.HMAC256(SecurityConstants.SECRET);

            JWTVerifier jwtVerifier = JWT.require(algo).build();
            try {
                DecodedJWT decodedJWT = jwtVerifier.verify(jwtToken);
                String username = decodedJWT.getSubject();

            String[] permissions = decodedJWT.getClaim("permissions").asArray(String.class);

            Collection<GrantedAuthority> authorities = new ArrayList<>();

                for (String permission : permissions) {
                    authorities.add(new SimpleGrantedAuthority(permission));
                }

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                filterChain.doFilter(request, response);

            }catch (TokenExpiredException e) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            }
        }
    }
}
