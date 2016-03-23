package org.stock.portfolio.indexer.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.CompletionField;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.core.completion.Completion;
import org.stock.portfolio.domain.StockCode;

import static com.google.common.base.Preconditions.checkArgument;
import static org.elasticsearch.search.suggest.Suggest.Suggestion.Entry;


@Document(
        indexName = StockCodeDto.INDEX_NAME,
        type = StockCodeDto.INDEX_TYPE,
        replicas = StockCodeDto.INDEX_REPLICAS,
        shards = StockCodeDto.INDEX_SHARDS)
public class StockCodeDto extends AbstractSuggestion {

    public static final String INDEX_NAME = "stock-code-index";
    public static final String INDEX_TYPE = "stock-code";
    public static final int INDEX_REPLICAS = 0;
    public static final int INDEX_SHARDS = 1;

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

    public static StockCodeDto fromEntity(ObjectMapper mapper, StockCode stockCode) {
        StockCodeDto dto = new StockCodeDto(stockCode);

        String code = stockCode.getCode();
        String payload = serializePayload(mapper, dto);

        Completion completion = new Completion(new String[]{code});
        completion.setPayload(payload);

        dto.setSuggest(completion);
        return dto;
    }

    public static StockCodeDto fromOption(ObjectMapper mapper, Entry.Option option) {
        checkArgument(option instanceof CompletionSuggestion.Entry.Option);

        CompletionSuggestion.Entry.Option completion = (CompletionSuggestion.Entry.Option) option;
        return deserializePayload(mapper, completion.getPayloadAsString(), StockCodeDto.class);
    }

}
