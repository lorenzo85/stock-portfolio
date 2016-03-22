package org.stock.portfolio.events;

import org.stock.portfolio.domain.StockHistoryEntry;

import java.util.Collection;

public class StockCodeHistoryIndexEvent implements Event {

    public static final String KEY = StockCodeHistoryIndexEvent.class.getSimpleName();

    private final Result result;
    private final Collection<StockHistoryEntry> entries;


    public StockCodeHistoryIndexEvent(Result result, Collection<StockHistoryEntry> entries) {
        this.result = result;
        this.entries = entries;
    }

    public boolean failed() {
        return result == Result.FAIL;
    }

    public Collection<StockHistoryEntry> getHistoryEntry() {
        return entries;
    }

}
