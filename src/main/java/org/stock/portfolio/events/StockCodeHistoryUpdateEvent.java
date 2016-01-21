package org.stock.portfolio.events;

import org.stock.portfolio.domain.StockHistoryEntry;

import java.util.Collection;

public class StockCodeHistoryUpdateEvent {

    public static final String KEY = StockCodeHistoryUpdateEvent.class.getSimpleName();

    private final Collection<StockHistoryEntry> entries;

    public StockCodeHistoryUpdateEvent(Collection<StockHistoryEntry> entries) {
        this.entries = entries;
    }

    public Collection<StockHistoryEntry> getEntries() {
        return entries;
    }
}
