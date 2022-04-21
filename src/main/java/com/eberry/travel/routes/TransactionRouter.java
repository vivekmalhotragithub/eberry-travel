package com.eberry.travel.routes;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration(proxyBeanMethods = false)
public class TransactionRouter {
    @Bean
    public RouterFunction<ServerResponse> route(TransactionHandler handler) {
        return RouterFunctions.route()
                .POST("/transactions", accept(APPLICATION_JSON), handler::handleTransaction)
                .POST("/addusers", accept(APPLICATION_JSON), handler::addUsers)
                .build();
    }


}
