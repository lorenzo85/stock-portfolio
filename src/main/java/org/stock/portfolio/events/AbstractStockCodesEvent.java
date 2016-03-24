package org.stock.portfolio.events;

import org.stock.portfolio.domain.StockCode;

import java.util.ArrayList;
import java.util.Collection;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.stock.portfolio.events.Event.Result.FAIL;
import static org.stock.portfolio.events.Event.Result.SUCCESS;

abstract class AbstractStockCodesEvent implements Event {

    private Throwable exception;

    private final Collection<StockCode> codes;
    private final String marketId;

    private Event.Result result;

    public AbstractStockCodesEvent(String marketId, Throwable e) {
        this(marketId, new ArrayList<>());
        this.exception = e;
        this.result = FAIL;
    }

    public AbstractStockCodesEvent(String marketId, Collection<StockCode> codes) {
        checkNotNull(marketId);
        checkNotNull(codes);

        this.codes = codes;
        this.result = SUCCESS;
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
        return result == FAIL;
    }
}
