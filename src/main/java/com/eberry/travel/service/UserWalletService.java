package com.eberry.travel.service;

import com.eberry.travel.exceptions.TransactionException;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.eberry.travel.exceptions.TransactionException.ErrorType.INSUFFICIENT_BALANCE;
import static com.eberry.travel.exceptions.TransactionException.ErrorType.INVALID_USER;
import static com.eberry.travel.exceptions.TransactionException.ErrorType.NEGATIVE_TRANSACTION;

@Component
public class UserWalletService {

    private Map<String, Long> userBalance = new ConcurrentHashMap<>();

    public Long addNewUser(String email, long initialBalance) {
        if (initialBalance < 1) {
            throw new IllegalArgumentException("Invalid initial balance");
        }
        return userBalance.compute(email, (__, balance) -> {
            if (balance == null) {
                return initialBalance;
            } else
                throw new IllegalArgumentException("User with email " + email + " already exists");
        });
    }

    public Long deductMoney(String email, long deductable) {
        if (deductable < 0) {
            throw new TransactionException(NEGATIVE_TRANSACTION, "Invalid transaction");
        }

        return userBalance.compute(email, (__, balance) -> {
            if (balance == null) {
                throw new TransactionException(INVALID_USER, "User does not exist exception");
            } else if (balance < deductable) {
                throw new TransactionException(INSUFFICIENT_BALANCE, "User does not have enough balance");
            } else
                return balance - deductable;
        });
    }

}
