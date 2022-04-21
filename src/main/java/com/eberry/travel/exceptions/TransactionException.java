package com.eberry.travel.exceptions;

public class TransactionException extends RuntimeException {

    public final ErrorType errorType;

    public TransactionException(ErrorType errorType) {
        super();
        this.errorType = errorType;
    }

    public TransactionException(ErrorType errorType, String message) {
        super(message);
        this.errorType = errorType;
    }

    public TransactionException(ErrorType errorType, String message, Throwable cause) {
        super(message, cause);
        this.errorType = errorType;
    }

    public enum ErrorType {
        INVALID_USER,
        INSUFFICIENT_BALANCE,
        NEGATIVE_TRANSACTION,
    }
}
