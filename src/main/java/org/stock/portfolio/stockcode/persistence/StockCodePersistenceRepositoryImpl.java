package org.stock.portfolio.stockcode.persistence;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.stock.portfolio.stockcode.StockCode;
import org.stock.portfolio.stockcode.StockCodePersistenceRepository;

import javax.annotation.PostConstruct;

@Service
public class StockCodePersistenceRepositoryImpl implements StockCodePersistenceRepository {

    @Autowired
    private Session session;

    private Mapper<StockCode> mapper;

    @PostConstruct
    public void postConstruct() {
        this.mapper = new MappingManager(session).mapper(StockCode.class);
    }

    @Override
    public StockCode findByMarketIdAndCode(String marketId, String code) {
        return mapper.get(marketId, code);
    }

    @Override
    public Iterable<StockCode> findAll() {
        ResultSet results = session.execute("SELECT * FROM codes");
        return mapper.map(results);
    }

    @Override
    public void save(StockCode code) {
        mapper.save(code);
    }
}
