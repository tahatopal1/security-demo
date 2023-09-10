package com.example.securitydemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Slf4j
public class BcryptGenerating {

    public static void main(String[] args) {
        generateBcrypt();
    }

    static void generateBcrypt() {
        String encoded = new BCryptPasswordEncoder().encode("password");
        System.out.println(encoded);
    }
}
