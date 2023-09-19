package com.tamanna.challenge.interview.calendar.exceptions;

/**
 * @author tlferreira
 */
public class NotModifiedException extends RuntimeException {
    public NotModifiedException() {
    }

    public NotModifiedException(String message) {
        super(message);
    }

    public NotModifiedException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotModifiedException(Throwable cause) {
        super(cause);
    }
}
