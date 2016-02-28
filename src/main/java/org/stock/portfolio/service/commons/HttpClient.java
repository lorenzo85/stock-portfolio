package org.stock.portfolio.service.commons;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.UUID;

import static java.lang.String.format;
import static org.stock.portfolio.service.commons.FileExtension.*;

@Component
@Scope("prototype")
public class HttpClient extends org.apache.commons.httpclient.HttpClient {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private HttpMethod method;

    public HttpClient get(String url) throws HttpClientException {
        method = new GetMethod(url);

        int status;

        try {
            LOG.debug(format("HTTP Request: executing GET on URL=%s", url));
            status = executeMethod(method);
        } catch (IOException e) {
            throw new HttpClientException(e);
        }

        if (status != HttpStatus.SC_OK) throw new HttpClientException(method);
        return this;
    }

    public ZipFile bodyAsZip() throws HttpClientException {
        try {
            InputStream in = new BufferedInputStream(method.getResponseBodyAsStream());

            String dir = System.getProperty("java.io.tmpdir");
            String name = UUID.randomUUID().toString() + ZIP.value();

            File zipFile = new File(dir + name);
            FileOutputStream fileOutputStream = new FileOutputStream(zipFile);
            OutputStream out = new BufferedOutputStream(fileOutputStream);
            IOUtils.copy(in, out);
            IOUtils.closeQuietly(out);
            IOUtils.closeQuietly(in);

            return new ZipFile(dir, name);
        } catch (IOException e) {
            throw new HttpClientException(e);
        }
    }

    public <T> T bodyAsObject(Class<T> clazz) throws HttpClientException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(method.getResponseBodyAsStream(), clazz);
        } catch (IOException e) {
            throw new HttpClientException(e);
        }
    }
}
