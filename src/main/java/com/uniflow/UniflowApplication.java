package com.uniflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class UniflowApplication {
    public static void main(String[] args) {
        SpringApplication.run(UniflowApplication.class, args);
        System.out.println("🚀 UniFlow System Started Successfully!");
    }
}