package com.tdf.search.services.impl;

import com.tdf.search.entities.PromptKey;
import com.tdf.search.constants.SystemPrompts;
import com.tdf.search.services.AIChatService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import java.util.List;
import java.util.Map;

import static com.tdf.search.constants.SystemPrompts.STORY_SYSTEM_PROMPT;


@Component
@RequiredArgsConstructor
public class OllamaChatService implements AIChatService {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    @Value("${rag-resource.prompt-key}")
    private String promptKey;

    private SearchRequest getCustomSearchRequest(String query){
        return SearchRequest.builder()
                .query(query)
                .similarityThreshold(0.6)
                .topK(3)
                .build();
    }

    public @NonNull Flux<String> getCustomVectorResponse(Map<String, String> queryMap) {

        String query = queryMap.getOrDefault("query", "Give me the summary of the document?");

        SearchRequest searchRequest = getCustomSearchRequest(query);
        List<Document> documents = vectorStore.similaritySearch(searchRequest);

        System.out.println("\n================ RETRIEVED " + documents.size() + " DOCUMENTS ================\n");

        for (int i = 0; i < documents.size(); i++) {
            Document doc = documents.get(i);

            System.out.println("Document #" + (i + 1));

            Object score = doc.getMetadata().get("score");
            if (score != null) {
                System.out.println("Score : " + score);
            }

            System.out.println(doc.getText());
            System.out.println("---------------------------------------------");
        }

        return chatClient.prompt()
                .system(STORY_SYSTEM_PROMPT)
                .advisors(
                        QuestionAnswerAdvisor.builder(vectorStore)
                                .searchRequest(searchRequest)
                                .build(),
                        new SimpleLoggerAdvisor()
                )
                .user(query)
                .stream()
                .content();
    }


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
    public @NonNull Flux<String> getVectorResponse(Map<String, String> queryMap, boolean configNeeded) {
        if (configNeeded) return getCustomVectorResponse(queryMap);
        return getDefaultVectorResponse(queryMap);
    }

    private @NonNull Flux<String> getDefaultVectorResponse(Map<String, String> queryMap) {
        String query = queryMap.getOrDefault("query", "Give me the summary of the document?");
        return chatClient.prompt()
                .system(SystemPrompts.getSystemPrompt(PromptKey.getPromptKey(promptKey)))
                .advisors(
                        QuestionAnswerAdvisor.builder(vectorStore).build()
                )
//                .advisors(new SimpleLoggerAdvisor())
//                .advisors(
//                        QuestionAnswerAdvisor.builder(vectorStore).build(),
//                        MessageChatMemoryAdvisor.builder(
//                                new MessageWindowChatMemory()).build()
//                )
//                .advisors(QuestionAnswerAdvisor.builder(vectorStore).build(), new SimpleLoggerAdvisor())
                .user(query)
                .stream()
                .content();
    }
}
