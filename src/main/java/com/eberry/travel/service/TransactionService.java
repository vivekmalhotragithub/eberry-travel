package com.eberry.travel.service;

import com.eberry.travel.domain.Transaction;
import com.eberry.travel.domain.TransactionResult;
import com.eberry.travel.exceptions.TransactionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.eberry.travel.domain.TransactionResult.Result.FAILURE;
import static com.eberry.travel.domain.TransactionResult.Result.SUCCCESS;

@Component
public class TransactionService {

    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);
    private UserWalletService userWalletService;

    @Autowired
    public TransactionService(UserWalletService userWalletService) {
        this.userWalletService = userWalletService;
    }

    public TransactionResult process(Transaction transaction) {
        try {
            var balance = userWalletService.deductMoney(transaction.userName().email(), transaction.amount());
            logger.info("transaction {} amount {} applied successfully, new balance {} ", transaction.transactionId(), transaction.amount(), balance);
            return new TransactionResult(SUCCCESS, transaction);

        } catch (TransactionException cause) {
            logger.warn("Transaction on user {} failed because of {}", transaction.userName().email(), cause.errorType);
            return new TransactionResult(FAILURE, transaction);
        }
    }
}
