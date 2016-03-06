package org.stock.portfolio.service.quandl.service;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.stock.portfolio.domain.StockCode;
import org.stock.portfolio.events.ReactorConfig;
import org.stock.portfolio.events.ServiceExceptionEvent;
import org.stock.portfolio.service.ServiceConfig;
import org.stock.portfolio.service.ServiceException;
import org.stock.portfolio.service.commons.HttpClient;
import org.stock.portfolio.service.commons.HttpClientException;
import org.stock.portfolio.service.commons.ZipFile;
import org.stock.portfolio.service.quandl.dto.StockCodeDto;
import org.stock.portfolio.service.serialization.Deserializer;
import reactor.bus.Event;
import reactor.bus.EventBus;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.mockito.Mockito.*;
import static org.stock.portfolio.service.quandl.service.AssertionUtils.assertThatAreRefEquals;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {ServiceConfig.class, ReactorConfig.class, TestConfig.class})
public class QuandlServiceTest {

    @Autowired HttpClient httpClient;
    @Autowired QuandlServiceImpl quandl;
    @Autowired Deserializer deserializer;
    @Autowired EventBus eventBus;

    List<StockCodeDto> dtos = asList(
            newStockCodeDto("WIKI", "AAL", "A description"),
            newStockCodeDto("WIKI", "BBA", "Another description"));

    Collection<StockCode> codes = asList(
            newStockCode("WIKI", "AAL", "A description"),
            newStockCode("WIKI", "BBA", "Another description"));

    ZipFile mockZipFile;

/**
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(quandl, "quandlApiKey", "TestApiKey");
        ReflectionTestUtils.setField(quandl, "fetchDatabaseCodesURL", "url1");
        ReflectionTestUtils.setField(quandl, "fetchCodeDataSerieURL", "url2");

        mockZipFile = mock(ZipFile.class);

        when(httpClient.(eq("url1"))).thenReturn(httpClient);
        when(httpClient.get(eq("url2"))).thenReturn(httpClient);
    }

    @Test
    public void shouldSuccessfullyFetchStockCodes() throws Exception {
        // Given
        String fileName = "stockCodes.csv";

        when(httpClient.bodyAsZip()).thenReturn(mockZipFile);
        when(mockZipFile.unzip()).thenReturn(singletonList(fileName));
        when(deserializer.deserialize(eq(fileName), eq(StockCodeDto.class))).thenReturn(dtos);

        // When
        Collection<StockCode> result = quandl.fetchStockCodes("LSE");

        // Then
        assertThatAreRefEquals(codes, result);
    }

    @Test(expected = ServiceException.class)
    public void shouldThrowExceptionWhenHttpClientThrowsException() throws HttpClientException, ServiceException {
        // Given
        HttpMethod method = mock(HttpMethod.class);
        when(method.getStatusCode()).thenReturn(HttpStatus.SC_FORBIDDEN);

        doThrow(new HttpClientException(method)).when(httpClient).get(anyString());

        // Expect
        quandl.fetchStockCodes("LSE");
    }

    @Test
    public void shouldKeepDeserializingAfterExceptionThrown() throws Exception {
        // Given
        String fileName1 = "stockCodes1.csv";
        String fileName2 = "stockCodes2.csv";

        when(httpClient.bodyAsZip()).thenReturn(mockZipFile);
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
    }*/

    @After
    public void tearDown() {
        reset(mockZipFile, httpClient, deserializer);
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