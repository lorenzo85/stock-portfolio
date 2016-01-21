package org.stock.portfolio.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.stock.portfolio.events.StockCodeHistoryUpdateEvent;
import reactor.bus.Event;
import reactor.bus.EventBus;
import reactor.fn.Consumer;

import javax.annotation.PostConstruct;

import static reactor.bus.selector.Selectors.$;

@Service
public class CassandraCodeEntriesUpdateEventConsumer implements Consumer<Event<StockCodeHistoryUpdateEvent>> {

    @Autowired
    private EventBus eventBus;
    @Autowired
    private StockTimeSeriesRepository repository;


    @PostConstruct
    public void onStartUp() {
        eventBus.on($(StockCodeHistoryUpdateEvent.KEY), this);
    }

    @Override
    public void accept(Event<StockCodeHistoryUpdateEvent> event) {
        event.getData()
                .getEntries()
                .stream()
                .forEach(repository::save);
    }
}
