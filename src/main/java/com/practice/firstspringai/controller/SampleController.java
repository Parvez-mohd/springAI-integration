package com.practice.firstspringai.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.http.MediaType;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class SampleController {
    private final RestClient restClient;

public SampleController() {
        this.restClient = RestClient.create();
    }


    @GetMapping(value = "/bookData", produces = MediaType.APPLICATION_JSON_VALUE)
    @CircuitBreaker(name = "openLibraryService", fallbackMethod = "getBookDataFallback")
    public String getBookData() {
        log.info("Fetching book data from Open Library API...");
        String response =  restClient.get()
                .uri("https://openlibrary.org/books/OL58293065M.json")
                .retrieve()
                .body(String.class);
                log.info("Response payload length: {}", response != null ? response.length() : 0);
        return response;
    }

    public String getBookDataFallback(Throwable throwable) {
        log.error("Open Library API is down or failing. Triggering fallback. Error: {}", throwable.getMessage());

        return """
                {
                  "status": "Fallback",
                  "message": "The external book service is currently unavailable. Please try again later.",
                  "cachedData": {
                    "title": "Default Book Title (Cached)",
                    "isbn": "0451450523"
                  }
                }
                """;
    }
    
}
