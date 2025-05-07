package org.example.teamshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class TeamShopApplication {
    public static void main(String[] args) {
        SpringApplication.run(TeamShopApplication.class, args);
    }

}