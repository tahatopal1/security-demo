package com.example.securitydemo.config;

import com.example.securitydemo.filter.JWTTokenGeneratorFilter;
import com.example.securitydemo.filter.JWTTokenValidatorFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().csrf().disable()
                .addFilterBefore(new JWTTokenValidatorFilter(), BasicAuthenticationFilter.class)
                .addFilterAfter(new JWTTokenGeneratorFilter(), BasicAuthenticationFilter.class)
                .authorizeHttpRequests(registry ->
                        registry
                                .requestMatchers(HttpMethod.GET, "/student").hasAnyRole("USER", "ADMIN")
                                .requestMatchers(HttpMethod.POST, "/student").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/student").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/student").hasRole("ADMIN")
                                .anyRequest().authenticated()
                ).httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public UserDetailsService usersInMem() {
        UserDetails user1 = User.builder()
                .username("user")
                .password("$2a$10$HvEztSMq1TadFw4WS6Smw.3zw/n7I1DvPw.YOLhWTf4vDeWAwOnDi")
                .roles("USER")
                .build();
        UserDetails admin = User.builder()
                .username("admin")
                .password("$2a$10$HvEztSMq1TadFw4WS6Smw.3zw/n7I1DvPw.YOLhWTf4vDeWAwOnDi")
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(user1, admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
