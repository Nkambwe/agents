package com.pbu.wendi.configurations;

import com.pbu.wendi.utils.exceptions.ApplicationExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExceptionHandlerConfig {
    @Bean
    public ApplicationExceptionHandler exceptionHandlerBean() {

        return new ApplicationExceptionHandler();
    }
}