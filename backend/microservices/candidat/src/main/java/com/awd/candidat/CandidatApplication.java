package com.awd.candidat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
public class CandidatApplication {

    public static void main(String[] args) {
        SpringApplication.run(CandidatApplication.class, args);
    }
}
