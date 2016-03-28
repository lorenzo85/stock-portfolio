package org.stock.portfolio.providers;

import org.stock.portfolio.stockcode.StockCode;
import org.stock.portfolio.stockcodehistory.StockCodeHistory;

import java.util.Collection;

public interface StockServiceProvider {

    Collection<StockCode> fetchStockCodes(String marketId);

    Collection<StockCodeHistory> fetchStockCodeHistory(String marketId, String code, String dataset);

}
