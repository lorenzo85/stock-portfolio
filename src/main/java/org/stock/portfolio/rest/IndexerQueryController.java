package org.stock.portfolio.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.stock.portfolio.indexer.ElasticsearchStockCodeHistoryRepository;
import org.stock.portfolio.indexer.StockCodeElasticSearchRepository;
import org.stock.portfolio.indexer.dto.*;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;


@RestController
public class IndexerQueryController {

    @Autowired
    private Client client;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private StockCodeElasticSearchRepository repository;
    @Autowired
    private ElasticsearchStockCodeHistoryRepository historyRepository;


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
        return StockCodeDto.completionSuggestByTerm(mapper, client, term);
    }

    @RequestMapping(value = "/query/stock/codes/total", method = GET)
    @ResponseBody
    public TotalCountDto totalStockCodes() {
        FacetedPage<StockCodeDto> page = allStockCodes(0, 1);
        return new TotalCountDto(page.getTotalElements());
    }

    @RequestMapping(value = "/query/stock/codes/history/search/{term}", method = GET)
    @ResponseBody
    public List<StockCodeHistorySuggestionDto> findStockCodeHistoryByTerm(@PathVariable("term") String term) {
        return StockCodeHistoryEntryDto.completionSuggestByTerm(mapper, client, term);
    }

    @RequestMapping(value = "/query/stock/codes/history/total", method = GET)
    @ResponseBody
    public TotalCountDto totalStockCodesHistory() {
        QueryBuilder builder = QueryBuilders.matchAllQuery();
        Pageable pageable = new PageRequest(0, 1);
        FacetedPage<StockCodeHistoryEntryDto> search = historyRepository.search(builder, pageable);
        return new TotalCountDto(search.getTotalElements());
    }

}
