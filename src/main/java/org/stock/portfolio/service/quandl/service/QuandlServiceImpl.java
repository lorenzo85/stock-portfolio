package org.stock.portfolio.service.quandl.service;


import com.diffplug.common.base.Errors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.stock.portfolio.domain.StockCode;
import org.stock.portfolio.domain.StockHistoryEntry;
import org.stock.portfolio.service.ServiceException;
import org.stock.portfolio.service.StockServiceProvider;
import org.stock.portfolio.service.commons.FileExtension;
import org.stock.portfolio.service.commons.HttpClient;
import org.stock.portfolio.service.quandl.dto.StockCodeDto;
import org.stock.portfolio.service.quandl.dto.StockHistoryWrapperDto;
import org.stock.portfolio.service.quandl.mapper.StockCodeMapper;
import org.stock.portfolio.service.quandl.mapper.StockHistoryMapper;
import org.stock.portfolio.service.serialization.Deserializer;

import java.util.Collection;
import java.util.Collections;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;
import static java.util.stream.Collectors.toList;


@Service("quandl")
public class QuandlServiceImpl implements StockServiceProvider {

    @Value("${quandl.api.key}")
    private String quandlApiKey;
    @Value("${quandl.fetch.database.codes}")
    private String fetchDatabaseCodesURL;
    @Value("${quandl.fetch.stockCode.data.series}")
    private String fetchCodeDataSerieURL;
    @Autowired
    private HttpClient client;
    @Autowired
    private Errors.Handling handler;
    @Autowired
    private Deserializer deserializer;
    @Autowired
    private StockCodeMapper stockCodeMapper;
    @Autowired
    private StockHistoryMapper historyMapper;


    @Override
    public Collection<StockCode> fetchStockCodes(String marketId) {
        checkNotNull(marketId);

        String url = format(fetchDatabaseCodesURL, marketId);
        return client.getAsZip(url)
                .unzip()
                .stream()
                .filter(file -> file.endsWith(FileExtension.CSV.value()))
                .map(handler.wrapWithDefault(csvFile -> deserializer.deserialize(csvFile, StockCodeDto.class), Collections.<StockCodeDto>emptyList()))
                .map(stockCodeMapper::mapAll)
                .flatMap(Collection::stream)
                .collect(toList());
    }

    @Override
    public Collection<StockHistoryEntry> fetchStockCodeHistory(String marketId, String code, String dataset) throws ServiceException {
        checkNotNull(code);
        checkNotNull(dataset);
        checkNotNull(marketId);

        String url = format(fetchCodeDataSerieURL, dataset, code, quandlApiKey);

        StockHistoryWrapperDto dto = client.getAsObject(url, StockHistoryWrapperDto.class);
        return historyMapper.map(dto.getStockHistoryDto());
    }

}
