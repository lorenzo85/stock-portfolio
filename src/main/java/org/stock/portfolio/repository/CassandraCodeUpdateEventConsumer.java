package org.stock.portfolio.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.stock.portfolio.events.CodesUpdateEvent;
import reactor.bus.Event;
import reactor.bus.EventBus;
import reactor.fn.Consumer;

import javax.annotation.PostConstruct;

import static reactor.bus.selector.Selectors.$;

@Service
public class CassandraCodeUpdateEventConsumer implements Consumer<Event<CodesUpdateEvent>> {

    @Autowired
    private EventBus eventBus;
    @Autowired
    private StockCodeRepository repository;

    @PostConstruct
    public void onStartUp() {
        eventBus.on($(CodesUpdateEvent.KEY), this);
    }

    @Override
    public void accept(Event<CodesUpdateEvent> event) {
        event.getData()
                .getCodes()
                .stream()
                .forEach(repository::save);
    }
}
