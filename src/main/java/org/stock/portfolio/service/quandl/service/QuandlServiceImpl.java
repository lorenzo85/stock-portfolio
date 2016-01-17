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
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;


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
    private StockCodeMapper codeMapper;
    @Autowired
    private StockHistoryMapper historyMapper;
    @Autowired
    private StockCodeRepository codeRepository;



    @Override
    public Collection<StockCode> updateStockCodes(String marketId) {
        checkNotNull(marketId);

        String url = format(fetchDatabaseCodesURL, marketId);
        try {

            Collection<StockCode> codes = client.get(url)
                    .bodyAsZip()
                    .unzip()
                    .stream()
                    .filter(file -> file.endsWith(FileExtension.CSV.value()))
                    .map(csvFile -> deserializer.deserialize(new FileReader(csvFile), StockCodeDto.class))
                    .map(codeMapper::mapAll)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());
            codes.stream().forEach(code -> code.setMarketId(marketId));

            return codes;

        } catch (ZipFileException | HttpClientException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Collection<StockHistoryEntry> updateStockCodeHistory(String marketId, String code) {
        checkNotNull(code);
        checkNotNull(marketId);

        // TODO: Need to get rid of code repository dependency. Dataset is specific to quandl.
        StockCode stockCode = codeRepository.findByMarketIdAndCode(marketId, code);
        checkNotNull(stockCode);

        String url = format(fetchCodeDataSerieURL, stockCode.getDataset(), stockCode.getCode(), quandlApiKey);

        StockHistoryWrapperDto dto;
        try {
            dto = client.get(url).bodyAsObject(StockHistoryWrapperDto.class);
        } catch (HttpClientException e) {
            throw new RuntimeException(e);
        }

        Collection<StockHistoryEntry> entries = historyMapper.map(dto.getStockHistoryDto());
        entries.stream().forEach(entry -> {
            entry.setCode(code);
            entry.setMarketId(marketId);
        });

        return entries;
    }
}
