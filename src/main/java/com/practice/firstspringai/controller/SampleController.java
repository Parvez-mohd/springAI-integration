package com.practice.firstspringai.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class SampleController {
    private final RestClient restClient;

public SampleController() {
        this.restClient = RestClient.create();
    }


    @GetMapping("/bookData")
    public String getBookData() {
        log.info("Fetching book data from Open Library API...");
        String response =  restClient.get()
                .uri("https://openlibrary.org/books/OL58293065M.json")
                .retrieve()
                .body(String.class);
                log.info("Response payload length: {}", response != null ? response.length() : 0);
        return response;
    }
    
}
