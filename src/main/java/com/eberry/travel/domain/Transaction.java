package com.eberry.travel.domain;

public record Transaction(UserName userName,
                          long amount,
                          String transactionId) {

}