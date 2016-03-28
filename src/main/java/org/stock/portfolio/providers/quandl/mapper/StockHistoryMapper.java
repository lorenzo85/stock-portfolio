package org.stock.portfolio.providers.quandl.mapper;

import org.springframework.stereotype.Component;
import org.stock.portfolio.providers.quandl.dto.StockCodeHistoryEntryDto;
import org.stock.portfolio.providers.quandl.mapper.internal.ColumnsDefinition;
import org.stock.portfolio.providers.quandl.mapper.internal.MapperDefinition1;
import org.stock.portfolio.providers.quandl.mapper.internal.MapperDefinition2;
import org.stock.portfolio.providers.quandl.mapper.internal.MapperDefinition3;
import org.stock.portfolio.stockcodehistory.StockCodeHistory;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Component
public class StockHistoryMapper implements Mapper<StockCodeHistoryEntryDto, Collection<StockCodeHistory>> {

    @Override
    public Collection<StockCodeHistory> map(StockCodeHistoryEntryDto stockHistory) {
        Mapper<List<Object>, StockCodeHistory> mapper = getMapper(stockHistory.getColumnNames());

        return stockHistory.getCodeHistoryEntries()
                .stream()
                .map(mapper::map)
                .collect(Collectors.toList());
    }

    private static final List<ColumnsDefinition> MAPPERS = Arrays.asList(
            new MapperDefinition1(),
            new MapperDefinition2(),
            new MapperDefinition3());

    public static ColumnsDefinition getMapper(List<String> columnsNames) {
        for (ColumnsDefinition definition : MAPPERS) {
            List<String> supportedColumnsNames = definition.getSupportedColumnsNames();
            if (supportedColumnsNames.equals(columnsNames)) return definition;
        }

        throw new IllegalArgumentException(format("No available mapper for columns names %s", columnsNames));
    }

}
