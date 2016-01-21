package org.stock.portfolio.serialization;

import java.util.Collection;

public interface Deserializer {

    <T> Collection<T> deserialize(String filePath, Class<? extends T> type);

}
