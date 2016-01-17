package org.stock.portfolio.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.stock.portfolio.domain.StockHistoryEntry;

public interface StockTimeSeriesRepository extends CassandraRepository<StockHistoryEntry> {
}
