package org.stock.portfolio.service;

public interface StockService {

    void fetchStockCodes(String marketId);

    void fetchStockCodeHistory(String marketId, String dataset, String code);

    void fetchStockCodesHistory(String marketId, String dataset, String ...codes);

}
