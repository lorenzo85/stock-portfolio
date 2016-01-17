package org.stock.portfolio.commons;

public enum FileExtension {

    CSV(".csv");

    private String value;

    FileExtension(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
