package org.stock.portfolio.service;

import org.stock.portfolio.domain.StockCode;

public interface StockService {

    void fetchStockCodes(String marketId);

    void fetchStockCodeHistory(String marketId, String dataset, String code);

    void fetchStockCodesHistory(String marketId, String dataset, String ...codes);

    void fetchAllStockCodeHistory(Iterable<StockCode> allCodes);
}
