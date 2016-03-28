package org.stock.portfolio.stockcode.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.stock.portfolio.providers.StockServiceProvider;
import org.stock.portfolio.stockcode.StockCode;
import org.stock.portfolio.stockcode.StockCodeService;
import org.stock.portfolio.stockcode.indexer.StockCodesIndexEvent;
import org.stock.portfolio.stockcode.persistence.StockCodesUpdateEvent;
import reactor.bus.Event;
import reactor.bus.EventBus;

import java.util.Collection;

import static com.google.common.base.Preconditions.checkNotNull;

@Service
public class StockCodeServiceImpl implements StockCodeService {

    @Autowired
    private EventBus eventBus;
    @Autowired
    @Qualifier("quandl")
    private StockServiceProvider quandl;
    @Autowired
    private SimpleAsyncTaskExecutor executor;

    @Async
    @Override
    public void fetchStockCodes(String marketId) {
        checkNotNull(marketId);

        executor.submitListenable(() -> quandl.fetchStockCodes(marketId))
                .addCallback(new ListenableFutureCallback<Collection<StockCode>>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        StockCodesUpdateEvent storeEvent = new StockCodesUpdateEvent(marketId, throwable);
                        eventBus.notify(StockCodesUpdateEvent.KEY, Event.wrap(storeEvent));

                        StockCodesIndexEvent indexEvent = new StockCodesIndexEvent(marketId, throwable);
                        eventBus.notify(StockCodesIndexEvent.KEY, Event.wrap(indexEvent));
                    }

                    @Override
                    public void onSuccess(Collection<StockCode> codes) {
                        StockCodesUpdateEvent storeEvent = new StockCodesUpdateEvent(marketId, codes);
                        eventBus.notify(StockCodesUpdateEvent.KEY, Event.wrap(storeEvent));

                        StockCodesIndexEvent indexEvent = new StockCodesIndexEvent(marketId, codes);
                        eventBus.notify(StockCodesIndexEvent.KEY, Event.wrap(indexEvent));
                    }
                });
    }

}
