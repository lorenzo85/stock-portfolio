package org.stock.portfolio.providers.quandl.commons;


import org.apache.http.client.methods.HttpUriRequest;

public class HttpClientException extends RuntimeException {

    public HttpClientException(Throwable throwable) {
        super(throwable);
    }

    public HttpClientException(HttpUriRequest request) {
        super(String.format("Request exception: [%s]", request));
    }

}
