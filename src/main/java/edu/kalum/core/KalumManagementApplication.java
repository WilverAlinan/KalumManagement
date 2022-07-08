package edu.kalum.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class KalumManagementApplication implements CommandLineRunner {
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(KalumManagementApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        String password = "12345";
        System.out.println(passwordEncoder.encode(password));


    }
}
