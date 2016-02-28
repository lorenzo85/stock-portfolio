package org.stock.portfolio.service.quandl.service;



import org.mockito.internal.matchers.apachecommons.ReflectionEquals;

import java.util.Collection;

import static org.junit.Assert.fail;

public class AssertionUtils {

    public static <T> void assertThatAreRefEquals(Collection<T> collection1, Collection<T> collection2) {
        for (T toFind : collection1) {
            boolean found = findInCollection(collection2, toFind);
            if (!found) {
                fail(String.format("The two collections %s and %s were not equals!", collection1.toString(), collection2.toString()));
            }
        }
    }

    private static <T> boolean findInCollection(Collection<T> collection2, T toFind) {
        for (T item : collection2) {
            if (new ReflectionEquals(toFind).matches(item)) return true;
        }
        return false;
    }
}
