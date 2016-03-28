package org.stock.portfolio.stockcodehistory.indexer;

import org.stock.portfolio.commons.events.Event;
import org.stock.portfolio.stockcodehistory.StockCodeHistory;

import java.util.Collection;

import static org.stock.portfolio.commons.events.Event.Result.FAIL;
import static org.stock.portfolio.commons.events.Event.Result.SUCCESS;

public class StockCodeHistoryIndexEvent implements Event {

    public static final String KEY = StockCodeHistoryIndexEvent.class.getSimpleName();

    private final Result result;
    private final Collection<StockCodeHistory> entries;


    public StockCodeHistoryIndexEvent(Collection<StockCodeHistory> entries) {
        this.result = SUCCESS;
        this.entries = entries;
    }

    public boolean failed() {
        return result == FAIL;
    }

    public Collection<StockCodeHistory> getHistoryEntry() {
        return entries;
    }

}
