package com.pbu.wendi.configurations;

import com.pbu.wendi.utils.common.NetworkService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NetworkConfig {
    @Bean
    public NetworkService networkService() {
        return new NetworkService();
    }
}
