package org.stock.portfolio.stockcodehistory.persistence;

import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.stock.portfolio.stockcodehistory.StockCodeHistory;
import org.stock.portfolio.stockcodehistory.StockCodeHistoryPersistenceRepository;

import javax.annotation.PostConstruct;

@Service
public class StockCodeHistoryPersistenceRepositoryImpl implements StockCodeHistoryPersistenceRepository {

    @Autowired
    private Session session;

    private Mapper<StockCodeHistory> mapper;

    @PostConstruct
    public void postConstruct() {
        this.mapper = new MappingManager(session).mapper(StockCodeHistory.class);
    }

    @Override
    public void save(StockCodeHistory entry) {
        mapper.save(entry);
    }
}
