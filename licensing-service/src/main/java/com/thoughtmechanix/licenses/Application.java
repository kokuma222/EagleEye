package com.thoughtmechanix.licenses;

import com.thoughtmechanix.licenses.utils.UserContextInterceptor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@EnableEurekaClient
@EnableFeignClients
@SpringCloudApplication
//@SpringBootApplication
//@EnableDiscoveryClient
//@EnableCircuitBreaker
@EnableResourceServer
public class Application {

    @LoadBalanced
    @Bean
    public OAuth2RestTemplate oAuth2RestTemplate(
            @Qualifier("oauth2ClientContext") OAuth2ClientContext oauth2ClientContext,
            OAuth2ProtectedResourceDetails details) {
        return new OAuth2RestTemplate(details, oauth2ClientContext);
    }

    /*@LoadBalanced
    @Bean
    public RestTemplate getRestTemplate() {
        RestTemplate template = new RestTemplate();
        List interceptors = template.getInterceptors();
        if (interceptors == null) {
            template.setInterceptors(Collections.singletonList(new UserContextInterceptor()));
        } else {
            interceptors.add(new UserContextInterceptor());
            template.setInterceptors(interceptors);
        }
        return template;
    }*/

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
