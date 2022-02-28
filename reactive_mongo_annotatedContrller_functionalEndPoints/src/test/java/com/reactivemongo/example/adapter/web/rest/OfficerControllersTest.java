package com.reactivemongo.example.adapter.web.rest;

import com.reactivemongo.example.adapter.repository.reactive.nosql.OfficerRepository;
import com.reactivemongo.example.adapter.repository.reactive.nosql.document.Officer;
import com.reactivemongo.example.adapter.repository.reactive.nosql.document.RANK;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OfficerControllersTest {

    @Autowired
    private WebTestClient client;

    @Autowired
    private OfficerRepository repository;


    private List<Officer> officers = Arrays.asList(
            new Officer(RANK.CAPTAIN, "James", "Kirk"),
            new Officer(RANK.CAPTAIN, "Jean-Luc", "Picard"),
            new Officer(RANK.CAPTAIN, "Benjamin", "Sisko"),
            new Officer(RANK.CAPTAIN, "Kathrya", "Janway"),
            new Officer(RANK.CAPTAIN, "Jonathan", "Archer")
    );
    @Before
    public void setUp() throws Exception {
        repository.deleteAll()
                .thenMany(Flux.fromIterable(officers))
                .flatMap(repository::save)
                .then()
                .block();

    }

    @Test
    public void testGetAllOfficer(){

        client.get().uri("/officers")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(Officer.class)
                .hasSize(5)
                .consumeWith(System.out::println);
    }

    @Test
    public void testGetOfficer(){
        client.get().uri("/officers/{id}",officers.get(0).getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(Officer.class)
                .consumeWith(System.out::println);
    }

    @Test
    public void testCreatedOfficer(){
        Officer officer = new Officer(RANK.CAPTAIN, "Hikaru", "Sulu");

        client.post().uri("/officers")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(officer),Officer.class)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.first").isEqualTo("Hikaru")
                .jsonPath("$.last").isEqualTo("Sulu")
                .consumeWith(System.out::println);
    }

}