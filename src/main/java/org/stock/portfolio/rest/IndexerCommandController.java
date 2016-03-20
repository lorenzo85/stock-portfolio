package org.stock.portfolio.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.stock.portfolio.events.StockCodesIndexEvent;
import org.stock.portfolio.repository.StockCodeRepository;
import reactor.bus.Event;
import reactor.bus.EventBus;

import static org.stock.portfolio.events.Event.Result.SUCCESS;


@RestController
public class IndexerCommandController {

    @Autowired
    private StockCodeRepository cassandraRepository;
    @Autowired
    private EventBus eventBus;


    @RequestMapping(value = "/indexer/codes/reindex", method = RequestMethod.GET)
    @ResponseBody
    public void indexerReindexCodes() {
        cassandraRepository.findAll()
                .forEach(code -> {
                    StockCodesIndexEvent indexEvent = new StockCodesIndexEvent(SUCCESS, code);
                    eventBus.notify(StockCodesIndexEvent.KEY, Event.wrap(indexEvent));
                });
    }

}
