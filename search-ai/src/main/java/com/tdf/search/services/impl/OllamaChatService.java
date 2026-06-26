package com.tdf.search.services.impl;

import com.tdf.search.services.AIChatService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Map;

import static com.tdf.search.constants.SystemPrompts.STORY_SYSTEM_PROMP;


@Component
@RequiredArgsConstructor
public class OllamaChatService implements AIChatService {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;


    @Override
    public @NonNull Flux<String> getQuerySolution(String query) {
        return chatClient.prompt().system("You are an expert Senior Tech Career Consultant. " +
                        "Review the request and guide the user accordingly " +
                        "Be brief.")
                .advisors(QuestionAnswerAdvisor.builder(vectorStore).build())
                .user(query)
                .stream()
                .content();

    }

    @Override
    public @NonNull Flux<String> getVectorResponse(Map<String, String> queryMap) {
        String query = queryMap.getOrDefault("query", "Give me the summary of the document?");
        return chatClient.prompt()
                .system(STORY_SYSTEM_PROMP)
                .advisors(QuestionAnswerAdvisor.builder(vectorStore).build(), new SimpleLoggerAdvisor())
                .user(query)
                .stream()
                .content();
    }
}
