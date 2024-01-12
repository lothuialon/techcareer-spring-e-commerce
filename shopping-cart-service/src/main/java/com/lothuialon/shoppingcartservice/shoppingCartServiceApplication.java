package com.lothuialon.shoppingcartservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@SpringBootApplication
@EnableDiscoveryClient
public class shoppingCartServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(shoppingCartServiceApplication.class, args);
    }

}
