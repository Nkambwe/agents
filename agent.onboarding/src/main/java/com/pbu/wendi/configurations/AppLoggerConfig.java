package com.pbu.wendi.configurations;

import com.pbu.wendi.utils.common.AppLoggerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppLoggerConfig {
    @Bean
    public AppLoggerService appLoggerService() {
        return new AppLoggerService();
    }
}
