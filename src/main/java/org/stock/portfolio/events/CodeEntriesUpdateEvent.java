package org.stock.portfolio.events;

import org.stock.portfolio.domain.StockHistoryEntry;

import java.util.Collection;

public class CodeEntriesUpdateEvent {

    public static final String KEY = CodeEntriesUpdateEvent.class.getSimpleName();

    private final Collection<StockHistoryEntry> entries;

    public CodeEntriesUpdateEvent(Collection<StockHistoryEntry> entries) {
        this.entries = entries;
    }

    public Collection<StockHistoryEntry> getEntries() {
        return entries;
    }
}
