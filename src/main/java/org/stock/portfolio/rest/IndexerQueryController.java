package org.stock.portfolio.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.FacetedPage;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.stock.portfolio.commons.indexer.TotalCountDto;
import org.stock.portfolio.stockcode.StockCodeIndexerService;
import org.stock.portfolio.stockcode.indexer.StockCodeDto;
import org.stock.portfolio.stockcodehistory.StockCodeHistoryIndexerService;
import org.stock.portfolio.stockcodehistory.indexer.StockCodeHistoryEntryDto;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;


@RestController
public class IndexerQueryController {

    @Autowired
    private StockCodeHistoryIndexerService historyIndexerService;
    @Autowired
    private StockCodeIndexerService stockCodeIndexerService;


    @RequestMapping(value = "/query/stock/codes/{size}/{page}", method = GET)
    @ResponseBody
    public FacetedPage<StockCodeDto> listAllStockCodes(@PathVariable("page") int page, @PathVariable("size") int size) {
        return stockCodeIndexerService.listAllStockCodes(page, size);
    }

    @RequestMapping(value = "/query/stock/codes/total", method = GET)
    @ResponseBody
    public TotalCountDto totalStockCodes() {
        return stockCodeIndexerService.countStockCodes();
    }

    @RequestMapping(value = "/query/stock/codes/search/{term}", method = GET)
    @ResponseBody
    public List<StockCodeDto> suggestStockCodes(@PathVariable("term") String term) {
        return stockCodeIndexerService.suggestStockCodes(term);
    }

    @RequestMapping(value = "/query/stock/codes/history/total", method = GET)
    @ResponseBody
    public TotalCountDto totalStockCodesHistory() {
        return historyIndexerService.countStockCodeHistoryEntries();
    }

    @RequestMapping(value = "/query/stock/codes/history/search/{term}", method = GET)
    @ResponseBody
    public List<StockCodeHistoryEntryDto> suggestStockCodeHistory(@PathVariable("term") String term) {
        return historyIndexerService.suggestStockCodeHistory(term);
    }

}
