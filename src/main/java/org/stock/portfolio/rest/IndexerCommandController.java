package org.stock.portfolio.rest;

import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.IndicesAdminClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.stock.portfolio.events.StockCodesIndexEvent;
import org.stock.portfolio.repository.StockCodeRepository;
import reactor.bus.Event;
import reactor.bus.EventBus;

import static java.lang.String.format;
import static org.stock.portfolio.events.Event.Result.SUCCESS;


@RestController
public class IndexerCommandController {

    @Autowired
    private StockCodeRepository cassandraRepository;
    @Autowired
    private Client client;
    @Autowired
    private EventBus eventBus;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @RequestMapping(value = "/indexer/codes/reindex", method = RequestMethod.GET)
    @ResponseBody
    public void indexerReindexCodes() {
        cassandraRepository.findAll().forEach(code -> {
            String marketId = code.getMarketId();
            StockCodesIndexEvent indexEvent = new StockCodesIndexEvent(marketId, SUCCESS, code);
            eventBus.notify(StockCodesIndexEvent.KEY, Event.wrap(indexEvent));
        });
    }

    private void createIndex(Class<?> clazz) {
        boolean created = elasticsearchTemplate.createIndex(clazz);
        if (!created) throw new IllegalStateException(String.format("Could not create index for class=%s", clazz.getSimpleName()));
    }

    private void deleteIndex(String indexName) {
        IndicesAdminClient adminClient = client.admin().indices();
        DeleteIndexRequestBuilder builder = new DeleteIndexRequestBuilder(adminClient, indexName);
        DeleteIndexResponse response = builder.execute().actionGet();

        if (!response.isAcknowledged()) throw new IllegalStateException(format("Could not delete index=%s", indexName));
    }

    private boolean existsIndex(String indexName) {
        IndicesExistsResponse res = client.admin().indices().prepareExists(indexName).execute().actionGet();
        return res.isExists();
    }
}
