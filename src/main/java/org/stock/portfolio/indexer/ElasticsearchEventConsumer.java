package org.stock.portfolio.indexer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.stock.portfolio.domain.StockCode;
import org.stock.portfolio.events.StockCodesUpdateEvent;
import org.stock.portfolio.indexer.dto.StockCodeDto;
import reactor.bus.Event;
import reactor.bus.EventBus;
import reactor.bus.selector.Selectors;
import reactor.fn.Consumer;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class ElasticsearchEventConsumer implements Consumer<Event<StockCodesUpdateEvent>> {

    @Autowired
    private EventBus eventBus;
    @Autowired
    private StockCodeElasticSearchRepository repository;

    @PostConstruct
    public void onStartUp() {
        eventBus.on(Selectors.$(StockCodesUpdateEvent.KEY), this);
    }

    @Override
    public void accept(Event<StockCodesUpdateEvent> event) {
        StockCodesUpdateEvent data = event.getData();
        Collection<StockCode> codes = data.getCodes();
        codes.forEach(c -> c.setMarketId(data.getMarketId()));

        Collection<StockCodeDto> dtos = codes
                .stream()
                .map(StockCodeDto::new)
                .collect(Collectors.toList());

        repository.save(dtos);
    }
}
