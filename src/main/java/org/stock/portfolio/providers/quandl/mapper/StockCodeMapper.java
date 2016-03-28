package org.stock.portfolio.providers.quandl.mapper;

import org.springframework.stereotype.Component;
import org.stock.portfolio.providers.quandl.dto.StockCodeDto;
import org.stock.portfolio.stockcode.StockCode;


@Component
public class StockCodeMapper implements Mapper<StockCodeDto, StockCode> {

    @Override
    public StockCode map(StockCodeDto dto) {
        StockCode stockCode = new StockCode();
        stockCode.setCode(dto.getCode());
        stockCode.setDataset(dto.getDataset());
        stockCode.setDescription(dto.getDescription());
        return stockCode;
    }

}
