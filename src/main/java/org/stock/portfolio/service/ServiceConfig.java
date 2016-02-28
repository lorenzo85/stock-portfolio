package org.stock.portfolio.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.stock.portfolio.events.ServiceExceptionEvent;
import org.stock.portfolio.events.StockCodeHistoryUpdateEvent;
import reactor.bus.Event;
import reactor.bus.EventBus;

import java.util.function.Consumer;

@Configuration
@EnableAsync
@ComponentScan(basePackages = "org.stock.portfolio.service.*")
public class ServiceConfig {

    @Bean
    public com.diffplug.common.base.Errors.Handling errorHandler(EventBus bus) {
        return com.diffplug.common.base.Errors.createHandling(new Publish(bus));
    }

    static class Publish implements Consumer<Throwable> {
        private final EventBus eventBus;

        public Publish(EventBus eventBus) {
            this.eventBus = eventBus;
        }

        @Override
        public void accept(Throwable throwable) {
            ServiceExceptionEvent event = new ServiceExceptionEvent(throwable);
            eventBus.notify(ServiceExceptionEvent.KEY, Event.wrap(event));
        }
    }
}
