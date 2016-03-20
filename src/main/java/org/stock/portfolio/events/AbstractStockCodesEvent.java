package org.stock.portfolio.events;

import org.stock.portfolio.domain.StockCode;

import java.util.ArrayList;
import java.util.Collection;

import static com.google.common.base.Preconditions.checkNotNull;

abstract class AbstractStockCodesEvent implements Event {

    private Throwable exception;

    private final Collection<StockCode> codes;
    private final String marketId;
    private final Event.Result result;

    public AbstractStockCodesEvent(String marketId, Throwable e, Event.Result success) {
        this(marketId, success, new ArrayList<>());
        this.exception = e;
    }

    public AbstractStockCodesEvent(String marketId, Event.Result result, Collection<StockCode> codes) {
        checkNotNull(marketId);
        checkNotNull(result);
        checkNotNull(codes);

        this.codes = codes;
        this.result = result;
        this.marketId = marketId;
    }

    public Collection<StockCode> getCodes() {
        return codes;
    }

    public String getMarketId() {
        return marketId;
    }

    public Event.Result getResult() {
        return result;
    }

    public Throwable getException() {
        return exception;
    }

    public boolean failed() {
        return result == Result.FAIL;
    }
}
