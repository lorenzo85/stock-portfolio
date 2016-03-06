package org.stock.portfolio.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.stock.portfolio.events.StockCodesUpdateEvent;
import reactor.bus.Event;
import reactor.bus.EventBus;
import reactor.fn.Consumer;

import javax.annotation.PostConstruct;

import static java.lang.String.format;
import static org.stock.portfolio.events.Event.Result;
import static reactor.bus.selector.Selectors.$;

@Service
public class CassandraCodeUpdateEventConsumer implements Consumer<Event<StockCodesUpdateEvent>> {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

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
        StockCodesUpdateEvent updateEvent = event.getData();
        String marketId = updateEvent.getMarketId();

        if (updateEvent.getResult() == Result.FAIL) {
            LOG.warn(format("Error while updating marketId=%s", marketId));
            return;
        }

        event.getData()
                .getCodes()
                .stream()
                .forEach(c -> {
                    c.setMarketId(marketId);
                    repository.save(c);
                });
    }
}
