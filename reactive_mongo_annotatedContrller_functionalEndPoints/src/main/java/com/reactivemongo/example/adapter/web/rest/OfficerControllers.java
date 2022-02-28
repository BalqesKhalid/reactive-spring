package com.reactivemongo.example.adapter.web.rest;


import com.reactivemongo.example.adapter.repository.reactive.nosql.OfficerRepository;
import com.reactivemongo.example.adapter.repository.reactive.nosql.document.Officer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/officers")
//annotated controller
public class OfficerControllers {

    private OfficerRepository officerRepository;

    @Autowired
    public OfficerControllers(OfficerRepository officerRepository) {
        this.officerRepository = officerRepository;
    }

    @GetMapping
    public Flux<Officer> getAllOfficer() {

        return officerRepository.findAll();
    }

    @GetMapping("{id}")
    public Mono<Officer> getOfficer(@PathVariable String id) {

        return officerRepository.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Officer> saveOfficer(@RequestBody Officer officer) {
        return officerRepository.save(officer);
    }

    @PutMapping("{id}")
    public Mono<ResponseEntity<Officer>> updateOfficer(@PathVariable(value = "id")
                                                               String id, @RequestBody Officer officer) {
        return officerRepository.findById(id)
                .flatMap(existingOfficer -> {
                    existingOfficer.setRank(officer.getRank());
                    existingOfficer.setFirst(officer.getFirst());
                    existingOfficer.setLast(officer.getLast());
                    return officerRepository.save(existingOfficer);
                }).map(updatedOfficer -> new ResponseEntity<>(updatedOfficer, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>((HttpStatus.NOT_FOUND)));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteOfficer(@PathVariable(value = "id")
                                                               String id) {
        return officerRepository.deleteById(id)
                .then(Mono.just((new ResponseEntity<Void>(HttpStatus.NO_CONTENT))))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


}

