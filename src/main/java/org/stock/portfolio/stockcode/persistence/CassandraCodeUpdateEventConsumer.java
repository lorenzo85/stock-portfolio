package org.stock.portfolio.stockcode.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.stock.portfolio.stockcode.StockCodePersistenceRepository;
import reactor.bus.Event;
import reactor.bus.EventBus;
import reactor.fn.Consumer;

import javax.annotation.PostConstruct;

import static java.lang.String.format;
import static reactor.bus.selector.Selectors.$;

@Service
public class CassandraCodeUpdateEventConsumer implements Consumer<Event<StockCodesUpdateEvent>> {

    private final Logger LOG = LoggerFactory.getLogger(CassandraCodeUpdateEventConsumer.class);

    @Autowired
    private EventBus eventBus;
    @Autowired
    private StockCodePersistenceRepository repository;

    @PostConstruct
    public void onStartUp() {
        eventBus.on($(StockCodesUpdateEvent.KEY), this);
    }

    @Override
    public void accept(Event<StockCodesUpdateEvent> event) {
        StockCodesUpdateEvent updateEvent = event.getData();
        String marketId = updateEvent.getMarketId();

        if (updateEvent.failed()) {
            LOG.warn(format("Error while updating marketId=%s", marketId));

        } else {
            event.getData()
                    .getCodes()
                    .stream()
                    .forEach(code -> {
                        code.setMarketId(marketId);
                        repository.save(code);
                    });
        }
    }
}
