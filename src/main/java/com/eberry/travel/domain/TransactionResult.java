package com.eberry.travel.domain;

public record TransactionResult(Result result, Transaction transaction) {
    public enum Result {
        SUCCCESS,
        FAILURE,
    }

}


