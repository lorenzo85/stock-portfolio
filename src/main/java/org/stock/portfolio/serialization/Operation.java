package org.stock.portfolio.serialization;

public interface Operation<T> {
    void execute(T object);
}
