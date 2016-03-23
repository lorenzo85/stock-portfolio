package org.stock.portfolio.indexer;

import org.springframework.data.elasticsearch.core.completion.Completion;

public interface Suggestion {

    Completion getSuggest();

}
