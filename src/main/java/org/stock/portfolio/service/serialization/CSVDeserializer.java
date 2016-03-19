package org.stock.portfolio.service.serialization;

import org.jsefa.csv.CsvIOFactory;
import org.jsefa.csv.config.CsvConfiguration;
import org.springframework.stereotype.Component;
import org.stock.portfolio.service.commons.FileReader;

import java.util.Collection;
import java.util.HashSet;

@Component
public class CSVDeserializer implements Deserializer {

    @Override
    public <T> Collection<T> deserialize(String filePath, Class<? extends T> type) {
        FileReader reader = new FileReader(filePath);
        CsvConfiguration configuration = new CsvConfiguration();
        configuration.setFieldDelimiter(',');

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
