package org.stock.portfolio.indexer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.stock.portfolio.indexer.dto.StockCodeDto;

import java.util.Collection;

public interface StockCodeElasticSearchRepository extends ElasticsearchRepository<StockCodeDto, String> {

    Collection<StockCodeDto> findByCodeStartingWith(String start);

    Page<StockCodeDto> findByCodeStartingWith(String start, Pageable pageable);
}
