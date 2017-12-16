package com.alfred.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@ComponentScan
@SpringBootApplication
public class AlfredApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(AlfredApiApplication.class, args);
    }



}

