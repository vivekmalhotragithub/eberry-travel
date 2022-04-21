package com.eberry.travel.routes;

import com.eberry.travel.api.ApiRejectedTransaction;
import com.eberry.travel.api.ApiResult;
import com.eberry.travel.domain.Transaction;
import com.eberry.travel.domain.TransactionResult;
import com.eberry.travel.domain.UserName;
import com.eberry.travel.service.TransactionService;
import com.eberry.travel.service.UserWalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class TransactionHandler {

    private static final Logger logger = LoggerFactory.getLogger(TransactionHandler.class);

    private Long initialUserBalance;
    private UserWalletService userWalletService;
    private TransactionService transactionService;

    @Autowired
    public TransactionHandler(TransactionService transactionService,
                              UserWalletService userWalletService,
                              @Value("${user.initialBalance}")
                                      Long initialUserBalance) {
        this.transactionService = transactionService;
        this.userWalletService = userWalletService;
        this.initialUserBalance = initialUserBalance;
    }

    public Mono<ServerResponse> addUsers(ServerRequest request) {
        return request.bodyToMono(String[].class)
                .flatMapMany(Flux::fromArray)
                .map(email -> {
                    userWalletService.addNewUser(email, initialUserBalance);
                    logger.info("user " + email + " added to wallet with balance " + initialUserBalance);
                    return email;
                })
                .then(ServerResponse.ok().build())
                .onErrorResume(cause -> ServerResponse.badRequest().bodyValue(cause.getMessage()));
    }


    public Mono<ServerResponse> handleTransaction(ServerRequest request) {
        return request.bodyToMono(String[].class)
                .flatMapMany(Flux::fromArray)
                .map(this::tokenizeTransaction)
                .map(it -> transactionService.process(it))
                .filter(result -> result.result() == TransactionResult.Result.FAILURE)
                .map(TransactionResult::transaction)
                .map(transaction -> new ApiRejectedTransaction(transaction.userName().firstName(),
                        transaction.userName().lastName(),
                        transaction.userName().email(),
                        transaction.transactionId()
                ))
                .collectList()
                .map(ApiResult::new)
                .flatMap(failedTransactions -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(failedTransactions))
                .onErrorResume(cause -> {
                    if (cause instanceof IllegalArgumentException) {
                        return ServerResponse.badRequest().bodyValue(cause.getMessage());
                    } else
                        logger.error("Exception, caused by: ", cause);
                    return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                });
    }

    private Transaction tokenizeTransaction(String transaction) {
        String[] transactionToken = transaction.split(",");
        if (transactionToken.length != 5) {
            throw new IllegalArgumentException("Cannot parse transactions");
        }
        long amount = Long.parseLong(transactionToken[3]);
        return new Transaction(new UserName(transactionToken[0], transactionToken[1], transactionToken[2]),
                amount,
                transactionToken[4]);
    }
}
