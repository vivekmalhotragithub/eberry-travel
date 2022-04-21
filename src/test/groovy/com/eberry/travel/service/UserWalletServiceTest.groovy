package com.eberry.travel.service

import com.eberry.travel.exceptions.TransactionException
import spock.lang.Specification
import spock.lang.Unroll

import static com.eberry.travel.exceptions.TransactionException.ErrorType.INSUFFICIENT_BALANCE
import static com.eberry.travel.exceptions.TransactionException.ErrorType.INVALID_USER

class UserWalletServiceTest extends Specification {


    UserWalletService userWalletService;

    // before every test case
    def setup() {
        userWalletService = new UserWalletService()
    }

    @Unroll
    def "should return #balance when deducting #amount from #initial"() {
        given:
        userWalletService.addNewUser("xyz@gmail.com", initial)

        when:
        def remainingBalance = userWalletService.deductMoney("xyz@gmail.com", amount)

        then:
        remainingBalance == balance

        where:
        initial | amount | balance
        100     | 50     | 50
        1000    | 800    | 200
    }

    @Unroll
    def "should throw exception of type #errorType when #condition"() {
        given:
        userWalletService.addNewUser(email1, initial)

        when:
        userWalletService.deductMoney(email2, amount)

        then:
        def exception = thrown(TransactionException)
        exception.errorType == errorType

        where:
        email1          | email2          | initial | amount | errorType            | condition
        "xyz@gmail.com" | "xyz@gmail.com" | 100     | 500    | INSUFFICIENT_BALANCE | "user does not have sufficient balance"
        "xyz@gmail.com" | "abc@gmail.com" | 1000    | 50     | INVALID_USER         | "user does not exist"
    }

    def "should fail if we add same user again"() {
        given:
        userWalletService.addNewUser("xyz@gmail.com", 1000)

        when:
        userWalletService.addNewUser("xyz@gmail.com", 500)

        then:
        def cause = thrown(IllegalArgumentException)
        cause.message == "User with email xyz@gmail.com already exists"
    }


}
