package org.stock.portfolio.events;

import org.stock.portfolio.domain.StockCode;

import java.util.Collection;
import java.util.Collections;

public class StockCodesIndexEvent extends AbstractStockCodesEvent {

    public static final String KEY = StockCodesIndexEvent.class.getSimpleName();

    public StockCodesIndexEvent(String marketId, Throwable e, Result success) {
        super(marketId, e, success);
    }

    public StockCodesIndexEvent(String marketId, Result result, Collection<StockCode> codes) {
        super(marketId, result, codes);
    }

    public StockCodesIndexEvent(String marketId, Result result, StockCode code) {
        super(marketId, result, Collections.singletonList(code));
    }
}
