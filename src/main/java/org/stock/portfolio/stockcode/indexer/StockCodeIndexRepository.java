package org.stock.portfolio.stockcode.indexer;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface StockCodeIndexRepository extends ElasticsearchRepository<StockCodeDto, String> {

}
