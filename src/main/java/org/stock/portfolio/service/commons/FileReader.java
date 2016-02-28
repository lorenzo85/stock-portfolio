package org.stock.portfolio.service.commons;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;

import static java.lang.String.*;

public class FileReader extends Reader {

    private java.io.FileReader fileReader;

    public FileReader(String fileName) {
        try {
            this.fileReader = new java.io.FileReader(fileName);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(format("Could not read file=[%s]", fileName), e);
        }
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        return fileReader.read(cbuf, off, len);
    }

    @Override
    public void close() throws IOException {
        fileReader.close();
    }
}
