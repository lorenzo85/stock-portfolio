package org.stock.portfolio.stockcode;

public interface StockCodePersistenceRepository {

    StockCode findByMarketIdAndCode(String marketId, String code);

    Iterable<StockCode> findAll();

    void save(StockCode code);
}
