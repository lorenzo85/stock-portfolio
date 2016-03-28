package org.stock.portfolio.stockcode.persistence;


import org.stock.portfolio.stockcode.AbstractStockCodesEvent;
import org.stock.portfolio.stockcode.StockCode;

import java.util.Collection;

public class StockCodesUpdateEvent extends AbstractStockCodesEvent {

    public static final String KEY = StockCodesUpdateEvent.class.getSimpleName();

    public StockCodesUpdateEvent(String marketId, Throwable e) {
        super(marketId, e);
    }

    public StockCodesUpdateEvent(String marketId, Collection<StockCode> codes) {
        super(marketId, codes);
    }
}
