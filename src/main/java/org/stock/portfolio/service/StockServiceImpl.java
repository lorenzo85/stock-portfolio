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
import static org.stock.portfolio.events.Event.Result.FAIL;
import static org.stock.portfolio.events.Event.Result.SUCCESS;


@Service
public class StockServiceImpl implements StockService {

    @Autowired
    @Qualifier("quandl")
    private StockServiceProvider quandl;
    @Autowired
    private EventBus eventBus;

    @Async
    @Override
    public void fetchStockCodeHistory(String marketId, String code, String dataset) {
        checkNotNull(code);
        checkNotNull(dataset);
        checkNotNull(marketId);

        try {
            Collection<StockHistoryEntry> entries = quandl.fetchStockCodeHistory(marketId, code, dataset);
            StockCodeHistoryUpdateEvent event = new StockCodeHistoryUpdateEvent(marketId, code, SUCCESS, entries);
            eventBus.notify(StockCodeHistoryUpdateEvent.KEY, Event.wrap(event));

        } catch (ServiceException e) {
            StockCodeHistoryUpdateEvent event = new StockCodeHistoryUpdateEvent(marketId, code, FAIL);
            eventBus.notify(StockCodeHistoryUpdateEvent.KEY, Event.wrap(event));
        }
    }

    @Async
    @Override
    public void fetchStockCodesHistory(String marketId, String dataset, String ...codes) {
        checkNotNull(codes);
        checkNotNull(marketId);

        for (String code : codes) {
            try {
                Collection<StockHistoryEntry> entries = quandl.fetchStockCodeHistory(marketId, code, dataset);
                StockCodeHistoryUpdateEvent event = new StockCodeHistoryUpdateEvent(marketId, code, SUCCESS, entries);
                eventBus.notify(StockCodeHistoryUpdateEvent.KEY, Event.wrap(event));

            } catch (ServiceException e) {
                StockCodeHistoryUpdateEvent event = new StockCodeHistoryUpdateEvent(marketId, code, FAIL);
                eventBus.notify(StockCodeHistoryUpdateEvent.KEY, Event.wrap(event));
            }
        }
    }

    @Async
    @Override
    public void fetchStockCodes(String marketId) {
        checkNotNull(marketId);

        try {
            Collection<StockCode> codes = quandl.fetchStockCodes(marketId);
            StockCodesUpdateEvent event = new StockCodesUpdateEvent(marketId, SUCCESS, codes);
            eventBus.notify(StockCodesUpdateEvent.KEY, Event.wrap(event));

        } catch (ServiceException e) {
            StockCodesUpdateEvent event = new StockCodesUpdateEvent(marketId, FAIL);
            eventBus.notify(StockCodesUpdateEvent.KEY, Event.wrap(event));
        }
    }
}
