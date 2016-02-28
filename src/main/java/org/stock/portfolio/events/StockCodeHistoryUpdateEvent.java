package org.stock.portfolio.events;

import org.stock.portfolio.domain.StockHistoryEntry;

import java.util.ArrayList;
import java.util.Collection;

import static com.google.common.base.Preconditions.checkNotNull;

public class StockCodeHistoryUpdateEvent implements Event {

    public static final String KEY = StockCodeHistoryUpdateEvent.class.getSimpleName();

    private final String code;
    private final Result result;
    private final String marketId;
    private final Collection<StockHistoryEntry> entries;

    public StockCodeHistoryUpdateEvent(String marketId, String code, Result result) {
        this(marketId, code, result, new ArrayList<>());
    }

    public StockCodeHistoryUpdateEvent(String marketId, String code, Result result, Collection<StockHistoryEntry> entries) {
        checkNotNull(marketId);
        checkNotNull(code);
        checkNotNull(result);
        checkNotNull(entries);

        this.code = code;
        this.result = result;
        this.entries = entries;
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

}
