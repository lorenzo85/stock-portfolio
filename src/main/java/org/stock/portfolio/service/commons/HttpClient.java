package org.stock.portfolio.service.commons;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.UUID;

import static org.stock.portfolio.service.commons.FileExtension.ZIP;


@Service
@Scope("singleton")
public class HttpClient {

    private final CloseableHttpClient client;

    public HttpClient() {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        this.client = HttpClients.custom()
                .disableCookieManagement()
                .setConnectionManager(connectionManager).build();
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

    private HttpResponse get(String url) throws HttpClientException {
        HttpUriRequest method = new HttpGet(url);

        try {
            CloseableHttpResponse response = client.execute(method);
            int status = response.getStatusLine().getStatusCode();

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

}
