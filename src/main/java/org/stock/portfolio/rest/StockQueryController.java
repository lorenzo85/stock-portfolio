package org.stock.portfolio.rest;

import org.elasticsearch.action.suggest.SuggestResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class StockQueryController {

    @Autowired
    private StockCodeElasticSearchRepository repository;
    @Autowired
    private Client client;

    @RequestMapping(value = "/query/stock/codes/{size}/{page}", method = GET)
    @ResponseBody
    public FacetedPage<StockCodeDto> listAllStockCodes(@PathVariable("page") int page, @PathVariable("size") int size) {
        Pageable pageable = new PageRequest(page, size);
        QueryBuilder builder = QueryBuilders.matchAllQuery();
        return repository.search(builder, pageable);
    }

    @RequestMapping(value = "/query/stock/codes/search/{term}", method = GET)
    @ResponseBody
    public List<String> findStockCodeByTerm(@PathVariable("term") String term) {
        CompletionSuggestionBuilder builder = new CompletionSuggestionBuilder("stock-code").field("suggest").text(term).size(20);

        SuggestResponse suggestResponse = client.prepareSuggest(StockCodeDto.INDEX_NAME).addSuggestion(builder).execute().actionGet();
        Suggest.Suggestion<? extends Suggest.Suggestion.Entry<? extends Suggest.Suggestion.Entry.Option>> suggestion = suggestResponse.getSuggest().getSuggestion("stock-code");
        if (suggestion == null || suggestion.getEntries().isEmpty()) return new ArrayList<>();

        return suggestion
                .getEntries()
                .get(0)
                .getOptions()
                .stream()
                .map(option -> option.getText().string())
                .collect(Collectors.toList());
    }

}
