package com.example.mvchttpcontrollers.adapter.web.rest.functionalstyle;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.servlet.function.RouterFunctions.route;
import static org.springframework.web.servlet.function.ServerResponse.ok;

public class FunctionalEndPoints {



    @Bean
    RouterFunction<ServerResponse> routes(GreetingService greetingService){

        return route()
                .GET("/greeting/{name}",r -> ok()
                        .body(greetingService.greet(new GreetingRequest
                                        (r.pathVariable("name"))),GreetingResponse.class))
                .build();
    }
}

@Service
class GreetingService{

    com.example.mvchttpcontrollers.adapter.web.rest.GreetingResponse greet(String name){

        return new com.example.mvchttpcontrollers.adapter.web.rest.GreetingResponse("Hello ");
    }


    Mono<com.example.mvchttpcontrollers.adapter.web.rest.GreetingResponse> greet(GreetingRequest request){

        return Mono.just(new com.example.mvchttpcontrollers.adapter.web.rest.GreetingResponse("Hello "));
    }

}

@Data
@AllArgsConstructor
@NoArgsConstructor
class GreetingResponse{
    private String message;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class GreetingRequest{
    private String name;
}
