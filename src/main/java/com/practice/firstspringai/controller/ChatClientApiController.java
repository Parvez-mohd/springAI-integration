package com.practice.firstspringai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class ChatClientApiController {
    @Qualifier("ollamaChatClient")
    ChatClient ollamaClient;

    // public SpringAiController(ChatClient.Builder chatClient) {
    //     this.chatClient = chatClient.build();
    // }

    public ChatClientApiController(@Qualifier("ollamaChatClient") ChatClient chatClient) {
        this.ollamaClient = chatClient;
    }



    @GetMapping("/ai/chatclient")
    public String generate(@RequestParam(defaultValue = "Tell me a joke") String value) {
        log.info("Generating response for value: {}", value);
        return ollamaClient.prompt(value)
                .call()
                .content();
    }

    @GetMapping("/ai/prompt")
    public String useDynamicPrompt(@RequestParam(defaultValue = "Tell me a joke") String value) {
        log.info("Generating response for useDynamicPrompt: {}", value);
        String query = "As an expert in programming "+value;

        
        return ollamaClient.prompt()
        .user(u -> u.text(query).param("query", query))
        .call()
        .content();
    }


    @GetMapping("/ai/prompt/template")
    public String useDynamicPromptTemplate(@RequestParam(defaultValue = "Tell me a joke") String value) {
        log.info("Generating response for useDynamicPrompt: {}", value);
        String query = "As an expert in programming " + value;

        return ollamaClient.prompt()
                .user(u -> u.text(query).param("query", query))
                .call()
                .content();
    }
}
