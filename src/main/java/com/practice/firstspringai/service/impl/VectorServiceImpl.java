package com.practice.firstspringai.service.impl;

import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.practice.firstspringai.service.VectorService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Service 
@Slf4j 
public class VectorServiceImpl implements VectorService {
        private final VectorStore vectorStore;

        private final ChatClient openAiChatClient;

    public VectorServiceImpl(@Qualifier("openAiChatClient") ChatClient chatClient, VectorStore vectorStore) {
        this.vectorStore = vectorStore;
        this.openAiChatClient = chatClient;
    }



    @Value("classpath:prompts/system-message.st")
    private Resource systemMessagePromptResource;


    @Override
    public void saveData(List<String> value) {

        List<Document> documents = value.stream()
        .map(text -> text != null ? new Document(text) : new Document(""))
        .toList();
        if(documents != null) {
            vectorStore.add(documents);
        }
    }

        public Flux<String> chatTemplate(String id, String message) {

            //load data from vector db

            List<Document> documents = 
            vectorStore.similaritySearch(SearchRequest.builder()
            .topK(5) //limited no. of top similar values need to retreive to reduce token size
            .similarityThreshold(0.6)  //matching value is not exact but similar 
            .query(message)
            .build());

            //similar result user query

            List<String> documentList = documents.stream().map(document -> document.getText()).toList();
            String context = String.join(", ", documentList);
            log.info("context {}", context);
            //pass in context



        return openAiChatClient.prompt()
                .system(sys -> sys.text(systemMessagePromptResource).param("documents", context))
                .user(message)
                .advisors(advisor -> advisor.param(
                        ChatMemory.CONVERSATION_ID, id))
                .stream()
                .content();
    }
}
