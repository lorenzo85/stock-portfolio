package org.stock.portfolio.rest;

import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.FacetedPage;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.stock.portfolio.indexer.StockCodeElasticSearchRepository;
import org.stock.portfolio.indexer.dto.StockCodeDto;
import org.stock.portfolio.indexer.dto.StockCodeSuggestionDto;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;


@RestController
public class IndexerQueryController {

    @Autowired
    private Client client;
    @Autowired
    private StockCodeElasticSearchRepository repository;


    @RequestMapping(value = "/query/stock/codes/{size}/{page}", method = GET)
    @ResponseBody
    public FacetedPage<StockCodeDto> allStockCodes(@PathVariable("page") int page, @PathVariable("size") int size) {
        QueryBuilder builder = QueryBuilders.matchAllQuery();
        Pageable pageable = new PageRequest(page, size);
        return repository.search(builder, pageable);
    }

    @RequestMapping(value = "/query/stock/codes/search/{term}", method = GET)
    @ResponseBody
    public List<StockCodeSuggestionDto> findStockCodeByTerm(@PathVariable("term") String term) {
        return StockCodeDto.completionSuggestByTerm(client, term);
    }

}
