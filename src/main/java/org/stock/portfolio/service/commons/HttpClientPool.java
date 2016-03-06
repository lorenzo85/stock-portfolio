package org.stock.portfolio.service.commons;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

public class HttpClientPool {

    private final PoolingHttpClientConnectionManager connectionManager;

    public HttpClientPool() {
        connectionManager = new PoolingHttpClientConnectionManager();
    }

    public HttpClient client() {
        CloseableHttpClient client = HttpClients.custom()
                .disableCookieManagement()
                .setConnectionManager(connectionManager).build();
        return new HttpClient(client);
    }
}
