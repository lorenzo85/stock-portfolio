package org.stock.portfolio.providers.quandl.commons;

public enum FileExtension {

    CSV(".csv"), ZIP(".zip");

    private String value;

    FileExtension(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
