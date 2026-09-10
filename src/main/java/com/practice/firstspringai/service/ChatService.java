package com.practice.firstspringai.service;

import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;

@Service 
public interface ChatService {
    String generate(String value);

    String useDynamicPrompt(String value);

    String chatTemplate();

    Flux<String> streamChat(String query);

    Flux<String> chatWithMemory(String id, String query);
}