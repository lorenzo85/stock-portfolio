package org.stock.portfolio.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.FacetedPage;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.stock.portfolio.indexer.IndexerService;
import org.stock.portfolio.indexer.dto.StockCodeDto;
import org.stock.portfolio.indexer.dto.StockCodeHistoryEntryDto;
import org.stock.portfolio.indexer.dto.TotalCountDto;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;


@RestController
public class IndexerQueryController {

    @Autowired
    private IndexerService service;


    @RequestMapping(value = "/query/stock/codes/{size}/{page}", method = GET)
    @ResponseBody
    public FacetedPage<StockCodeDto> listAllStockCodes(@PathVariable("page") int page, @PathVariable("size") int size) {
        return service.listAllStockCodes(page, size);
    }

    @RequestMapping(value = "/query/stock/codes/total", method = GET)
    @ResponseBody
    public TotalCountDto totalStockCodes() {
        return service.countStockCodes();
    }

    @RequestMapping(value = "/query/stock/codes/history/total", method = GET)
    @ResponseBody
    public TotalCountDto totalStockCodesHistory() {
        return service.countStockCodeHistoryEntries();
    }

    @RequestMapping(value = "/query/stock/codes/search/{term}", method = GET)
    @ResponseBody
    public List<StockCodeDto> suggestStockCodes(@PathVariable("term") String term) {
        return service.suggestStockCodes(term);
    }

    @RequestMapping(value = "/query/stock/codes/history/search/{term}", method = GET)
    @ResponseBody
    public List<StockCodeHistoryEntryDto> findStockCodeHistoryByTerm(@PathVariable("term") String term) {
        return service.suggestStockCodeHistory(term);
    }

}
