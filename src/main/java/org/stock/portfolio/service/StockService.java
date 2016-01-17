package org.stock.portfolio.service;

public interface StockService {
    void updateStockCodes(String marketId);
    void updateStockCodeHistory(String marketId, String code);
}
