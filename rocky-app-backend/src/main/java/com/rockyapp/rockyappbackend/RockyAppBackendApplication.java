package com.rockyapp.rockyappbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Properties;

@SpringBootApplication
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class RockyAppBackendApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(RockyAppBackendApplication.class, args);
    }

    @Bean
    public BCryptPasswordEncoder getBCPE(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    @ConditionalOnMissingBean(BuildProperties.class)
    public BuildProperties buildProperties() {
        return new BuildProperties(new Properties());
    }

}
