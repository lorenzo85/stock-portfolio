package org.stock.portfolio.service.quandl.mapper;


import java.util.Collection;
import java.util.HashSet;

public interface Mapper<T, U> {

    U map(T object);

    default Collection<U> mapAll(Collection<T> objects) {
        Collection<U> codes = new HashSet<>();
        for (T dto : objects) {
            codes.add(map(dto));
        }
        return codes;
    }
}
