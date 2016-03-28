package org.stock.portfolio.stockcode;

import org.springframework.data.elasticsearch.core.FacetedPage;
import org.stock.portfolio.commons.indexer.TotalCountDto;
import org.stock.portfolio.stockcode.indexer.StockCodeDto;

import java.util.List;

public interface StockCodeIndexerService {

    FacetedPage<StockCodeDto> listAllStockCodes(int page, int size);

    TotalCountDto countStockCodes();

    List<StockCodeDto> suggestStockCodes(String term);
}
