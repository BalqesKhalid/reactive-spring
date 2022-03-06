package com.example.mvchttpcontrollers.adapter.web.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class ApplicationController {

    private final GreetingService greetingService;

    @GetMapping("/greetings/{name}")
    Mono<GreetingResponse> greet(@PathVariable String name){

        return this.greetingService.greet(new GreetingRequest(name));
    }
}


@Service
class GreetingService{

    GreetingResponse greet(String name){

        return new GreetingResponse("Hello ");
    }


    Mono<GreetingResponse> greet(GreetingRequest request){

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
