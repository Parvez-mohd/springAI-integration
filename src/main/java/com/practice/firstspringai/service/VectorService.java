package com.practice.firstspringai.service;

import java.util.List;

import reactor.core.publisher.Flux;

public interface VectorService {
    public void saveData(List<String> value);

    public Flux<String> chatTemplate(String id, String message);
    public Flux<String> questionAnswerAdvisorUseCase(String id, String message);
}
