package com.example.websocket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

@SpringBootApplication
public class WebsocketApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebsocketApplication.class, args);
    }

}
@Configuration
class GreetingSocketConfiguration{

    @Bean
    SimpleUrlHandlerMapping simpleUrlHandlerMapping(WebSocketHandler webSocketHandler){

        return new SimpleUrlHandlerMapping(Map.of("/ws/greetings",webSocketHandler),10);
    }

    @Bean
    WebSocketHandler webSocketHandler(GreetingService greetingService){

        return new WebSocketHandler() {
            @Override
            public Mono<Void> handle(WebSocketSession session) {

                var recive = session
                        .receive()
                        .map(WebSocketMessage::getPayloadAsText)
                        .map(GreetingRequest::new)
                        .flatMap(greetingService::greet)
                        .map(GreetingResponse::getMessage)
                        .map(session::textMessage);

                return session.send(recive);
            }
        };
    }

    @Bean
    WebSocketHandlerAdapter webSocketHandlerAdapter(){
        return new WebSocketHandlerAdapter();
    }
}

@Service
class GreetingService {

    Flux<GreetingResponse> greet(GreetingRequest request) {

        return Flux.fromStream(
                Stream.generate(() -> new GreetingResponse("hello" + request.getName() + "@"
                        + Instant.now()))).delayElements(Duration.ofSeconds(1));

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

