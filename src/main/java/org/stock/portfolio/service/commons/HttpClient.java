package org.stock.portfolio.service.commons;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.UUID;

import static java.lang.String.format;
import static org.stock.portfolio.service.commons.FileExtension.ZIP;


public class HttpClient {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());
    private final CloseableHttpClient client;

    public HttpClient(CloseableHttpClient client) {
        this.client = client;
    }

    private HttpResponse get(String url) throws HttpClientException {
        HttpUriRequest method = new HttpGet(url);


        int status;
        CloseableHttpResponse response = null;
        try {
            LOG.debug(format("HTTP Request: executing GET on URL=%s", url));

            response = client.execute(method);
            status = response.getStatusLine().getStatusCode();

            if (status != HttpStatus.SC_OK) throw new HttpClientException(method);

            return response;

        } catch (IOException e) {
            throw new HttpClientException(e);
        }
    }

    private void consume(HttpResponse response) throws HttpClientException {
        try {
            EntityUtils.consume(response.getEntity());
        } catch (IOException e) {
            throw new HttpClientException(e);
        }
    }

    public ZipFile getAsZip(String url) throws HttpClientException {
        HttpResponse response = get(url);

        InputStream in = null;
        OutputStream out = null;
        try {
            in = new BufferedInputStream(response.getEntity().getContent());

            String dir = System.getProperty("java.io.tmpdir");
            String name = UUID.randomUUID().toString() + ZIP.value();

            File zipFile = new File(dir + name);
            FileOutputStream fileOutputStream = new FileOutputStream(zipFile);
            out = new BufferedOutputStream(fileOutputStream);
            IOUtils.copy(in, out);

            return new ZipFile(dir, name);

        } catch (IOException e) {
            throw new HttpClientException(e);

        } finally {
            consume(response);
            IOUtils.closeQuietly(out);
            IOUtils.closeQuietly(in);
        }
    }

    public <T> T getAsObject(String url, Class<T> clazz) throws HttpClientException {
        HttpResponse response = get(url);

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(response.getEntity().getContent(), clazz);
        } catch (IOException e) {
            throw new HttpClientException(e);
        } finally {
            consume(response);
        }
    }
}
