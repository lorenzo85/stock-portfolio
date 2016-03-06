package org.stock.portfolio.service.quandl.mapper;

import org.springframework.stereotype.Component;
import org.stock.portfolio.domain.StockHistoryEntry;
import org.stock.portfolio.service.quandl.dto.StockHistoryDto;
import org.stock.portfolio.service.quandl.mapper.internal.ColumnsDefinition;
import org.stock.portfolio.service.quandl.mapper.internal.MapperDefinition1;
import org.stock.portfolio.service.quandl.mapper.internal.MapperDefinition2;
import org.stock.portfolio.service.quandl.mapper.internal.MapperDefinition3;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Component
public class StockHistoryMapper implements Mapper<StockHistoryDto, Collection<StockHistoryEntry>> {

    @Override
    public Collection<StockHistoryEntry> map(StockHistoryDto stockHistory) {
        Mapper<List<Object>, StockHistoryEntry> mapper = getMapper(stockHistory.getColumnNames());

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
