package org.stock.portfolio.stockcode.indexer;


import org.stock.portfolio.stockcode.AbstractStockCodesEvent;
import org.stock.portfolio.stockcode.StockCode;

import java.util.Collection;

public class StockCodesIndexEvent extends AbstractStockCodesEvent {

    public static final String KEY = StockCodesIndexEvent.class.getSimpleName();

    public StockCodesIndexEvent(String marketId, Throwable e) {
        super(marketId, e);
    }

    public StockCodesIndexEvent(String marketId, Collection<StockCode> codes) {
        super(marketId, codes);
    }

}
