package org.stock.portfolio.stockcodehistory;

import org.stock.portfolio.commons.indexer.TotalCountDto;
import org.stock.portfolio.stockcodehistory.indexer.StockCodeHistoryEntryDto;

import java.util.List;

public interface StockCodeHistoryIndexerService {

    TotalCountDto countStockCodeHistoryEntries();

    List<StockCodeHistoryEntryDto> suggestStockCodeHistory(String term);

}
