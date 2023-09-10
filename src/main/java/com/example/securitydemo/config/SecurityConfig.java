package com.example.securitydemo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .authorizeRequests(authorize -> {
                    authorize
                            .antMatchers(HttpMethod.GET, "/student").hasAnyRole("USER", "ADMIN")
                            .antMatchers(HttpMethod.POST, "/student").hasRole("ADMIN")
                            .antMatchers(HttpMethod.PUT, "/student").hasRole("ADMIN")
                            .antMatchers(HttpMethod.DELETE, "/student").hasRole("ADMIN");
                } )
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .httpBasic();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("admin")
                .password("{noop}password")
                .roles("ADMIN")
                .and()
                .withUser("user")
                .password("{noop}password")
                .roles("USER");
    }


}
