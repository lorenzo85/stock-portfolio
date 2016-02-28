package org.stock.portfolio.events;

import org.stock.portfolio.domain.StockCode;

import java.util.ArrayList;
import java.util.Collection;

import static com.google.common.base.Preconditions.checkNotNull;

public class StockCodesUpdateEvent implements Event {

    public static final String KEY = StockCodesUpdateEvent.class.getSimpleName();

    private final String marketId;
    private final Result result;
    private final Collection<StockCode> codes;

    public StockCodesUpdateEvent(String marketId, Result success) {
        this(marketId, success, new ArrayList<>());
    }

    public StockCodesUpdateEvent(String marketId, Result result, Collection<StockCode> codes) {
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

    public Result getResult() {
        return result;
    }
}
