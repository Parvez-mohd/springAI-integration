package com.practice.firstspringai.service.impl;

import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.practice.firstspringai.service.ChatService;

import reactor.core.publisher.Flux;

@Service
public class ChatServiceImpl implements ChatService {

    private final ChatClient ollamaClient;

    public ChatServiceImpl(@Qualifier("ollamaChatClient") ChatClient chatClient) {
        this.ollamaClient = chatClient;
    }

    @Value("classpath:prompts/joke-template.st")
    private Resource jokePromptResource;

    @Override
    public String generate(String value) {
        return ollamaClient.prompt(value)
                .call()
                .content();
    }

    @Override
    public String useDynamicPrompt(String value) {
        return ollamaClient.prompt()
                .system(sys -> sys.text("You are an expert in cracking jokes"))
                .user(customValue -> customValue.text(value))
                .call()
                .content();
    }

    @Override
    public String chatTemplate() {
        var sysPrmpt = new SystemPromptTemplate("You are good in cracking jokes");
        var sysMsg = sysPrmpt.createMessage();

        String templateText2 = "Tell me a {type} joke about {topic}";
        PromptTemplate strTemplate2 = new PromptTemplate(templateText2);

        var userMessage = strTemplate2.createMessage(Map.of(
                "type", "sarcastic",
                "topic", "Spring Boot"));

        Prompt prompt2 = new Prompt(sysMsg, userMessage);

        return ollamaClient.prompt(prompt2).call().content();
    }

    @Override
    public Flux<String> streamChat(String query) {
        return ollamaClient.prompt()
                .system(sys -> sys.text("You are Helpful in coding"))
                .user(customValue -> customValue.text(query))
                .stream()
                .content();
    }

    public Flux<String> chatWithMemory(String conversationId, String message) {
        return ollamaClient.prompt()
                .user(message)
                .advisors(advisor -> advisor.param(
                        MessageChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY, conversationId))
                .stream()
                .content();
    }
}
