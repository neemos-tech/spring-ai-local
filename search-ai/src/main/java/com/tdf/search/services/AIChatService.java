package com.tdf.search.services;

import org.jspecify.annotations.NonNull;
import reactor.core.publisher.Flux;

import java.util.Map;

public interface AIChatService {
    @NonNull Flux<String> getQuerySolution(String query);
    @NonNull Flux<String> getVectorResponse(Map<String, String> requestMap);
}
