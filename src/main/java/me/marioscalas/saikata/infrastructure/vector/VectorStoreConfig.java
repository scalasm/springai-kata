package me.marioscalas.saikata.infrastructure.vector;

import java.io.File;
import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class VectorStoreConfig {
    @Bean
    SimpleVectorStore simpleVectorStore(EmbeddingModel embeddingModel, VectorStoreProperties vectorStoreProperties) {
        final SimpleVectorStore store = SimpleVectorStore.builder(embeddingModel).build();

        final var currentDir = new File(".").getAbsolutePath();
        
        // Add data to the vector store
        final File vectorStoreFile = new File(vectorStoreProperties.getVectorStorePath());
        if (vectorStoreFile.exists()) {
            log.info("Vector store file found. Loading ...");
            store.load(vectorStoreFile);
        } else {
            log.info("Vector store file not found. Initializing from CSV store ...");
            vectorStoreProperties.getDocumentsToLoad().forEach(documentPath -> {
                documentPath = currentDir + documentPath;
                final File documentFile = new File(documentPath);
                if (documentFile.exists()) {
                    final TikaDocumentReader tikaDocumentReader = new TikaDocumentReader(documentFile.toURI().toString());
                    final List<Document> documents = tikaDocumentReader.get();

                    final TextSplitter textSplitter = new TokenTextSplitter();
                    final List<Document> splitDocuments = textSplitter.apply(documents);
                    // Call OpenAPI to get the embeddings
                    store.add(splitDocuments);
                } else {
                    log.error("Document file not found: {}", documentPath);
                }
            });

            store.save(vectorStoreFile);
            log.info("Vector store saved to {0} ...", vectorStoreFile.getAbsolutePath());
        }
        return store;
    }
}
