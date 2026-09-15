package com.practice.firstspringai.service.impl;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.client.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;
import org.springframework.ai.rag.preretrieval.query.expansion.MultiQueryExpander;
import org.springframework.ai.rag.preretrieval.query.transformation.QueryTransformer;
import org.springframework.ai.rag.preretrieval.query.transformation.RewriteQueryTransformer;
import org.springframework.ai.rag.retrieval.join.ConcatenationDocumentJoiner;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
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


    @Override
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


    @Override
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



    @Override 
    public Flux<String> moduleRAG(String message) {

        QueryTransformer reWriteQueryTransformer = RewriteQueryTransformer.builder()
        .chatClientBuilder(openAiChatClient.mutate().clone())
    
        .build();

        log.info("reWriteQueryTransformer {}", reWriteQueryTransformer);

        MultiQueryExpander multiQueryExpander = MultiQueryExpander.builder()
        .chatClientBuilder(openAiChatClient.mutate().clone())
        .numberOfQueries(3)
        .build();

        log.info("multiQueryExpander {}", multiQueryExpander);

        VectorStoreDocumentRetriever vectorStoreDocumentRetriever = VectorStoreDocumentRetriever.builder()
        .vectorStore(vectorStore)
        .topK(3)
        .similarityThreshold(0.3)
        .build();

        log.info("vectorStoreDocumentRetriever {}", vectorStoreDocumentRetriever);

        ContextualQueryAugmenter contextualQueryAugmenter = ContextualQueryAugmenter.builder()
        .build();
    

        var advisor  = RetrievalAugmentationAdvisor.builder()
        .queryTransformers(reWriteQueryTransformer)   //pre-retrival stage we are rewriting the msg by using LLM here, we can also use translationTransformer too 
        .queryExpander(multiQueryExpander)   //providing alternative query formulations, or by breaking down complex problems into simpler sub-queries.
        .documentRetriever(vectorStoreDocumentRetriever)   // Retrieves relevant documents from an underlying data source based on the given query
        .documentJoiner(new ConcatenationDocumentJoiner())
        .queryAugmenter(contextualQueryAugmenter)   // Augments the user query with contextual data.
        .build();


        return openAiChatClient.prompt()
                .user(message)
                .advisors(advisor)
                .stream()
                .content();
    }
}
