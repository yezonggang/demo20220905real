package com.quick.start.demo.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JsonWebTokenProperty {
    private String used;
    private String secret;
    private Long expiration;
    private String header;
    private Long refresh_expiration;
}
