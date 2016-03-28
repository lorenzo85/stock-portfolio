package org.stock.portfolio.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.stock.portfolio.commons.ServiceExceptionEvent;
import reactor.bus.Event;
import reactor.bus.EventBus;

import java.util.function.Consumer;

@Configuration
@EnableAsync
public class ServiceConfig {

    public static final int MAX_POOL_SIZE = 10;

    @Bean
    public SimpleAsyncTaskExecutor executor() {
        SimpleAsyncTaskExecutor executor = new SimpleAsyncTaskExecutor();
        executor.setConcurrencyLimit(MAX_POOL_SIZE);
        return executor;
    }

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
