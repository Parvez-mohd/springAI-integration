package com.practice.firstspringai.service.impl;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.prompt.PromptTemplate;
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


    @Value("classpath:prompts/questionAnswerPromptTemplate.st")
    private Resource questionAnswerPromptTemplate;


    @Override
    public void saveData(List<String> value) {
        List<Document> documents = value.stream()
                .map(text -> text != null ? new Document(text) : new Document(""))
                .toList();

        if (documents != null) {
            vectorStore.add(documents);
        }
    }

    public Flux<String> chatTemplate(String id, String message) {
        // Load data from vector DB manually
        List<Document> documents = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .topK(5)
                        .similarityThreshold(0.6)
                        .query(message)
                        .build());

        List<String> documentList = documents.stream().map(Document::getText).toList();
        String context = String.join(", ", documentList);
        log.info("context {}", context);

        return openAiChatClient.prompt()
                .system(sys -> sys.text(systemMessagePromptResource).param("documents", context))
                .user(message)
                .advisors(advisor -> advisor.param(
                        MessageChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY, id))
                .stream()
                .content();
    }

    public Flux<String> questionAnswerAdvisorUseCase(String id, String message) {
        String customAdviseText;
        try {
            customAdviseText = questionAnswerPromptTemplate.getContentAsString(StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to read QA prompt resource template", e);
        }
        var qaAdvisor = new QuestionAnswerAdvisor(
                vectorStore,
                SearchRequest.builder().topK(5).similarityThreshold(0.5).build(), customAdviseText);

        return openAiChatClient.prompt()
                .user(message)
                .advisors(qaAdvisor)
                .advisors(advisorSpec -> advisorSpec.param(
                        MessageChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY, id))
                .stream()
                .content();
    }
}
