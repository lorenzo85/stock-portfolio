package org.stock.portfolio.indexer.dto;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.CompletionField;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.core.completion.Completion;
import org.stock.portfolio.domain.StockCode;

@Document(
        indexName = StockCodeDto.INDEX_NAME,
        type = StockCodeDto.INDEX_TYPE,
        replicas = StockCodeDto.INDEX_REPLICAS,
        shards = StockCodeDto.INDEX_SHARDS)
public class StockCodeDto {

    public static final String INDEX_NAME = "stock-code-index";
    public static final String INDEX_TYPE = "stock-code";
    public static final int INDEX_SHARDS = 1;
    public static final int INDEX_REPLICAS = 0;

    @Id
    private String code;

    @CompletionField(payloads = true, maxInputLength = 100)
    private Completion suggest;
    private String marketId;
    private String description;


    public StockCodeDto() {
        //
    }

    public StockCodeDto(StockCode stockCode) {
        this.code = stockCode.getCode();
        this.marketId = stockCode.getMarketId();
        this.description = stockCode.getDescription();
        this.suggest = new Completion(new String[]{code});
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
}
