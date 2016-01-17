package org.stock.portfolio.service;

import org.stock.portfolio.domain.StockCode;
import org.stock.portfolio.domain.StockHistoryEntry;

import java.util.Collection;

public interface StockServiceProvider {
    Collection<StockCode> updateStockCodes(String marketId);
    Collection<StockHistoryEntry> updateStockCodeHistory(String marketId, String code);
}
