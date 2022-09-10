package com.rockyapp.rockyappbackend.security;

import com.rockyapp.rockyappbackend.users.entity.User;
import com.rockyapp.rockyappbackend.users.exception.UserNotFoundException;
import com.rockyapp.rockyappbackend.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserService userService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(new UserDetailsService() {
                    @Override
                    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
                        User user = null;
                        try {
                            user = userService.findUserByUsernameOrEmail(name);
                        } catch (UserNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                        Collection<GrantedAuthority> authorities = new ArrayList<>();

                        user.getPermissions().forEach(p -> authorities.add(new SimpleGrantedAuthority(p.getName())));
                        user.getRoles().forEach(r -> r.getPermissions().forEach(p -> authorities.add(new SimpleGrantedAuthority(p.getName()))));

                        Set<GrantedAuthority> authoritiesSet = new LinkedHashSet<>(authorities);
                        authorities.clear();

                        authorities.addAll(authoritiesSet);

                        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
                    }
                });
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        final JWTAuthenticationFilter filter = new JWTAuthenticationFilter(authenticationManager());
        filter.setFilterProcessesUrl("/v1/auth");

        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.headers().frameOptions().disable();
        http.authorizeRequests().antMatchers("/index.html", "/ajax/**", "/angtrans/**", "/bower_components/**", "/css/**", "/fonts/**", "/webapp/**", "/images/**", "/img/**", "/js/**", "/less/**", "/rtl/**", "/scripts/**", "/styles/**", "/views/**").permitAll();
        http.authorizeRequests().antMatchers("/v1/auth/**", "/v1/refreshToken/**").permitAll();
        http.authorizeRequests().anyRequest().authenticated();
        http.addFilter(filter);
        http.addFilter(new JWTAuthenticationFilter(authenticationManagerBean()));
        http.addFilterBefore(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

}
