package org.stock.portfolio.service.quandl.mapper;

import org.springframework.stereotype.Component;
import org.stock.portfolio.domain.StockCode;
import org.stock.portfolio.service.quandl.dto.StockCodeDto;


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
