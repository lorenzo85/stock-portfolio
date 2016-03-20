package org.stock.portfolio.indexer.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.client.Client;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.CompletionField;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.core.completion.Completion;
import org.stock.portfolio.domain.StockCode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;
import static org.elasticsearch.search.suggest.Suggest.Suggestion.Entry;


@Document(
        indexName = StockCodeDto.INDEX_NAME,
        type = StockCodeDto.INDEX_TYPE,
        replicas = StockCodeDto.INDEX_REPLICAS,
        shards = StockCodeDto.INDEX_SHARDS)
public class StockCodeDto {

    public static final ObjectMapper JSON_MAPPER;

    static {
        JSON_MAPPER = new ObjectMapper();
        JSON_MAPPER.disable(MapperFeature.DEFAULT_VIEW_INCLUSION);
    }

    public static final String INDEX_NAME = "stock-code-index";
    public static final String INDEX_TYPE = "stock-code";
    public static final int INDEX_REPLICAS = 0;
    public static final int INDEX_SHARDS = 1;

    private static final String COMPLETION_NAME = "stock-code";
    private static final int COMPLETION_RESULTS_SIZE = 10;

    @Id
    @JsonView(JsonViews.Payload.class)
    private String code;
    @JsonView(JsonViews.Payload.class)
    private String marketId;
    @JsonView(JsonViews.Payload.class)
    private String description;
    @CompletionField(payloads = true, maxInputLength = 100)
    private Completion suggest;


    public StockCodeDto() {
        //required by mapper to instantiate object
    }

    public static StockCodeDto fromEntity(StockCode stockCode) {
        StockCodeDto dto = new StockCodeDto(stockCode);

        String code = stockCode.getCode();
        String payload = serializePayload(dto);

        Completion completion = new Completion(new String[]{code});
        completion.setPayload(payload);

        dto.setSuggest(completion);
        return dto;
    }

    private StockCodeDto(StockCode stockCode) {
        this.code = stockCode.getCode();
        this.marketId = stockCode.getMarketId();
        this.description = stockCode.getDescription();
    }

    public String getCode() {
        return code;
    }

    public String getMarketId() {
        return marketId;
    }

    public String getDescription() {
        return description;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setMarketId(String marketId) {
        this.marketId = marketId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Completion getSuggest() {
        return suggest;
    }

    public void setSuggest(Completion suggest) {
        this.suggest = suggest;
    }


    @SuppressWarnings("unchecked")
    public static List<StockCodeSuggestionDto> completionSuggestByTerm(Client client, String term) {
        CompletionSuggestionBuilder builder = new CompletionSuggestionBuilder(COMPLETION_NAME)
                .field("suggest")
                .text(term)
                .size(COMPLETION_RESULTS_SIZE);

        Suggest.Suggestion suggestions = client.prepareSuggest(INDEX_NAME)
                .addSuggestion(builder)
                .execute()
                .actionGet()
                .getSuggest()
                .getSuggestion(COMPLETION_NAME);

        List<StockCodeSuggestionDto> results = new ArrayList<>();

        for (Entry next : (Iterable<Entry>) suggestions) {
            next.getOptions().forEach(object -> {
                results.add(new StockCodeSuggestionDto(object));
            });
        }

        return results;
    }

    private static String serializePayload(StockCodeDto dto) {
        try {
            return JSON_MAPPER
                    .writerWithView(JsonViews.Payload.class)
                    .writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(format("Could not serialize payload for dto=[%s]", dto));
        }
    }

    public static StockCodeDto deserializePayload(String json) {
        try {
            return JSON_MAPPER
                    .readerWithView(JsonViews.Payload.class)
                    .forType(StockCodeDto.class)
                    .readValue(json);
        } catch (IOException e) {
            throw new IllegalStateException(format("Could not deserialize payload for json=[%s]", json));
        }
    }

}
