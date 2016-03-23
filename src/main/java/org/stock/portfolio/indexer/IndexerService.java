package org.stock.portfolio.indexer;

import org.springframework.data.elasticsearch.core.FacetedPage;
import org.stock.portfolio.indexer.dto.StockCodeDto;
import org.stock.portfolio.indexer.dto.StockCodeHistoryEntryDto;
import org.stock.portfolio.indexer.dto.TotalCountDto;

import java.util.List;

public interface IndexerService {

    // Paging on stock codes
    FacetedPage<StockCodeDto> listAllStockCodes(int page, int size);

    // Total counts
    TotalCountDto countStockCodes();

    TotalCountDto countStockCodeHistoryEntries();

    // Suggest
    List<StockCodeDto> suggestStockCodes(String term);

    List<StockCodeHistoryEntryDto> suggestStockCodeHistory(String term);

}
