package com.reactivemongo.example.adapter.repository.reactive.nosql;

import com.reactivemongo.example.adapter.repository.reactive.nosql.document.Officer;
import com.reactivemongo.example.adapter.repository.reactive.nosql.document.RANK;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class OfficerRepositoryTest {

    @Autowired
    private OfficerRepository officerRepository;

    private List<Officer> officers = Arrays.asList(
            new Officer(RANK.CAPTAIN, "James", "Kirk"),
            new Officer(RANK.CAPTAIN, "Jean-Luc", "Picard"),
            new Officer(RANK.CAPTAIN, "Benjamin", "Sisko"),
            new Officer(RANK.CAPTAIN, "Kathrya", "Janway"),
            new Officer(RANK.CAPTAIN, "Jonathan", "Archer")
    );


    @Before
    public void setUp() throws Exception {
        officerRepository.deleteAll()
                .thenMany(Flux.fromIterable(officers))
                .flatMap(officerRepository::save)
                .then()
                .block();

    }

    @Test
    public void save() {
        Officer lorca = new Officer(RANK.CAPTAIN, "Gabriel", "Lorca");
        StepVerifier.create(officerRepository.save(lorca))
                .expectNextMatches(officer -> !officer.getId().equals(""))
                .verifyComplete();
    }

    @Test
    public void findAll() {

        StepVerifier.create(officerRepository.findAll())
                .expectNextCount(5)
                .verifyComplete();
    }

    @Test
    public void findById() {

        officers.stream()
                .map(Officer::getId)
                .forEach(id -> StepVerifier.create(officerRepository.findById(id)
                        )
                        .expectNextCount(1)
                        .verifyComplete());
    }

    @Test
    public void findByIdNotExist() {

        StepVerifier.create(officerRepository.findById("xyz"))
                .verifyComplete();
    }

    @Test
    public void count() {

        StepVerifier.create(officerRepository.count())
                .expectNext(5L)
                .verifyComplete();
    }

    @Test
    public void findByRank() {

        StepVerifier.create(officerRepository.findByRank(RANK.CAPTAIN)
                        .map(Officer::getRank)
                        .distinct())
                .expectNextCount(1)
                .verifyComplete();

        StepVerifier.create(officerRepository.findByRank(RANK.ENGLISH)
                        .map(Officer::getRank)
                        .distinct())
                .expectNextCount(1)
                .verifyComplete();
    }


    @Test
    public void findByLast() {

        officers.stream()
                .map(Officer::getLast)
                .forEach(lastName ->
                        StepVerifier
                                .create(officerRepository
                                        .findByLast(lastName))
                                .expectNextMatches(officer ->
                                        officer.getLast()
                                                .equals(lastName))
                                .verifyComplete());

    }
}

