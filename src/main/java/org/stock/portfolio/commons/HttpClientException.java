package org.stock.portfolio.commons;

import org.apache.commons.httpclient.HttpMethod;

import java.io.IOException;

import static java.lang.String.format;

public class HttpClientException extends Exception {

    private static final String ERROR_MSG = "HTTP request error. Answered with status=%d, body=%s";

    public HttpClientException(Throwable throwable) {
        super(throwable);
    }

    public HttpClientException(HttpMethod method) {
        super(format(ERROR_MSG, method.getStatusCode(), unwrapBody(method)));
    }

    private static String unwrapBody(HttpMethod method) {
        try {
            return method.getResponseBodyAsString();
        } catch (IOException e) {
            return null;
        }
    }

}
