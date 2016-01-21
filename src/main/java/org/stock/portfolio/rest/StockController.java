package org.stock.portfolio.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.stock.portfolio.service.StockService;

@RestController
public class StockController {

    @Autowired
    private StockService stockService;

    @RequestMapping(value = "/update/market/codes", method = RequestMethod.GET)
    @ResponseBody
    public void updateMarketCodes(@RequestParam(name = "marketid") String marketId) throws Exception {
        stockService.fetchStockCodes(marketId);
    }

    @RequestMapping(value = "/update/history/code", method = RequestMethod.GET)
    @ResponseBody
    public void updateHistoryCode(@RequestParam(name = "marketid") String marketId, @RequestParam(name = "code") String code) throws Exception {
        stockService.fetchStockCodeHistory(marketId, code);
    }

}
