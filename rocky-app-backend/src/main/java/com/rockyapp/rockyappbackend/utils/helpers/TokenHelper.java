package com.rockyapp.rockyappbackend.utils.helpers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rockyapp.rockyappbackend.security.SecurityConstants;
import com.rockyapp.rockyappbackend.users.entity.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

public class TokenHelper {
    private static final Algorithm ALGORITHM = Algorithm.HMAC256(SecurityConstants.SECRET);
    private static final String HTTP_HEADER = "error-message";

    public static String extractUsernameFromToken(String token){
        String refreshToken = token.substring(7);

        JWTVerifier jwtVerifier = JWT.require(ALGORITHM).build();
        DecodedJWT decodedJWT = jwtVerifier.verify(refreshToken);
        return decodedJWT.getSubject();
    }

    public static List<String> getUserPermissions(User user){
        List<String> authorities = new ArrayList<>();

        user.getPermissions().forEach(p -> authorities.add(p.getName()));
        user.getRoles().forEach(r -> r.getPermissions().forEach(p -> authorities.add(p.getName())));

        Set<String> authoritiesSet = new LinkedHashSet<>(authorities);
        authorities.clear();

        authorities.addAll(authoritiesSet);
        return authorities;
    }

    public static String generateJwtAccessToken(String username, List<String> authorities, HttpServletRequest request){
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME_ACCESS))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("permissions", authorities)
                .sign(ALGORITHM);
    }

    public static String generateJwtRefreshToken(String username, HttpServletRequest request){
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME_REFRESH))
                .withIssuer(request.getRequestURL().toString())
                .sign(ALGORITHM);
    }

    public static void writeTokenInHttpResponse(String jwtAccessToken, String jwtRefreshToken, HttpServletResponse response) throws IOException {
        Map<String, String> idToken = new HashMap<>();
        idToken.put("access-token", jwtAccessToken);
        idToken.put("refresh-token", jwtRefreshToken);

        response.setContentType("application/json");
        new ObjectMapper().writeValue(response.getOutputStream(), idToken);
    }

    public static void writeTokenErrorInHttpResponse(HttpServletResponse response, Exception e) throws IOException {
        response.setHeader(HTTP_HEADER, e.getMessage());
        response.sendError(HttpServletResponse.SC_FORBIDDEN);
    }
}
