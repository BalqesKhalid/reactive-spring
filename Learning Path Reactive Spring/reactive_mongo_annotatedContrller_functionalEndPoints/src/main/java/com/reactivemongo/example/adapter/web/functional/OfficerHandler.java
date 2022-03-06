package com.reactivemongo.example.adapter.web.functional;

import com.reactivemongo.example.adapter.repository.reactive.nosql.OfficerRepository;
import com.reactivemongo.example.adapter.repository.reactive.nosql.document.Officer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Component
public class OfficerHandler {

    private OfficerRepository officerRepository;

    //    @Autowired
    public OfficerHandler(OfficerRepository officerRepository) {
        this.officerRepository = officerRepository;
    }

    public Mono<ServerResponse> listOfficers(ServerRequest request) {

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(officerRepository.findAll(), Officer.class);
    }

    public Mono<ServerResponse> createOfficer(ServerRequest request) {
        Mono<Officer> officerMono = request.bodyToMono(Officer.class);
        return officerMono.flatMap(officer ->
                ServerResponse.status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(officerRepository.save(officer), Officer.class));

    }

    public Mono<ServerResponse> getOfficer(ServerRequest request) {

        String id = request.pathVariable("id");
        Mono<ServerResponse> notFound = ServerResponse.notFound().build();
        Mono<Officer> personMono = this.officerRepository.findById(id);

        return personMono
                .flatMap(person -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(person)))
                .switchIfEmpty(notFound);
    }
}
