package org.stock.portfolio.commons.indexer;

import org.springframework.data.elasticsearch.core.completion.Completion;

public interface Suggestion {

    Completion getSuggest();

}
