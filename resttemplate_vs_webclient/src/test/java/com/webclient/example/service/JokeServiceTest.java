package com.webclient.example.service;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.test.StepVerifier;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class JokeServiceTest {

    @Autowired
    private JokeService jokeService;


    @Test
    void getjoke(){
        String joke = jokeService.getJoke("balqes","khalid");

        assertTrue(joke.contains("balqes") || joke.contains("khalid") );
    }


    @Test
    void getJokeAsynch(){
        String joke = jokeService.getJokeAsync("balqes","khalid")
                .block(Duration.ofSeconds(2));
        assertTrue(joke.contains("balqes") || joke.contains("khalid") );

    }


    @Test
    void getJokeAsynchUsingStepVerifier(){

        StepVerifier.create(jokeService.getJokeAsync("balqes","khalid"))
                .assertNext(joke ->
        assertTrue(joke.contains("balqes") || joke.contains("khalid")
        )).verifyComplete();

    }
}