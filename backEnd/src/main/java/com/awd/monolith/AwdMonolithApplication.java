package com.awd.monolith;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * AWD recruitment platform - monolithic version.
 * <p>
 * One application, one database, three business modules (packages):
 * candidat, job and candidature. Each package will become a microservice.
 */
@SpringBootApplication
public class AwdMonolithApplication {

    public static void main(String[] args) {
        SpringApplication.run(AwdMonolithApplication.class, args);
    }
}
