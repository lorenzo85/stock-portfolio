package org.stock.portfolio.stockcode;


import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

@Table(name = "codes")
public class StockCode {

    @PartitionKey(0)
    @Column (name = "market_id")
    private String marketId;
    @PartitionKey(1)
    @Column(name = "code")
    private String code;
    @Column(name = "data_set")
    private String dataset;
    @Column(name = "description")
    private String description;

    public String getMarketId() {
        return marketId;
    }

    public void setMarketId(String marketId) {
        this.marketId = marketId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDataset() {
        return dataset;
    }

    public void setDataset(String dataset) {
        this.dataset = dataset;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "StockCode{" +
                "marketId='" + marketId + '\'' +
                ", code='" + code + '\'' +
                ", dataset='" + dataset + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
