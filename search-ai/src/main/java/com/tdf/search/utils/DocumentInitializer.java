package com.tdf.search.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.stream.IntStream;

import static java.util.concurrent.Executors.newFixedThreadPool;

@Component
@RequiredArgsConstructor
@Slf4j
public class DocumentInitializer {

    private static final int BATCH_SIZE = 10;
    private static final int THREAD_POOL_SIZE = 4; // M4 Pro: can go up to 8

    private final VectorStore vectorStore;

    @Value("${rag-resource.names}")
    private Resource[] resources;

//    @EventListener(ApplicationReadyEvent.class)
    public void loadResource(){

        try{
            List<Document> splitList = new ArrayList<>();
            for (Resource resource : resources) {
                log.info("Loading resource {}", resource);
                TikaDocumentReader tikaDocumentReader = new TikaDocumentReader(resource);
                TokenTextSplitter tokenTextSplitter = TokenTextSplitter.builder().build();
                splitList.addAll(tokenTextSplitter.apply(tikaDocumentReader.read()));
            }


            log.info("Found {} splitter elements", splitList.size());

            if(splitList.isEmpty()){
                throw new RuntimeException("No documents found in the resources: " + resources);
            } else if(splitList.size() < BATCH_SIZE){
                log.info("Accepting to vector store");

                vectorStore.accept(splitList);
                // or vectorStore.add(splitList); as internally, accept calls add only
                // or vectorStore.write(splitList);as internally, write calls accept only

                vectorStore.similaritySearch("");

                log.info("Successfully Loaded the document");
            } else {
                loadDocumentParallel(splitList);
            }

        } catch (Exception e){
            log.error("Unknown Exception Occured : {}", e.getMessage());
        }

    }

    public void loadDocumentParallel(List<Document> splitList) {
        // 1. Read + split (still sequential, this part is fast)
        log.info("Total chunks: {}. Starting parallel ingestion...", splitList.size());

        // 2. Partition into batches
        List<List<Document>> batches = partitionList(splitList, BATCH_SIZE);

        // 3. Process batches in parallel
        ExecutorService executor = newFixedThreadPool(THREAD_POOL_SIZE);

        List<CompletableFuture<Void>> futures = batches.stream()
                .map(batch -> CompletableFuture.runAsync(() -> {
                    try {
                        vectorStore.accept(batch);
                        log.info("Ingested batch of {} chunks [thread: {}]",
                                batch.size(), Thread.currentThread().getName());
                    } catch (Exception e) {
                        log.error("Batch ingestion failed: {}", e.getMessage(), e);
                        throw new RuntimeException(e);
                    }
                }, executor))
                .toList();

        // 4. Wait for all batches to complete
        try {
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
            log.info("Successfully loaded all {} chunks in {} batches",
                    splitList.size(), batches.size());
        } catch (Exception e) {
            log.error("Parallel ingestion failed: {}", e.getMessage(), e);
        } finally {
            executor.shutdown();
        }
    }

    // Utility: split a list into fixed-size sublists
    private <T> List<List<T>> partitionList(List<T> list, int size) {
        return IntStream.range(0, (list.size() + size - 1) / size)
                .mapToObj(i -> list.subList(
                        i * size,
                        Math.min(i * size + size, list.size())
                ))
                .toList();
    }
}
