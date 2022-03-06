package com.webclient.example.service;

import com.webclient.example.JokeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class JokeService {

    //https://api.icndb.com/jokes/random?limitTo=nerdy&firstName=bebo&lastName=khlaid
    private static final String BASE = "https://api.icndb.com/jokes/random?limitTo=nerdy";
    // class for accessing request
    private RestTemplate restTemplate;
    //non-blocking reactive
    private WebClient client = WebClient.create();


    @Autowired
    public JokeService(RestTemplateBuilder builder){
        restTemplate = builder.build();
    }

    /**
     * Synchrized Imp
     * @param firstName
     * @param lastName
     * @return
     */
    public String getJoke(String firstName , String lastName){

        String url = String.format("%s&firstName=%s&lastName=%s",
                BASE,firstName,lastName);

        JokeResponse response =
                restTemplate.getForObject(url, JokeResponse.class);

        if(response != null)
            return response.getValue().getJoke();
        return "";
    }

    /**
     * mono rapped 0 or one object--will be return immediately
     */
    public Mono<String> getJokeAsync(String firstName , String lastName){
        String url = String.format("%s&firstName=%s&lastName=%s",
                BASE,firstName,lastName);

        Mono<String> map = client.get()
                .uri(url)
                .retrieve()
                .bodyToMono(JokeResponse.class)
                .map(jokeResponse -> jokeResponse.getValue().getJoke());
        return map;

    }

}
