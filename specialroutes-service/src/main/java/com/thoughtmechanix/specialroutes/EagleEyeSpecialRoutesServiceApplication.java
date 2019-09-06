package com.thoughtmechanix.specialroutes;

import com.thoughtmechanix.specialroutes.utils.UserContextFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

import javax.servlet.Filter;

@SpringBootApplication
@EnableEurekaClient
public class EagleEyeSpecialRoutesServiceApplication {
    @Bean
    public Filter userContextFilter() {
        return new UserContextFilter();
    }

    public static void main(String[] args) {
        SpringApplication.run(EagleEyeSpecialRoutesServiceApplication.class, args);
    }

}
