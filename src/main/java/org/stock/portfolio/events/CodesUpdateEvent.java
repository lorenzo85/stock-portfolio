package org.stock.portfolio.events;

import org.stock.portfolio.domain.StockCode;

import java.util.Collection;

public class CodesUpdateEvent {

    public static final String KEY = CodesUpdateEvent.class.getSimpleName();

    private final Collection<StockCode> codes;

    public CodesUpdateEvent(Collection<StockCode> codes) {
        this.codes = codes;
    }

    public Collection<StockCode> getCodes() {
        return codes;
    }
}
