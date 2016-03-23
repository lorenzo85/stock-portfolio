package org.stock.portfolio.indexer;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.stock.portfolio.indexer.dto.StockCodeHistoryEntryDto;

public interface StockCodeHistoryIndex extends ElasticsearchRepository<StockCodeHistoryEntryDto, String> {


}
