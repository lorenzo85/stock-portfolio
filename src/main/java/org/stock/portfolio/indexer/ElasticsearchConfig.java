package org.stock.portfolio.indexer;

import org.elasticsearch.client.Client;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

@Configuration
@EnableElasticsearchRepositories(basePackages = "org.stock.portfolio.indexer.*")
public class ElasticsearchConfig {

    @Bean
    public Client elasticsearchClient() {
        return nodeBuilder().local(true).node().client();
    }

    @Bean
    public ElasticsearchOperations elasticsearchTemplate(Client client) {
        return new ElasticsearchTemplate(client);
    }
}
