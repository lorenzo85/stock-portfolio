package org.stock.portfolio.indexer.event.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.stock.portfolio.domain.StockCode;
import org.stock.portfolio.events.StockCodesIndexEvent;
import org.stock.portfolio.indexer.StockCodeIndex;
import org.stock.portfolio.indexer.dto.StockCodeDto;
import reactor.bus.Event;
import reactor.bus.EventBus;
import reactor.bus.selector.Selectors;
import reactor.fn.Consumer;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
public class StockCodeIndexEventConsumer implements Consumer<Event<StockCodesIndexEvent>> {

    private static final Logger LOG = LoggerFactory.getLogger(StockCodeIndexEventConsumer.class);

    @Autowired
    private EventBus eventBus;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private StockCodeIndex repository;

    @PostConstruct
    public void onStartUp() {
        eventBus.on(Selectors.$(StockCodesIndexEvent.KEY), this);
    }

    @Override
    public void accept(Event<StockCodesIndexEvent> event) {
        StockCodesIndexEvent updateEvent = event.getData();
        String marketId = updateEvent.getMarketId();

        if (updateEvent.failed()) {
            LOG.warn(format("Error while updating marketId=%s", marketId));

        } else {
            StockCodesIndexEvent data = event.getData();
            Collection<StockCode> codes = data.getCodes();
            codes.forEach(c -> c.setMarketId(data.getMarketId()));

            Collection<StockCodeDto> dtos = codes
                    .stream()
                    .map(code -> StockCodeDto.fromEntity(mapper, code))
                    .collect(Collectors.toList());

            repository.save(dtos);
        }
    }
}
