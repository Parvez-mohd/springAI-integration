package com.practice.firstspringai.service;

import org.springframework.stereotype.Service;

@Service 
public interface ChatService {
    String generate(String value);

    String useDynamicPrompt(String value);

    String chatTemplate();
}