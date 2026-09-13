package com.practice.firstspringai.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.practice.firstspringai.service.VectorService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@RestController 
@RequestMapping ("/vector")
@RequiredArgsConstructor 
@Slf4j 
public class VectorController {
    private final VectorService vectorService;

        @GetMapping("/vector-memory")
    public ResponseEntity<Flux<String>> chatMemory(
             String id,
            @RequestParam String message) {
        return ResponseEntity.ok(vectorService.chatTemplate(id, message));
    }
}
