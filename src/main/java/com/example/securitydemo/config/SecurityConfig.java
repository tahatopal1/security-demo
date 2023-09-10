package com.example.securitydemo.config;

import com.example.securitydemo.filter.JWTTokenGeneratorFilter;
import com.example.securitydemo.filter.JWTTokenValidatorFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(new JWTTokenValidatorFilter(), BasicAuthenticationFilter.class)
                .addFilterAfter(new JWTTokenGeneratorFilter(), BasicAuthenticationFilter.class)
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
