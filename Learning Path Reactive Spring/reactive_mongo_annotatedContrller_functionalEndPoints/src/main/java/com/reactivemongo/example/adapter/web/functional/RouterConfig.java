package com.reactivemongo.example.adapter.web.functional;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class RouterConfig {

    @Bean
    public RouterFunction<ServerResponse> route(OfficerHandler officerHandler) {

        return RouterFunctions
                .route(GET("/route/{id}").and(accept(MediaType.APPLICATION_JSON)), officerHandler::getOfficer)
                .andRoute(GET("/route").and(accept(MediaType.APPLICATION_JSON)), officerHandler::listOfficers)
                .andRoute(POST("/route").and(accept(MediaType.APPLICATION_JSON)), officerHandler::createOfficer);
    }

}
