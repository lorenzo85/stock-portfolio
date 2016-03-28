package org.stock.portfolio.config;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;


@Configuration
@PropertySource(value = {"classpath:META-INF/cassandra.properties"})
public class CassandraConfig {

    @Autowired
    private Environment environment;


    @Bean
    public Session session() throws Exception {
        String contactPoint = environment.getProperty("cassandra.contactpoints");
        int port = Integer.parseInt(environment.getProperty("cassandra.port"));
        Cluster cluster = Cluster.builder()
                .addContactPoint(contactPoint)
                .withPort(port)
                .build();
        return cluster.connect(environment.getProperty("cassandra.keyspace"));

    }

}

