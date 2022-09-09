package com.rockyapp.rockyappbackend;

import com.rockyapp.rockyappbackend.roles.dao.RoleDAO;
import com.rockyapp.rockyappbackend.roles.entity.Role;
import com.rockyapp.rockyappbackend.users.dto.UserCreaDTO;
import com.rockyapp.rockyappbackend.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;

@SpringBootApplication
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class RockyAppBackendApplication extends SpringBootServletInitializer implements CommandLineRunner {

    @Autowired
    RoleDAO roleDAO;

    @Autowired
    UserService userService;

    public static void main(String[] args) {
        SpringApplication.run(RockyAppBackendApplication.class, args);
    }

    @Bean
    public BCryptPasswordEncoder getBCPE(){
        return new BCryptPasswordEncoder();
    }

    @Override
    public void run(String... args) throws Exception {
        Role role1 = new Role();
        role1.setName("ADMIN");

        roleDAO.save(role1);

        Role role2 = new Role();
        role2.setName("USER");

        roleDAO.save(role2);

        UserCreaDTO userCreaDTO = new UserCreaDTO();
        userCreaDTO.setName("toto");
        userCreaDTO.setUsername("toto");
        userCreaDTO.setEmail("toto@gmail.com");
        userCreaDTO.setPassword("aaaa");
        userCreaDTO.setPasswordConfirm("aaaa");
        userCreaDTO.setRole(role1.getId());

        userService.create(userCreaDTO);
    }
}
