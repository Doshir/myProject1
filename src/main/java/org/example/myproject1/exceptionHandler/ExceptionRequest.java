package org.example.myproject1.exceptionHandler;

public class ExceptionRequest extends RuntimeException{


    public ExceptionRequest() {
    }

    public ExceptionRequest(String message) {
        super(message);
    }

    public ExceptionRequest(String message, Throwable cause) {
        super(message, cause);
    }

    public ExceptionRequest(Throwable cause) {
        super(cause);
    }

    public ExceptionRequest(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
