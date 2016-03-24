package org.stock.portfolio.events;

import org.stock.portfolio.domain.StockHistoryEntry;

import java.util.Collection;

import static org.stock.portfolio.events.Event.Result.FAIL;
import static org.stock.portfolio.events.Event.Result.SUCCESS;

public class StockCodeHistoryIndexEvent implements Event {

    public static final String KEY = StockCodeHistoryIndexEvent.class.getSimpleName();

    private final Result result;
    private final Collection<StockHistoryEntry> entries;


    public StockCodeHistoryIndexEvent(Collection<StockHistoryEntry> entries) {
        this.result = SUCCESS;
        this.entries = entries;
    }

    public boolean failed() {
        return result == FAIL;
    }

    public Collection<StockHistoryEntry> getHistoryEntry() {
        return entries;
    }

}
