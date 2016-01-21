package org.stock.portfolio.service;

import org.stock.portfolio.domain.StockCode;
import org.stock.portfolio.domain.StockHistoryEntry;

import java.util.Collection;

public interface StockServiceProvider {

    Collection<StockCode> fetchStockCodes(String marketId);

    Collection<StockHistoryEntry> fetchStockCodeHistory(String marketId, String code);

}
