package org.stock.portfolio.indexer;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.stock.portfolio.indexer.dto.StockCodeHistoryEntryDto;

public interface ElasticsearchStockCodeHistoryRepository extends ElasticsearchRepository<StockCodeHistoryEntryDto, String> {


}
