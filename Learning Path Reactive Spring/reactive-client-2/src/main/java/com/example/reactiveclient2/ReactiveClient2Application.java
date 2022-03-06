package com.example.reactiveclient2;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.var;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class ReactiveClient2Application {

    public static void main(String[] args) {
        SpringApplication.run(ReactiveClient2Application.class, args);
    }

    @Bean
    WebClient webClient(WebClient.Builder builder) {

        return builder
                .baseUrl("http://localhost:8080")
//				.filter(ExchangeFilterFunctions.basicAuthentication())
                .build();
    }

}

@Component
@RequiredArgsConstructor
@Log4j2
class Client {

    private final WebClient client;
    private final ReactiveCircuitBreakerFactory breakerFactory;

    /**
     * we mention reactive client, error handling, circuit breaker
     * circuit breaker part of spring cloud
     */
    @EventListener(ApplicationReadyEvent.class)
    public void ready() {
        //hedging
        //host1,2,3  of type flux
        //Flux.first(host1,2,3)


        var name = "Spring Fans";
        ReactiveCircuitBreaker breaker = breakerFactory.create("greeting");
        Mono<String> http = this.client
                .get()
                .uri("/greetings/{name}")
                .retrieve()
                .bodyToMono(GreetingResponse.class)
                .map(GreetingResponse::getMessage);
                //error handling
//                .onErrorResume(exception -> Mono.just("EEEk"));
//                .onBackpressureBuffer()
//                .onErrorMap()

//                .subscribe(gr -> log.info("Mono: " + gr));
        //using circuit breaker
        breaker.run(http,throwable -> Mono.just("EEk"))
                .subscribe(gr-> log.info("Mono "+gr);

        this.client
                .get()
                .uri("/greetings/{name}")
                .retrieve()
                .bodyToFlux(GreetingResponse.class)
                .subscribe(gr -> log.info("Flux: " + gr.getMessage()));
    }
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class GreetingResponse {
    private String message;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class GreetingRequest {
    private String name;
}
