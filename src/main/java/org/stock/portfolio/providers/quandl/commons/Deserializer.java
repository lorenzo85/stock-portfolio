package org.stock.portfolio.providers.quandl.commons;

import java.util.Collection;

public interface Deserializer {

    <T> Collection<T> deserialize(String filePath, Class<? extends T> type);

}
