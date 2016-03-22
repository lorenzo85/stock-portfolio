package org.stock.portfolio.indexer;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.stock.portfolio.indexer.dto.StockCodeDto;

public interface StockCodeElasticSearchRepository extends ElasticsearchRepository<StockCodeDto, String> {

}
