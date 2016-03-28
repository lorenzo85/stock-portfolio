package org.stock.portfolio.stockcodehistory;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import org.joda.time.DateTime;

import java.util.Date;

@Table(name = "stock_daily_time_series")
public class StockCodeHistory {

    @PartitionKey(0)
    @Column(name = "market_id")
    private String marketId;
    @PartitionKey(1)
    @Column(name = "code")
    private String code;
    @PartitionKey(2)
    @Column(name = "year")
    private String year;
    @Column(name = "date")
    private Date date;
    @Column(name = "open_price")
    private double openPrice;
    @Column(name = "high_price")
    private double highPrice;
    @Column(name = "low_price")
    private double lowPrice;
    @Column(name = "close_price")
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

    public void setDate(DateTime date) {
        this.date = date.toDate();
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

    public String getMarketId() {
        return marketId;
    }

    public String getCode() {
        return code;
    }

    public String getYear() {
        return year;
    }

    public Date getDate() {
        return date;
    }

    public double getOpenPrice() {
        return openPrice;
    }

    public double getHighPrice() {
        return highPrice;
    }

    public double getLowPrice() {
        return lowPrice;
    }

    public double getClosePrice() {
        return closePrice;
    }
}
