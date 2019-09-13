package com.thoughtmechanix.licenses.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * Created by Fency on 2019/8/17.
 */
@Component
@RefreshScope
public class ServiceConfig {

    @Value("${example.property}")
    private String exampleProperty;

    @Value("${signing.key}")
    private String jwtSigningKey;

    public String getExampleProperty() {
        return exampleProperty;
    }

    public String getJwtSigningKey() {
        return jwtSigningKey;
    }
}
