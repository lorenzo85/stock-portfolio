package org.stock.portfolio.indexer;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.stock.portfolio.indexer.dto.StockCodeDto;

public interface StockCodeIndex extends ElasticsearchRepository<StockCodeDto, String> {

}
