package org.stock.portfolio.events;

import org.stock.portfolio.domain.StockHistoryEntry;

import java.util.ArrayList;
import java.util.Collection;

import static com.google.common.base.Preconditions.checkNotNull;

public class StockCodeHistoryUpdateEvent implements Event {

    public static final String KEY = StockCodeHistoryUpdateEvent.class.getSimpleName();

    private Throwable exception;

    private final String code;
    private final Result result;
    private final String dataset;
    private final String marketId;
    private final Collection<StockHistoryEntry> entries;

    public StockCodeHistoryUpdateEvent(String marketId, String code, String dataset, Throwable e, Result result) {
        this(marketId, code, dataset, result, new ArrayList<>());
        this.exception = e;
    }

    public StockCodeHistoryUpdateEvent(String marketId, String code, String dataset, Result result, Collection<StockHistoryEntry> entries) {
        checkNotNull(marketId);
        checkNotNull(code);
        checkNotNull(result);
        checkNotNull(entries);
        checkNotNull(dataset);

        this.code = code;
        this.result = result;
        this.entries = entries;
        this.dataset = dataset;
        this.marketId = marketId;
    }

    public Collection<StockHistoryEntry> getEntries() {
        return entries;
    }

    public String getCode() {
        return code;
    }

    public Result getResult() {
        return result;
    }

    public String getMarketId() {
        return marketId;
    }

    public String getDataset() {
        return dataset;
    }

    public Throwable getException() {
        return exception;
    }
}
