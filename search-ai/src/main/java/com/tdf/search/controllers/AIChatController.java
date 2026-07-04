package com.tdf.search.controllers;


import com.tdf.search.services.AIChatService;
import com.tdf.search.utils.DocumentInitializer;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ResponseEntity;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/ollama")
@RequiredArgsConstructor
public class AIChatController {

    private final AIChatService ollamaChatService;
    private final VectorStore vectorStore;
    private final DocumentInitializer documentInitializer;

    @GetMapping("/document/init")
    public ResponseEntity<HttpStatusCode, String> initializeDocument() {

        documentInitializer.loadResource();
        return new ResponseEntity<>(
                HttpStatusCode.valueOf(201),"ack"
        );
    }

    @GetMapping("/respond")
    public Flux<String> getSolutions(@RequestParam String query){
        return ollamaChatService.getQuerySolution(query);
    }


    //    @PostMapping(value = "/story", produces = "text/event-stream")
    @PostMapping(value = "/respond")
    public Flux<String> getSolutions(@RequestBody Map<String, String> requestMap,
                                     @RequestParam(value = "config-needed", required = false,
                                             defaultValue = "false") boolean configNeeded){
        return ollamaChatService.getVectorResponse(requestMap, configNeeded);
    }

}
