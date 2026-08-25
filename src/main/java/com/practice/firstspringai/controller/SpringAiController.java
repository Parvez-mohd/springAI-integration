package com.practice.firstspringai.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class SpringAiController {

    
    private ChatClient chatClient;

    public SpringAiController(ChatClient.Builder chatClient) {
        this.chatClient = chatClient.build();
    }



    @GetMapping("/ai/generate")
    public String generate(@RequestParam(defaultValue = "Tell me a joke") String prompt) {
        log.info("Generating response for prompt: {}", prompt);
        return chatClient.prompt(prompt)
                .call()
                .content();
    }


@PostMapping("/ai/translate")
public String translate(
        @RequestParam(defaultValue = "Tell me a joke") String prompt,
        @RequestHeader String language) {
    
    return chatClient.prompt()
            .system("You are a professional translator. Translate the given text accurately into the target language without adding conversational filler.")
            .user(u -> u.text("Target Language: {language}\nText to translate: {text}")
                    .param("language", language)
                    .param("text", prompt))
            .call()
            .content();
}
}
