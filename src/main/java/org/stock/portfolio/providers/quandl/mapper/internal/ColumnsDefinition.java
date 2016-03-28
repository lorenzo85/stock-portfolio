package org.stock.portfolio.providers.quandl.mapper.internal;

import org.stock.portfolio.providers.quandl.mapper.Mapper;
import org.stock.portfolio.stockcodehistory.StockCodeHistory;

import java.util.List;

public interface ColumnsDefinition extends Mapper<List<Object>, StockCodeHistory> {

    List<String> getSupportedColumnsNames();

}
