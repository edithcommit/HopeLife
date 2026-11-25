
package com.clara.hopelife.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(MercadoPagoProperties.class)
public class MercadoPagoConfig {

    private final MercadoPagoProperties properties;

    @Bean
    public RestClient mercadoPagoRestClient(RestClient.Builder builder) {
        return builder
                .baseUrl(properties.getBaseUrl())
                .defaultHeader(
                        "Authorization",
                        "Bearer " + properties.getAccessToken()
                )
                .defaultHeader(
                        "Content-Type",
                        "application/json"
                )
                .build();
    }
}
