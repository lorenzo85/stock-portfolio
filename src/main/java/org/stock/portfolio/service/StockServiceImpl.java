package org.stock.portfolio.service;

import com.google.common.util.concurrent.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.stock.portfolio.domain.StockCode;
import org.stock.portfolio.domain.StockHistoryEntry;
import org.stock.portfolio.events.StockCodeHistoryIndexEvent;
import org.stock.portfolio.events.StockCodeHistoryUpdateEvent;
import org.stock.portfolio.events.StockCodesIndexEvent;
import org.stock.portfolio.events.StockCodesUpdateEvent;
import reactor.bus.Event;
import reactor.bus.EventBus;

import java.util.Collection;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;
import static org.stock.portfolio.events.Event.Result.FAIL;
import static org.stock.portfolio.events.Event.Result.SUCCESS;


@Service
public class StockServiceImpl implements StockService {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

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
                        StockCodesUpdateEvent storeEvent = new StockCodesUpdateEvent(marketId, throwable, FAIL);
                        eventBus.notify(StockCodesUpdateEvent.KEY, Event.wrap(storeEvent));

                        StockCodesIndexEvent indexEvent = new StockCodesIndexEvent(marketId, throwable, FAIL);
                        eventBus.notify(StockCodesIndexEvent.KEY, Event.wrap(indexEvent));
                    }

                    @Override
                    public void onSuccess(Collection<StockCode> codes) {
                        StockCodesUpdateEvent storeEvent = new StockCodesUpdateEvent(marketId, SUCCESS, codes);
                        eventBus.notify(StockCodesUpdateEvent.KEY, Event.wrap(storeEvent));

                        StockCodesIndexEvent indexEvent = new StockCodesIndexEvent(marketId, SUCCESS, codes);
                        eventBus.notify(StockCodesIndexEvent.KEY, Event.wrap(indexEvent));
                    }
                });
    }

    @Async
    @Override
    public void fetchStockCodeHistory(String marketId, String code, String dataset) {
        checkNotNull(code);
        checkNotNull(dataset);
        checkNotNull(marketId);

        executor.submitListenable(() -> quandl.fetchStockCodeHistory(marketId, code, dataset))
                .addCallback(new ListenableFutureCallback<Collection<StockHistoryEntry>>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        StockCodeHistoryUpdateEvent event = new StockCodeHistoryUpdateEvent(marketId, code, dataset, throwable, FAIL);
                        eventBus.notify(StockCodeHistoryUpdateEvent.KEY, Event.wrap(event));
                    }

                    @Override
                    public void onSuccess(Collection<StockHistoryEntry> entries) {
                        StockCodeHistoryUpdateEvent event = new StockCodeHistoryUpdateEvent(marketId, code, dataset, SUCCESS, entries);
                        eventBus.notify(StockCodeHistoryUpdateEvent.KEY, Event.wrap(event));

                        entries.forEach(historyEntry -> {
                            historyEntry.setCode(code);
                            historyEntry.setMarketId(marketId);
                        });

                        StockCodeHistoryIndexEvent indexEvent = new StockCodeHistoryIndexEvent(SUCCESS, entries);
                        eventBus.notify(StockCodeHistoryIndexEvent.KEY, Event.wrap(indexEvent));
                    }
                });
    }

    @Async
    @Override
    public void fetchAllStockCodeHistory(Iterable<StockCode> allCodes) {
        // Quandl limit: 2000 calls/10 minutes - 50000 calls / day
        final RateLimiter rateLimiter = RateLimiter.create(2.0); // rate is "2 permits per second"

        int count = 0;
        for (StockCode c : allCodes) {
            rateLimiter.acquire(); // may wait
            count++;

            String code = c.getCode();
            String dataset = c.getDataset();
            String markedId = c.getMarketId();

            LOG.info(format("Fetching stock code #%d, market=%s, code=%s, dataset=%s", count, markedId, code, dataset));

            fetchStockCodeHistory(markedId, code, dataset);
        }
    }
}
