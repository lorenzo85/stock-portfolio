package org.stock.portfolio.stockcodehistory;

import org.stock.portfolio.stockcode.StockCode;

public interface StockCodeHistoryService {

    void fetchStockCodeHistory(String marketId, String dataset, String code);

    void fetchAllStockCodeHistory(Iterable<StockCode> allCodes);
}
