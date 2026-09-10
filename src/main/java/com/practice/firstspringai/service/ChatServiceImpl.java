package com.practice.firstspringai.service;

import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;


@Service 
public class ChatServiceImpl implements ChatService {

    private final ChatClient ollamaClient;

    public ChatServiceImpl(@Qualifier("ollamaChatClient") ChatClient chatClient) {
        this.ollamaClient = chatClient;
    }


    @Value ("classpath:prompts/joke-template.st")
    private Resource jokePromptResource;


    @Override
    public String generate(String value) {
        return ollamaClient.prompt(value)
                .call()
                .content();
    }


    //FLUENT API IMPLEMENTATION..... [most recommended]

    @Override
    public String useDynamicPrompt(String value) {
        // return ollamaClient.prompt().system(sys -> sys.text("You are an expert in java coding language"))
        //         .user(u -> u.text(value).param("query", value))
        //         .call()
        //         .content();

            //using from joke-template.st because sometimes the prompt becomes big and hard to handle...
                return ollamaClient.prompt().system(sys -> sys.text("You are an expert in cracking jokes"))
                // .user(u -> u.text(jokePromptResource).param("type", "vehicle").param("topic", "bike").param("maxLines", "100"))
                .user(customValue -> customValue.text(value))
                .call()
                .content();
    }


    /*
     * user - represents userinput
     * system - define rules, behaviour or identity/
     * assistant - represent ai response
     * tools/functions/call -
     * 
     * spring boot -> prompt -> llm model
     * llm model -> response - assistant role -> spring boot
     * 
     * prompt templating :we get dynamic values in the user prompt then we remove
     * those dynamic values
     * like my name is parvez so parvez is dynamic
     * prompt templating -> render -> MESSAGE -> create -> PROMPT -> ChatClient
     * 
     * 
     * 
     */

    @Override
    public String chatTemplate() {
        
        //1) without system prompts

        String templateText = "Tell me a {type} joke about {topic}";
        PromptTemplate strTemplate = new PromptTemplate(templateText);

        Prompt prompt = strTemplate.create(Map.of(
                "type", "sarcastic",
                "topic", "Spring Boot"));
        // return ollamaClient.prompt(prompt).call().content();




        //2) systemPromptTemplate -> render -> MESSAGE -> PROMPT -> ChatClient

        var sysPrmpt = new SystemPromptTemplate("You are good in cracking jokes");
        var sysMsg = sysPrmpt.createMessage(); //converting to msg instead of rendering

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
//we use stream() functionality as we want our response in chunks and in non-blocking manner 
// instead of call() which blocks our call.
        return ollamaClient.prompt().system(sys -> sys.text("You are Helpful in coding"))
                .user(customValue -> customValue.text(query))
                .stream()
                .content();
    }
    
}
