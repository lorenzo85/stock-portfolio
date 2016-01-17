package org.stock.portfolio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.stock.portfolio.domain.StockCode;
import org.stock.portfolio.domain.StockHistoryEntry;
import org.stock.portfolio.events.CodeEntriesUpdateEvent;
import org.stock.portfolio.events.CodesUpdateEvent;
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
    public void updateStockCodeHistory(String marketId, String code) {
        checkNotNull(code);
        checkNotNull(marketId);

        //ExecutorService executor = Executors.newFixedThreadPool(10);
        //final RateLimiter rateLimiter = RateLimiter.create(1.0); // rate is "1 permits per second"
        Collection<StockHistoryEntry> entries = quandl.updateStockCodeHistory(marketId, code);

        CodeEntriesUpdateEvent event = new CodeEntriesUpdateEvent(entries);
        eventBus.notify(CodeEntriesUpdateEvent.KEY, Event.wrap(event));
    }

    @Async
    @Override
    public void updateStockCodes(String marketId) {
        checkNotNull(marketId);

        Collection<StockCode> codes = quandl.updateStockCodes(marketId);
        CodesUpdateEvent event = new CodesUpdateEvent(codes);
        eventBus.notify(CodesUpdateEvent.KEY, Event.wrap(event));
    }
}
