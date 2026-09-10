package com.practice.firstspringai.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.practice.firstspringai.service.ChatService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping ("/ai")
@RequiredArgsConstructor 
@Slf4j
public class ChatClientApiController {
    private final ChatService chatService;

    @GetMapping("/chatclient")
    public String generate(@RequestParam(defaultValue = "Tell me a joke") String value) {
        log.info("Generating response for value: {}", value);
        return chatService.generate(value);
    }

    @GetMapping("/prompt")
    public String useDynamicPrompt(@RequestParam(defaultValue = "Tell me a joke") String value) {
        log.info("Generating response for useDynamicPrompt: {}", value);
        return chatService.useDynamicPrompt(value);
    }

    @GetMapping("/prompt/template")
    public String useDynamicPromptTemplate() {
        return chatService.chatTemplate();
    }


    @GetMapping("/stream-chat")
    public ResponseEntity<Flux<String>> streamChat(@RequestParam("q") String query ) {
        return ResponseEntity.ok(chatService.streamChat(query));
    }
}
