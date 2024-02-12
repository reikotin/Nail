package com.reiko.nail.config;

import java.net.http.HttpClient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import lombok.Data;

@Configuration
@Data
public class AppConfigu {

    @Bean
    public HttpClient httpClient() {
        return HttpClient.newHttpClient();
    }
    
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfig() {
    	return new PropertySourcesPlaceholderConfigurer();
    }
}
