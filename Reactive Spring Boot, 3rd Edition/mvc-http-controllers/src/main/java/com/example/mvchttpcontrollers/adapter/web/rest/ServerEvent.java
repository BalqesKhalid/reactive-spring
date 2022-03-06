package com.example.mvchttpcontrollers.adapter.web.rest;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.stream.Stream;

/**
 * if you want to do infinite stream with http endpoints
 * adding some delays
 * so each second for example we can get new value
 * repeat explanation
 */
public class ServerEvent {



}



@Service
class GreetingService{



    Flux<GreetingResponse> greetMany(String name){

        return Flux
                .fromStream(Stream.generate(()->greet(name)))
                .delayElements(Duration.ofSeconds(1))
                .subscribeOn(Schedulers.elastic());
    }

    private GreetingResponse greet(String name) {
        return null;

    }


    Mono<GreetingResponse> greetonce(GreetingRequest request){

        return Mono.just(new GreetingResponse("Hello "));
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



