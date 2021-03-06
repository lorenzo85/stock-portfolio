package org.stock.portfolio.stockcodehistory.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.stock.portfolio.stockcodehistory.StockCodeHistoryPersistenceRepository;
import reactor.bus.Event;
import reactor.bus.EventBus;
import reactor.fn.Consumer;

import javax.annotation.PostConstruct;

import static java.lang.String.format;
import static org.stock.portfolio.commons.events.Event.Result;
import static reactor.bus.selector.Selectors.$;

@Service
public class StockCodeHistoryUpdateEventConsumer implements Consumer<Event<StockCodeHistoryUpdateEvent>> {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private EventBus eventBus;
    @Autowired
    private StockCodeHistoryPersistenceRepository repository;


    @PostConstruct
    public void onStartUp() {
        eventBus.on($(StockCodeHistoryUpdateEvent.KEY), this);
    }

    @Override
    public void accept(Event<StockCodeHistoryUpdateEvent> event) {
        StockCodeHistoryUpdateEvent updateEvent = event.getData();
        String marketId = updateEvent.getMarketId();
        String dataset = updateEvent.getDataset();
        String code = updateEvent.getCode();

        if (updateEvent.getResult() == Result.FAIL) {
            Throwable e = updateEvent.getException();
            LOG.warn(format("Error while updating history for code=%s, marketId=%s, dataset=%s", marketId, code, dataset), e);
            return;
        }

        updateEvent.getEntries()
                .stream()
                .forEach(ch -> {
                    ch.setMarketId(marketId);
                    ch.setCode(code);
                    repository.save(ch);
                });
    }
}
