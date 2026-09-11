package com.practice.firstspringai.configs;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelsConfig {

    @Bean
    public ChatMemory chatMemory() {
        return new InMemoryChatMemory();
    }


    @Bean(name = "openAiChatClient")
    public ChatClient openAiChatClient(ChatModel openAiChatModel) {
        return ChatClient.builder(openAiChatModel).build();
    }

//advisors acts as an interceptor and intercepts our inputs and do certain ops,
//multiple advisors can be passed. And we can  create our own custom advisors as well



    @Bean(name = "ollamaChatClient")
    public ChatClient ollamaChatClient(ChatModel ollamaChatModel, ChatMemory chatMemory) {
        return ChatClient.builder(ollamaChatModel)
        .defaultAdvisors(new SimpleLoggerAdvisor(), new MessageChatMemoryAdvisor(chatMemory))
        .build();
    }
}
