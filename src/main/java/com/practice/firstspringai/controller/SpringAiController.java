package com.practice.firstspringai.controller;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class SpringAiController {

    @Autowired
    private ChatModel chatModel;



    @GetMapping("/ai/generate")
    public String generate(@RequestParam(defaultValue = "Tell me a joke") String prompt) {
        return chatModel.call(prompt);
    }
}
