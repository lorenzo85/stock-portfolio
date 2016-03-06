package org.stock.portfolio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.stock.portfolio.service.commons.HttpClient;
import reactor.Environment;
import reactor.bus.EventBus;


@SpringBootApplication(scanBasePackages = {"org.stock.portfolio.*"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
