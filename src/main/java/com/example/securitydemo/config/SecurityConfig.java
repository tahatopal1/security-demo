package com.example.securitydemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

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
                //.password(passwordEncoder().encode("password"))
                .password("$2a$10$HvEztSMq1TadFw4WS6Smw.3zw/n7I1DvPw.YOLhWTf4vDeWAwOnDi")
                .roles("ADMIN")
                .and()
                .withUser("user")
                //.password(passwordEncoder().encode("password"))
                .password("$2a$10$HvEztSMq1TadFw4WS6Smw.3zw/n7I1DvPw.YOLhWTf4vDeWAwOnDi")
                .roles("USER");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
