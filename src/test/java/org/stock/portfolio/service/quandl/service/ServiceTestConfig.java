package org.stock.portfolio.service.quandl.service;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.stock.portfolio.service.commons.HttpClient;
import org.stock.portfolio.service.serialization.Deserializer;
import reactor.bus.EventBus;

@Configuration
public class ServiceTestConfig {

    @Bean
    @Primary
    public HttpClient httpClient() {
        return Mockito.mock(HttpClient.class);
    }

    @Bean
    @Primary
    public Deserializer stockCodeRepository() {
        return Mockito.mock(Deserializer.class);
    }

    @Bean
    @Primary
    public EventBus eventBus() {
        return Mockito.mock(EventBus.class);
    }


}
