package org.stock.portfolio.domain;

import org.springframework.cassandra.core.PrimaryKeyType;
import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.mapping.Table;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Table(value = "stock_daily_time_series")
public class StockHistoryEntry {

    @PrimaryKeyColumn(name = "market_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String marketId;
    @PrimaryKeyColumn(name = "code", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
    private String code;
    @PrimaryKeyColumn(name = "year", ordinal = 2, type = PrimaryKeyType.PARTITIONED)
    private String year;
    @PrimaryKeyColumn(name = "date", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date date;
    @Column(value = "open_price")
    private double openPrice;
    @Column(value = "high_price")
    private double highPrice;
    @Column(value = "low_price")
    private double lowPrice;
    @Column(value = "close_price")
    private double closePrice;

    public void setMarketId(String marketId) {
        this.marketId = marketId;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setOpenPrice(double openPrice) {
        this.openPrice = openPrice;
    }

    public void setHighPrice(double highPrice) {
        this.highPrice = highPrice;
    }

    public void setLowPrice(double lowPrice) {
        this.lowPrice = lowPrice;
    }

    public void setClosePrice(double closePrice) {
        this.closePrice = closePrice;
    }
}
