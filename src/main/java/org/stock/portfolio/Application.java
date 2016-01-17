package org.stock.portfolio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.stock.portfolio.commons.HttpClient;
import reactor.Environment;
import reactor.bus.EventBus;


@SpringBootApplication(scanBasePackages = {"org.stock.portfolio.*"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    // TODO: Reactor specific configuration. Move this config in its own Java config class.
    @Bean
    public Environment env() {
        return Environment.initializeIfEmpty().assignErrorJournal();
    }

    @Bean
    public EventBus createEventBus(Environment env) {
        return EventBus.create(env, Environment.THREAD_POOL);
    }

    @Bean
    @Scope("prototype")
    public HttpClient httpClient() {
        return new HttpClient();
    }
}
