package me.marioscalas.saikata.infrastructure.vector;

import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * Preload the vector store with the documents.
 */
@Slf4j
@Component
public class VectorStoreLoader implements CommandLineRunner {

    private final VectorStore vectorStore;

    private final VectorStoreProperties vectorStoreProperties;

    public VectorStoreLoader(VectorStore vectorStore, VectorStoreProperties vectorStoreProperties) {
        this.vectorStore = vectorStore;
        this.vectorStoreProperties = vectorStoreProperties;
    }

    @Override
    public void run(String... args) throws Exception {
        if (vectorStore.similaritySearch("Sportsman").isEmpty()){
            log.info("Loading documents into the vector store ...");

            vectorStoreProperties.getDocumentsToLoad().forEach(document -> {
                log.debug("Loading document: " + document.getFilename());

                TikaDocumentReader documentReader = new TikaDocumentReader(document);
                List<Document> documents = documentReader.get();

                TextSplitter textSplitter = new TokenTextSplitter();

                List<Document> splitDocuments = textSplitter.apply(documents);

                vectorStore.add(splitDocuments);
            });

            log.info("Vector store initialized!");
        }
    }
}
