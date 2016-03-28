package org.stock.portfolio.stockcodehistory.indexer;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.time.FastDateFormat;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.CompletionField;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.core.completion.Completion;
import org.stock.portfolio.commons.indexer.AbstractSuggestion;
import org.stock.portfolio.commons.indexer.JsonViews;
import org.stock.portfolio.stockcodehistory.StockCodeHistory;

import java.util.Date;

import static com.google.common.base.Preconditions.checkArgument;
import static org.elasticsearch.search.suggest.Suggest.Suggestion.Entry;


@Document(
        indexName = StockCodeHistoryEntryDto.INDEX_NAME,
        type = StockCodeHistoryEntryDto.INDEX_TYPE,
        replicas = StockCodeHistoryEntryDto.INDEX_REPLICAS,
        shards = StockCodeHistoryEntryDto.INDEX_SHARDS)
public class StockCodeHistoryEntryDto extends AbstractSuggestion {

    private static FastDateFormat DATE_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd");

    public static final String INDEX_NAME = "stock-code-history-index";
    public static final String INDEX_TYPE = "stock-code-history";
    public static final int INDEX_REPLICAS = 0;
    public static final int INDEX_SHARDS = 1;

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


    public StockCodeHistoryEntryDto() {
        //required by mapper to instantiate object
    }

    private StockCodeHistoryEntryDto(StockCodeHistory entry) {
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

    public static StockCodeHistoryEntryDto fromEntity(ObjectMapper mapper, StockCodeHistory entry) {
        StockCodeHistoryEntryDto dto = new StockCodeHistoryEntryDto(entry);

        String code = entry.getCode();

        Date date = entry.getDate();
        String payload = serializePayload(mapper, dto);

        Completion completion = new Completion(new String[]{code + " " + DATE_FORMAT.format(date)});
        completion.setPayload(payload);

        dto.setSuggest(completion);
        return dto;
    }

    public static StockCodeHistoryEntryDto fromOption(ObjectMapper mapper, Entry.Option option) {
        checkArgument(option instanceof CompletionSuggestion.Entry.Option);

        CompletionSuggestion.Entry.Option completion = (CompletionSuggestion.Entry.Option) option;
        return deserializePayload(mapper, completion.getPayloadAsString(), StockCodeHistoryEntryDto.class);
    }

}
