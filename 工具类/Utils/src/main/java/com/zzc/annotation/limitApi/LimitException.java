package com.zzc.annotation.limitApi;

/**
 * @author Administrator
 */
public class LimitException extends RuntimeException {

    private static final long serialVersionUID = 6559606007632362870L;

    LimitException(String message) {
        super(message);
    }

    LimitException(Throwable cause) {
        super(cause);
    }
}
