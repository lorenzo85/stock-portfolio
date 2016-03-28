package org.stock.portfolio.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.stock.portfolio.stockcode.StockCode;
import org.stock.portfolio.stockcode.StockCodePersistenceRepository;
import org.stock.portfolio.stockcode.StockCodeService;
import org.stock.portfolio.stockcodehistory.StockCodeHistoryService;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;

@RestController
public class StockCommandController {

    @Autowired
    private StockCodeService stockService;
    @Autowired
    private StockCodeHistoryService stockCodeHistoryService;
    @Autowired
    private StockCodePersistenceRepository repository;

    @RequestMapping(value = "/update/market/codes", method = RequestMethod.GET)
    @ResponseBody
    public void updateMarketCodes(@RequestParam("marketid") String marketId) throws Exception {
        stockService.fetchStockCodes(marketId);
    }

    @RequestMapping(value = "/update/history/code", method = RequestMethod.GET)
    @ResponseBody
    public void updateHistoryCode(@RequestParam("marketid") String marketId, @RequestParam("code") String code) throws Exception {
        StockCode byMarketIdAndCode = repository.findByMarketIdAndCode(marketId, code);
        checkNotNull(byMarketIdAndCode, format("Could not find stock code for marketId=[%s] and code=[%s]", marketId, code));

        String dataset = byMarketIdAndCode.getDataset();
        stockCodeHistoryService.fetchStockCodeHistory(marketId, code, dataset);
    }

    @RequestMapping(value = "/update/history/code/all", method = RequestMethod.GET)
    @ResponseBody
    public void updateHistoryCode() {
        Iterable<StockCode> allCodes = repository.findAll();
        stockCodeHistoryService.fetchAllStockCodeHistory(allCodes);
    }

}
