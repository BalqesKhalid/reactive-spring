package com.reactivemongo.example.adapter.repository.reactive;

import com.reactivemongo.example.adapter.repository.reactive.nosql.OfficerRepository;
import com.reactivemongo.example.adapter.repository.reactive.nosql.document.Officer;
import com.reactivemongo.example.adapter.repository.reactive.nosql.document.RANK;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class OfficerInit implements CommandLineRunner {

    private OfficerRepository officerRepository;

    @Autowired
    public OfficerInit(OfficerRepository officerRepository){
        this.officerRepository = officerRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        officerRepository.deleteAll()
                .thenMany(Flux.just(new Officer(RANK.CAPTAIN, "James", "Kirk"),
                        new Officer(RANK.CAPTAIN, "Jean-Luc", "Picard"),
                        new Officer(RANK.CAPTAIN, "Benjamin", "Sisko"),
                        new Officer(RANK.CAPTAIN, "Kathrya", "Janway"),
                        new Officer(RANK.CAPTAIN, "Jonathan", "Archer")
                ))
                .flatMap(officerRepository::save)
                .then()
                .doOnEach(System.out::println)
                .block();
    }
}
