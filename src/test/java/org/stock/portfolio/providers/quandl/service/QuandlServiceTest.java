package org.stock.portfolio.providers.quandl.service;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.stock.portfolio.commons.ServiceExceptionEvent;
import org.stock.portfolio.config.ReactorConfig;
import org.stock.portfolio.config.ServiceConfig;
import org.stock.portfolio.providers.quandl.commons.Deserializer;
import org.stock.portfolio.providers.quandl.commons.HttpClient;
import org.stock.portfolio.providers.quandl.commons.HttpClientException;
import org.stock.portfolio.providers.quandl.commons.ZipFile;
import org.stock.portfolio.providers.quandl.dto.StockCodeDto;
import org.stock.portfolio.stockcode.StockCode;
import reactor.bus.Event;
import reactor.bus.EventBus;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.mockito.Mockito.*;
import static org.stock.portfolio.AssertionUtils.assertThatAreRefEquals;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {ServiceConfig.class, ReactorConfig.class, ServiceTestConfig.class})
public class QuandlServiceTest {

    @Autowired Deserializer deserializer;
    @Autowired QuandlServiceImpl quandl;
    @Autowired HttpClient httpClient;
    @Autowired EventBus eventBus;

    List<StockCodeDto> dtos = asList(
            newStockCodeDto("WIKI", "AAL", "A description"),
            newStockCodeDto("WIKI", "BBA", "Another description"));

    Collection<StockCode> codes = asList(
            newStockCode("WIKI", "AAL", "A description"),
            newStockCode("WIKI", "BBA", "Another description"));

    ZipFile mockZipFile;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(quandl, "quandlApiKey", "TestApiKey");
        ReflectionTestUtils.setField(quandl, "fetchDatabaseCodesURL", "url1");
        ReflectionTestUtils.setField(quandl, "fetchCodeDataSerieURL", "url2");

        mockZipFile = mock(ZipFile.class);

        when(httpClient.getAsZip(eq("url1"))).thenReturn(mockZipFile);
    }

    @After
    public void tearDown() {
        reset(mockZipFile, httpClient, deserializer);
    }

    @Test
    public void fetchStockCodes_Success() throws Exception {
        // Given
        String fileName = "stockCodes.csv";

        when(mockZipFile.unzip()).thenReturn(singletonList(fileName));
        when(deserializer.deserialize(eq(fileName), eq(StockCodeDto.class))).thenReturn(dtos);

        // When
        Collection<StockCode> result = quandl.fetchStockCodes("LSE");

        // Then
        assertThatAreRefEquals(codes, result);
    }

    @Test(expected = HttpClientException.class)
    public void fetchStockCodes_ExceptionThrown() throws HttpClientException {
        // Given
        doThrow(new HttpClientException(new Exception())).when(httpClient).getAsZip(anyString());

        // Expect
        quandl.fetchStockCodes("LSE");
    }

    @Test
    public void fetchStockCodes_ExceptionThrown_KeepsDeserializingCSVs() throws Exception {
        // Given
        String fileName1 = "stockCodes1.csv";
        String fileName2 = "stockCodes2.csv";

        when(mockZipFile.unzip()).thenReturn(Arrays.<String>asList(fileName1, fileName2));
        when(deserializer.deserialize(eq(fileName2), eq(StockCodeDto.class))).thenReturn(dtos);

        doThrow(new RuntimeException("Could not deserialize!"))
                .when(deserializer)
                .deserialize(eq(fileName1), eq(StockCodeDto.class));


        // When
        Collection<StockCode> result = quandl.fetchStockCodes("LSE");

        // Then
        verify(deserializer).deserialize(eq(fileName2), eq(StockCodeDto.class));
        verify(deserializer).deserialize(eq(fileName2), eq(StockCodeDto.class));

        assertThatAreRefEquals(codes, result);

        verify(eventBus).notify(eq(ServiceExceptionEvent.KEY), any(Event.class));
    }

    private static StockCodeDto newStockCodeDto(String dataset, String code, String description) {
        StockCodeDto dto = new StockCodeDto();
        ReflectionTestUtils.setField(dto, "datasetAndCode", format("%s/%s", dataset, code));
        ReflectionTestUtils.setField(dto, "description", description);
        return dto;
    }

    private static StockCode newStockCode(String dataset, String code, String description) {
        StockCode stockCode = new StockCode();
        stockCode.setCode(code);
        stockCode.setDataset(dataset);
        stockCode.setDescription(description);
        return stockCode;
    }
}