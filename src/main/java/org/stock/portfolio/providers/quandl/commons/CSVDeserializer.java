package org.stock.portfolio.providers.quandl.commons;

import org.jsefa.csv.CsvIOFactory;
import org.jsefa.csv.config.CsvConfiguration;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;

@Component
public class CSVDeserializer implements Deserializer {

    private static CsvConfiguration configuration;
    static {
        configuration = new CsvConfiguration();
        configuration.setFieldDelimiter(',');
    }

    @Override
    public <T> Collection<T> deserialize(String filePath, Class<? extends T> type) {
        FileReader reader = new FileReader(filePath);

        org.jsefa.Deserializer deserializer = CsvIOFactory.createFactory(configuration, type).createDeserializer();
        deserializer.open(reader);

        Collection<T> objects = new HashSet<>();
        while (deserializer.hasNext()) {
            T deserialized = deserializer.<T>next();
            objects.add(deserialized);
        }

        deserializer.close(true);
        return objects;
    }

}
