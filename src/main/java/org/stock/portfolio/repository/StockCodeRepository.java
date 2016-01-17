package org.stock.portfolio.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.stock.portfolio.domain.StockCode;

public interface StockCodeRepository extends CassandraRepository<StockCode> {

    @Query("SELECT * FROM codes WHERE market_id=?0 AND code=?1 ALLOW FILTERING")
    StockCode findByMarketIdAndCode(String marketId, String code);

}
