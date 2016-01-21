package org.stock.portfolio.service.quandl.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.stock.portfolio.commons.*;
import org.stock.portfolio.domain.StockCode;
import org.stock.portfolio.domain.StockHistoryEntry;
import org.stock.portfolio.repository.StockCodeRepository;
import org.stock.portfolio.serialization.Deserializer;
import org.stock.portfolio.service.StockServiceProvider;
import org.stock.portfolio.service.quandl.dto.StockCodeDto;
import org.stock.portfolio.service.quandl.dto.StockHistoryWrapperDto;
import org.stock.portfolio.service.quandl.mapper.StockCodeMapper;
import org.stock.portfolio.service.quandl.mapper.StockHistoryMapper;

import java.util.Collection;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;
import static java.util.stream.Collectors.*;


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
    private Deserializer deserializer;
    @Autowired
    private StockCodeMapper stockCodeMapper;
    @Autowired
    private StockHistoryMapper historyMapper;
    @Autowired
    private StockCodeRepository codeRepository;


    @Override
    public Collection<StockCode> fetchStockCodes(String marketId) {
        checkNotNull(marketId);

        String url = format(fetchDatabaseCodesURL, marketId);
        Collection<String> archiveFiles = downloadZipFileAndUnzip(url);

        return archiveFiles.stream()
                .filter(file -> file.endsWith(FileExtension.CSV.value()))
                .map(csvFile -> deserializer.deserialize(csvFile, StockCodeDto.class))
                .map(stockCodeMapper::mapAll)
                .flatMap(Collection::stream)
                .collect(toList());
    }

    @Override
    public Collection<StockHistoryEntry> fetchStockCodeHistory(String marketId, String code) {
        checkNotNull(code);
        checkNotNull(marketId);

        StockCode stockCode = codeRepository.findByMarketIdAndCode(marketId, code);
        checkNotNull(stockCode);

        String url = format(fetchCodeDataSerieURL, stockCode.getDataset(), stockCode.getCode(), quandlApiKey);

        StockHistoryWrapperDto dto;
        try {
            dto = client.get(url).bodyAsObject(StockHistoryWrapperDto.class);
        } catch (HttpClientException e) {
            // TODO: Send to error queue.
            throw new RuntimeException(e);
        }

        Collection<StockHistoryEntry> entries = historyMapper.map(dto.getStockHistoryDto());
        entries.stream()
                .forEach(entry -> {
                    entry.setCode(code);
                    entry.setMarketId(marketId);
                });

        return entries;
    }

    private Collection<String> downloadZipFileAndUnzip(String url) {
        try {
            return client.get(url).bodyAsZip().unzip();
        } catch (ZipFileException | HttpClientException e) {
            // TODO: Send to error queue.
            throw new RuntimeException(e);
        }
    }
}
