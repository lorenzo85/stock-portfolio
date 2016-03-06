package org.stock.portfolio.service.quandl.mapper.internal;

import org.stock.portfolio.domain.StockHistoryEntry;
import org.stock.portfolio.service.quandl.mapper.Mapper;

import java.util.List;

public interface ColumnsDefinition extends Mapper<List<Object>, StockHistoryEntry> {

    List<String> getSupportedColumnsNames();

}
