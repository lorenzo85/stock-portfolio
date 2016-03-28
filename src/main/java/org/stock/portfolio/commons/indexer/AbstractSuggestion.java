package org.stock.portfolio.commons.indexer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import static java.lang.String.format;

public abstract class AbstractSuggestion implements Suggestion {

    protected static <T> String serializePayload(ObjectMapper mapper, T dto) {
        try {
            return mapper
                    .writerWithView(JsonViews.Payload.class)
                    .writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(format("Could not serialize payload for dto=[%s]", dto));
        }
    }

    protected static <T> T deserializePayload(ObjectMapper mapper, String json, Class<?> clazz) {
        try {
            return mapper
                    .readerWithView(JsonViews.Payload.class)
                    .forType(clazz)
                    .readValue(json);
        } catch (IOException e) {
            throw new IllegalStateException(format("Could not deserialize payload for json=[%s]", json), e);
        }
    }

}
