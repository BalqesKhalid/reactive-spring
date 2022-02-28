package com.reactivemongo.example.adapter.repository.reactive.nosql;

import com.reactivemongo.example.adapter.repository.reactive.nosql.document.Officer;
import com.reactivemongo.example.adapter.repository.reactive.nosql.document.RANK;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface OfficerRepository extends ReactiveMongoRepository<Officer,String> {

    Flux<Officer> findByRank(RANK rank);
    Flux<Officer> findByLast(String last);
}
