package com.practice.firstspringai.service.impl;

import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.practice.firstspringai.service.VectorService;

@Service 
public class VectorServiceImpl implements VectorService {
        private final VectorStore vectorStore;

    public VectorServiceImpl(@Qualifier("ollamaChatClient") ChatClient chatClient, VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }


    @Value ("classpath:prompts/joke-template.st")
    private Resource jokePromptResource;


    @Override
    public void saveData(List<String> value) {

        List<Document> documents = value.stream()
        .map(text -> text != null ? new Document(text) : new Document(""))
        .toList();
        if(documents != null) {
            vectorStore.add(documents);
        }
    }
}
