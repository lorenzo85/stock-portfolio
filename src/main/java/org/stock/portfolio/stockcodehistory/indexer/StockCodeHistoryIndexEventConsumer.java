package org.stock.portfolio.stockcodehistory.indexer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.stock.portfolio.stockcodehistory.StockCodeHistory;
import reactor.bus.Event;
import reactor.bus.EventBus;
import reactor.bus.selector.Selectors;
import reactor.fn.Consumer;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class StockCodeHistoryIndexEventConsumer implements Consumer<Event<StockCodeHistoryIndexEvent>> {

    private static final Logger LOG = LoggerFactory.getLogger(StockCodeHistoryIndexEventConsumer.class);

    @Autowired
    private EventBus eventBus;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private StockCodeHistoryIndexRepository repository;

    @PostConstruct
    public void onStartUp() {
        eventBus.on(Selectors.$(StockCodeHistoryIndexEvent.KEY), this);
    }

    @Override
    public void accept(Event<StockCodeHistoryIndexEvent> event) {
        StockCodeHistoryIndexEvent data = event.getData();

        if (data.failed()) {
            LOG.warn("Error while updating history.");

        } else {
            Collection<StockCodeHistory> entries = data.getHistoryEntry();

            Collection<StockCodeHistoryEntryDto> dtos = entries
                    .stream()
                    .map(historyEntry -> StockCodeHistoryEntryDto.fromEntity(mapper, historyEntry))
                    .collect(Collectors.toList());

            repository.save(dtos);
        }
    }
}
