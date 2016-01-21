package org.stock.portfolio.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.stock.portfolio.events.StockCodesUpdateEvent;
import reactor.bus.Event;
import reactor.bus.EventBus;
import reactor.fn.Consumer;

import javax.annotation.PostConstruct;

import static reactor.bus.selector.Selectors.$;

@Service
public class CassandraCodeUpdateEventConsumer implements Consumer<Event<StockCodesUpdateEvent>> {

    @Autowired
    private EventBus eventBus;
    @Autowired
    private StockCodeRepository repository;

    @PostConstruct
    public void onStartUp() {
        eventBus.on($(StockCodesUpdateEvent.KEY), this);
    }

    @Override
    public void accept(Event<StockCodesUpdateEvent> event) {
        event.getData()
                .getCodes()
                .stream()
                .forEach(repository::save);
    }
}
