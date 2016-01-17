package org.stock.portfolio.serialization;

import org.jsefa.csv.CsvIOFactory;
import org.jsefa.csv.config.CsvConfiguration;
import org.springframework.stereotype.Component;

import java.io.Reader;
import java.util.Collection;
import java.util.HashSet;

@Component
public class DeserializerImpl implements Deserializer {

    @Override
    public <T> Collection<T> deserialize(Reader reader, Class<? extends T> type) {
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
