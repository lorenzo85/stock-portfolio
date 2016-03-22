package org.stock.portfolio.indexer.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.client.Client;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.CompletionField;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.core.completion.Completion;
import org.stock.portfolio.domain.StockHistoryEntry;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.lang.String.format;


@Document(
        indexName = StockCodeHistoryEntryDto.INDEX_NAME,
        type = StockCodeHistoryEntryDto.INDEX_TYPE,
        replicas = StockCodeHistoryEntryDto.INDEX_REPLICAS,
        shards = StockCodeHistoryEntryDto.INDEX_SHARDS)
public class StockCodeHistoryEntryDto {

    public static final String INDEX_NAME = "stock-code-history-index";
    public static final String INDEX_TYPE = "stock-code-history";
    public static final int INDEX_REPLICAS = 0;
    public static final int INDEX_SHARDS = 1;

    private static final String COMPLETION_NAME = "stock-code-history";
    private static final int COMPLETION_RESULTS_SIZE = 100;


    @Id
    @JsonView(JsonViews.Payload.class)
    private String id;
    @JsonView(JsonViews.Payload.class)
    private String code;
    @JsonView(JsonViews.Payload.class)
    private String marketId;
    @JsonView(JsonViews.Payload.class)
    private String year;
    @JsonView(JsonViews.Payload.class)
    private double closePrice;
    @JsonView(JsonViews.Payload.class)
    private Date date;
    @CompletionField(payloads = true, maxInputLength = 100)
    private Completion suggest;

    static DateFormat df = new SimpleDateFormat("yyyy-MM-dd");


    public StockCodeHistoryEntryDto() {
        //required by mapper to instantiate object
    }

    public static StockCodeHistoryEntryDto fromEntity(ObjectMapper mapper, StockHistoryEntry entry) {
        StockCodeHistoryEntryDto dto = new StockCodeHistoryEntryDto(entry);

        String code = entry.getCode();

        Date date = entry.getDate();
        String payload = serializePayload(mapper, dto);

        Completion completion = new Completion(new String[]{code + " " + df.format(date)});
        completion.setPayload(payload);

        dto.setSuggest(completion);
        return dto;
    }

    private StockCodeHistoryEntryDto(StockHistoryEntry entry) {
        this.closePrice = entry.getClosePrice();
        this.marketId = entry.getMarketId();
        this.code = entry.getCode();
        this.year = entry.getYear();
        this.date = entry.getDate();
    }

    public String getCode() {
        return code;
    }

    public String getMarketId() {
        return marketId;
    }

    public String getYear() {
        return year;
    }

    public double getClosePrice() {
        return closePrice;
    }

    public Date getDate() {
        return date;
    }

    public Completion getSuggest() {
        return suggest;
    }

    public void setSuggest(Completion suggest) {
        this.suggest = suggest;
    }


    // Could totally be abstract!
    @SuppressWarnings("unchecked")
    public static List<StockCodeHistorySuggestionDto> completionSuggestByTerm(ObjectMapper mapper, Client client, String term) {
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

        List<StockCodeHistorySuggestionDto> results = new ArrayList<>();

        for (Suggest.Suggestion.Entry next : (Iterable<Suggest.Suggestion.Entry>) suggestions) {
            next.getOptions()
                    .forEach(object ->
                            results.add(StockCodeHistorySuggestionDto.fromOption(mapper, object)));
        }

        return results;
    }

    private static String serializePayload(ObjectMapper mapper, StockCodeHistoryEntryDto dto) {
        try {
            return mapper
                    .writerWithView(JsonViews.Payload.class)
                    .writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(format("Could not serialize payload for dto=[%s]", dto));
        }
    }

    public static StockCodeHistoryEntryDto deserializePayload(ObjectMapper mapper, String json) {
        try {
            return mapper
                    .readerWithView(JsonViews.Payload.class)
                    .forType(StockCodeHistoryEntryDto.class)
                    .readValue(json);
        } catch (IOException e) {
            throw new IllegalStateException(format("Could not deserialize payload for json=[%s]", json));
        }
    }
}
