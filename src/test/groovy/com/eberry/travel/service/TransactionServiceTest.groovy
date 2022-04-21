package com.eberry.travel.service

import com.eberry.travel.domain.Transaction
import com.eberry.travel.domain.TransactionResult
import com.eberry.travel.domain.UserName
import com.eberry.travel.exceptions.TransactionException
import spock.lang.Specification

import static com.eberry.travel.exceptions.TransactionException.ErrorType.INVALID_USER

class TransactionServiceTest extends Specification {

    TransactionService transactionService

    // before every test case
    def setup() {
        def userWalletService = Mock(UserWalletService) {
            deductMoney("xyz@gmail.com", 100) >> 500
            deductMoney("abc@gmail.com", 100) >> {
                throw new TransactionException(INVALID_USER)
            }
        }
        transactionService = new TransactionService(userWalletService)
    }

    def "should transaction result in SUCCESS"() {
        when:
        def transaction = new Transaction(new UserName("John", "Henry", "xyz@gmail.com"),
                100,
                "TR001"
        )
        def result = transactionService.process(transaction)

        then:
        result == new TransactionResult(TransactionResult.Result.SUCCCESS, transaction)
    }

    def "should transaction result in FAILURE"() {

        when:
        def transaction = new Transaction(new UserName("John", "Henry", "abc@gmail.com"),
                100,
                "TR001"
        )
        def result = transactionService.process(transaction)

        then:
        result == new TransactionResult(TransactionResult.Result.FAILURE, transaction)
    }
}
