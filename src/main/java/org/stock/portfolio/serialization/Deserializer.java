package org.stock.portfolio.serialization;

import java.io.Reader;
import java.util.Collection;

public interface Deserializer {

    <T> Collection<T> deserialize(Reader reader, Class<? extends T> type);

}
