package com.rockyapp.rockyappbackend.common.controller;

import com.rockyapp.rockyappbackend.common.exception.InvalidTokenException;
import com.rockyapp.rockyappbackend.security.SecurityConstants;
import com.rockyapp.rockyappbackend.users.entity.User;
import com.rockyapp.rockyappbackend.users.service.UserService;
import com.rockyapp.rockyappbackend.utils.helpers.TokenHelper;
import lombok.AllArgsConstructor;
import org.springframework.boot.info.BuildProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1")
public class CommonController {

    private UserService userService;
    private BuildProperties buildProperties;

    @GetMapping("/refreshToken")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws InvalidTokenException, IOException {
        String authToken = request.getHeader(SecurityConstants.HEADER_STRING);
        if(authToken == null || !authToken.startsWith(SecurityConstants.TOKEN_PREFIX))
            throw new InvalidTokenException("refresh");

        try {
            String username = TokenHelper.extractUsernameFromToken(authToken);

            User user = userService.findByUsernameOrEmail(username);
            List<String> authorities = TokenHelper.getUserPermissions(user);

            String jwtAccessToken = TokenHelper.generateJwtAccessToken(username, authorities, request);

            String jwtRefreshToken = TokenHelper.generateJwtRefreshToken(username, request);

            TokenHelper.writeTokenInHttpResponse(jwtAccessToken, jwtRefreshToken, response);

        } catch (Exception e) {
            TokenHelper.writeTokenErrorInHttpResponse(response, e);
        }
    }

    @GetMapping("/version")
    public String getAppVersion(){
        return buildProperties.getVersion();
    }
}
