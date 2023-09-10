package com.example.securitydemo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.Charset;
import java.util.Base64;

@RestController
@RequestMapping("/login")
@Slf4j
public class LoginController {

    @GetMapping
    public ResponseEntity<Void> login(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Basic")) {
            String base64Credentials = authorizationHeader.substring("Basic".length()).trim();
            String credentials = new String(Base64.getDecoder().decode(base64Credentials), Charset.forName("UTF-8"));
            final String[] values = credentials.split(":", 2); // credentials = username:password
            String username = values[0];
            String password = values[1];
            log.info("Logging in... Username: {}, Password: {}", username, password);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
