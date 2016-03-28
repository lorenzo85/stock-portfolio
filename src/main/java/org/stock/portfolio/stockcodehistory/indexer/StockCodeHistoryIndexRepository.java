package org.stock.portfolio.stockcodehistory.indexer;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface StockCodeHistoryIndexRepository extends ElasticsearchRepository<StockCodeHistoryEntryDto, String> {


}
