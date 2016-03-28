package org.stock.portfolio.stockcodehistory.persistence;

import org.stock.portfolio.commons.events.Event;
import org.stock.portfolio.stockcodehistory.StockCodeHistory;

import java.util.ArrayList;
import java.util.Collection;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.stock.portfolio.commons.events.Event.Result.FAIL;
import static org.stock.portfolio.commons.events.Event.Result.SUCCESS;

public class StockCodeHistoryUpdateEvent implements Event {

    public static final String KEY = StockCodeHistoryUpdateEvent.class.getSimpleName();

    private Throwable exception;

    private final String code;
    private final String dataset;
    private final String marketId;
    private final Collection<StockCodeHistory> entries;

    private Result result;

    public StockCodeHistoryUpdateEvent(String marketId, String code, String dataset, Throwable e) {
        this(marketId, code, dataset, new ArrayList<>());
        this.exception = e;
        this.result = FAIL;
    }

    public StockCodeHistoryUpdateEvent(String marketId, String code, String dataset, Collection<StockCodeHistory> entries) {
        checkNotNull(marketId);
        checkNotNull(code);
        checkNotNull(entries);
        checkNotNull(dataset);

        this.code = code;
        this.entries = entries;
        this.dataset = dataset;
        this.marketId = marketId;
        this.result = SUCCESS;
    }

    public Collection<StockCodeHistory> getEntries() {
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
