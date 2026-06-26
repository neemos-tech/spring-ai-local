package com.tdf.search.services.impl;

import com.tdf.search.services.AIChatService;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Map;

@Component
public class OpenAIChatService implements AIChatService {
    @Override
    public @NonNull Flux<String> getQuerySolution(String query) {
        return null;
    }

    @Override
    public @NonNull Flux<String> getVectorResponse(Map<String, String> requestMap) {
        return null;
    }
}
