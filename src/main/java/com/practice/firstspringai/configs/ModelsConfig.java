package com.practice.firstspringai.configs;

import java.util.ArrayList;
import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelsConfig {


    @Bean(name = "openAiChatClient")
    public ChatClient openAiChatClient(ChatModel openAiChatModel) {
        return ChatClient.builder(openAiChatModel).build();
    }

//advisors acts as an interceptor and intercepts our inputs and do certain ops,
//multiple advisors can be passed. And we can  create our own custom advisors as well



    @Bean(name = "ollamaChatClient")
    public ChatClient ollamaChatClient(ChatModel ollamaChatModel) {
        return ChatClient.builder(ollamaChatModel)
        .defaultAdvisors(new SimpleLoggerAdvisor(), new SafeGuardAdvisor(List.of("bomb")))
        .build();
    }
}
