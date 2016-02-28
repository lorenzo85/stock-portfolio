package org.stock.portfolio.events;

public class ServiceExceptionEvent {

    public static final String KEY = ServiceExceptionEvent.class.getSimpleName();
    private final Throwable throwable;

    public ServiceExceptionEvent(Throwable throwable) {
        this.throwable = throwable;
    }
}
