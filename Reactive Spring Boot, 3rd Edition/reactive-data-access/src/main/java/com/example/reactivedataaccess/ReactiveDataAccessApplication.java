package com.example.reactivedataaccess;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@SpringBootApplication
public class ReactiveDataAccessApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReactiveDataAccessApplication.class, args);
    }


}

@Component
@RequiredArgsConstructor
@Log4j2
class SampleDataInitializer {

    private final ReactiveRepository reactiveRepository = null;

    @EventListener(ApplicationReadyEvent.class)
    public void ready() {

        Flux<Reservation> reservations = Flux
                .just("Madhura", "Josh", "Olga", "Marcin", "Stephane", "Violette", "Dr. syer")
                .map(name -> new Reservation(null, name))
                .flatMap(this.reactiveRepository::save);

        this.reactiveRepository
                .deleteAll()
                .thenMany(reservations)
				.thenMany(this.reactiveRepository.findAll())
                .subscribe(log::info);
    }


}

interface ReactiveRepository extends ReactiveCrudRepository<Reservation, String> {
    Flux<Reservation> FindByName(String name);
}

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
class Reservation {

    @Id
    private String id;

    private String name;
}
