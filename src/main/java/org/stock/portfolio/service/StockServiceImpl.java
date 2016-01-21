package org.stock.portfolio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.stock.portfolio.domain.StockCode;
import org.stock.portfolio.domain.StockHistoryEntry;
import org.stock.portfolio.events.StockCodeHistoryUpdateEvent;
import org.stock.portfolio.events.StockCodesUpdateEvent;
import reactor.bus.Event;
import reactor.bus.EventBus;

import java.util.Collection;

import static com.google.common.base.Preconditions.checkNotNull;


@Service
public class StockServiceImpl implements StockService {

    @Autowired
    @Qualifier("quandl")
    private StockServiceProvider quandl;
    @Autowired
    private EventBus eventBus;

    @Async
    @Override
    public void fetchStockCodeHistory(String marketId, String code) {
        checkNotNull(code);
        checkNotNull(marketId);

        Collection<StockHistoryEntry> entries = quandl.fetchStockCodeHistory(marketId, code);

        StockCodeHistoryUpdateEvent event = new StockCodeHistoryUpdateEvent(entries);
        eventBus.notify(StockCodeHistoryUpdateEvent.KEY, Event.wrap(event));
    }

    @Async
    @Override
    public void fetchStockCodesHistory(String marketId, String ...codes) {
        checkNotNull(marketId);

        for (String code : codes) {
            Collection<StockHistoryEntry> entries = quandl.fetchStockCodeHistory(marketId, code);

            StockCodeHistoryUpdateEvent event = new StockCodeHistoryUpdateEvent(entries);
            eventBus.notify(StockCodeHistoryUpdateEvent.KEY, Event.wrap(event));
        }

    }

    @Async
    @Override
    public void fetchStockCodes(String marketId) {
        checkNotNull(marketId);

        Collection<StockCode> codes = quandl.fetchStockCodes(marketId);

        StockCodesUpdateEvent event = new StockCodesUpdateEvent(codes);
        eventBus.notify(StockCodesUpdateEvent.KEY, Event.wrap(event));
    }
}
