package org.stock.portfolio;

import org.jsefa.csv.CsvDeserializer;
import org.jsefa.csv.CsvIOFactory;
import org.jsefa.csv.config.CsvConfiguration;
import org.junit.Test;
import org.stock.portfolio.service.quandl.dto.StockCodeDto;

import java.io.StringReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class CsvTest extends AbstracJUnitTest {

    @Test
    public void shouldCorrectlyMapCsvLine() {
        // Given
        String csv = "WIKI/A,\"Agilent Technologies, Inc. (A) Prices, Dividends, Splits and Trading Volume\"";

        CsvConfiguration configuration = new CsvConfiguration();
        configuration.setFieldDelimiter(',');
        CsvDeserializer deserializer = CsvIOFactory.createFactory(configuration, StockCodeDto.class).createDeserializer();
        deserializer.open(new StringReader(csv));

        // When
        StockCodeDto stockCodeDto = deserializer.<StockCodeDto>next();

        // Then
        assertNotNull(stockCodeDto);
        assertEquals(stockCodeDto.getDataset(), "WIKI");
        assertEquals(stockCodeDto.getCode(), "A");
        assertEquals("Agilent Technologies, Inc. (A) Prices, Dividends, Splits and Trading Volume", stockCodeDto.getDescription());
    }
}
