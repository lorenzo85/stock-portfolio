package org.stock.portfolio.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.stock.portfolio.domain.StockCode;
import org.stock.portfolio.repository.StockCodeRepository;
import org.stock.portfolio.service.StockService;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.*;

@RestController
public class StockController {

    @Autowired
    private StockService stockService;
    @Autowired
    private StockCodeRepository repository;

    @RequestMapping(value = "/update/market/codes", method = RequestMethod.GET)
    @ResponseBody
    public void updateMarketCodes(@RequestParam(name = "marketid") String marketId) throws Exception {
        stockService.fetchStockCodes(marketId);
    }

    @RequestMapping(value = "/update/history/code", method = RequestMethod.GET)
    @ResponseBody
    public void updateHistoryCode(@RequestParam(name = "marketid") String marketId, @RequestParam(name = "code") String code) throws Exception {
        StockCode byMarketIdAndCode = repository.findByMarketIdAndCode(marketId, code);
        checkNotNull(byMarketIdAndCode, format("Could not find stock code for marketId=[%s] and code=[%s]", marketId, code));

        stockService.fetchStockCodeHistory(marketId, code, byMarketIdAndCode.getDataset());
    }

}
