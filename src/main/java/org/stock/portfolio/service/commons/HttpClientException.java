package org.stock.portfolio.service.commons;

import org.apache.http.client.methods.HttpUriRequest;

import static java.lang.String.format;

// TODO: Fix this
public class HttpClientException extends RuntimeException {

    private static final String ERROR_MSG = "HTTP request error. Answered with status=%d, body=%s";

    public HttpClientException(Throwable throwable) {
        super(throwable);
    }

    public HttpClientException(HttpUriRequest method) {
        super(format(ERROR_MSG, null, null));
    }


}
