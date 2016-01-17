package org.stock.portfolio.indexer.dto;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.CompletionField;
import org.springframework.data.elasticsearch.annotations.Document;
import org.stock.portfolio.domain.StockCode;

@Document(indexName = "post", type = "post", shards = 1, replicas = 0)
public class StockCodeDto {
    @Id
    @CompletionField
    private String code;
    private String marketId;
    private String description;

    public StockCodeDto() {
    }

    public StockCodeDto(StockCode stockCode) {
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
}
