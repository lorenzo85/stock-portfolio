package org.stock.portfolio.events;

import org.stock.portfolio.domain.StockCode;

import java.util.Collection;

public class StockCodesUpdateEvent extends AbstractStockCodesEvent {

    public static final String KEY = StockCodesUpdateEvent.class.getSimpleName();

    public StockCodesUpdateEvent(String marketId, Throwable e, Result success) {
        super(marketId, e, success);
    }

    public StockCodesUpdateEvent(String marketId, Result result, Collection<StockCode> codes) {
        super(marketId, result, codes);
    }
}
