package org.stock.portfolio.events;

import org.stock.portfolio.domain.StockCode;

import java.util.Collection;

public class StockCodesUpdateEvent {

    public static final String KEY = StockCodesUpdateEvent.class.getSimpleName();

    private final Collection<StockCode> codes;

    public StockCodesUpdateEvent(Collection<StockCode> codes) {
        this.codes = codes;
    }

    public Collection<StockCode> getCodes() {
        return codes;
    }
}
