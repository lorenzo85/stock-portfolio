package org.stock.portfolio.indexer.dto;

public class TotalCountDto {

    private long totalCount;

    public TotalCountDto(long totalCount) {
        this.totalCount = totalCount;
    }

    public long getTotalCount() {
        return totalCount;
    }
}
