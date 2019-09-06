package com.thoughtmechanix.organization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class NewVersionOfEagleEyeOrganizationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NewVersionOfEagleEyeOrganizationServiceApplication.class, args);
    }

}
