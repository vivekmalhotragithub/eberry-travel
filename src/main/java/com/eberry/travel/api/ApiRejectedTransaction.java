package com.eberry.travel.api;

public record ApiRejectedTransaction(String firstName,
                                     String lastName,
                                     String email,
                                     String transactionNumber) {
}
