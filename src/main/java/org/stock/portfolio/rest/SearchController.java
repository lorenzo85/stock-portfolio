package org.stock.portfolio.rest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.stock.portfolio.indexer.StockCodeElasticSearchRepository;
import org.stock.portfolio.indexer.dto.StockCodeDto;

import java.util.Collection;

@RestController
public class SearchController {

    @Autowired
    private StockCodeElasticSearchRepository repository;

    @RequestMapping(value = "/search/code", method = RequestMethod.GET)
    @ResponseBody
    public Collection<StockCodeDto> searchByCode(@RequestParam(name = "id") String id) {
        return repository.findByCodeStartingWith(id);
    }

    @RequestMapping(value = "/search/code/page", method = RequestMethod.GET)
    @ResponseBody
    public Page<StockCodeDto> searchByCode(@RequestParam(name = "id") String id, @RequestParam(name = "page") int page, @RequestParam(name = "size") int size) {
        Pageable pageable = new PageRequest(page, size);
        return repository.findByCodeStartingWith(id, pageable);
    }
}
