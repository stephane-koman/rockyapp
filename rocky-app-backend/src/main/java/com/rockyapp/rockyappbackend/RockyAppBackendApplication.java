package com.rockyapp.rockyappbackend;

import com.rockyapp.rockyappbackend.permissions.dao.PermissionDAO;
import com.rockyapp.rockyappbackend.roles.dao.RoleDAO;
import com.rockyapp.rockyappbackend.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class RockyAppBackendApplication extends SpringBootServletInitializer {

    @Autowired
    RoleDAO roleDAO;

    @Autowired
    PermissionDAO permissionDAO;

    @Autowired
    UserService userService;

    public static void main(String[] args) {
        SpringApplication.run(RockyAppBackendApplication.class, args);
    }

    @Bean
    public BCryptPasswordEncoder getBCPE(){
        return new BCryptPasswordEncoder();
    }

}
