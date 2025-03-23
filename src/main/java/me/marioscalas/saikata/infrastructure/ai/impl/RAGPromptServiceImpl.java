package me.marioscalas.saikata.infrastructure.ai.impl;

import java.util.List;
import java.util.Map;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import me.marioscalas.saikata.infrastructure.ai.Answer;
import me.marioscalas.saikata.infrastructure.ai.Question;
import me.marioscalas.saikata.infrastructure.ai.RAGPromptService;

@Service
public class RAGPromptServiceImpl implements RAGPromptService {
    /**
     * The maximumn number of similar results to fetch from the vector store
     */
    private static final int TOP_SIMILAR_RESULTS = 5;
    /**
     * 1.0 --> exact match only, 0.0 --> anything is fine :)
     */
    private static final double SIMILARITY_THRESHOLD = 0.5;

    private final ChatModel chatModel;

    private final SimpleVectorStore vectorStore;

    @Value("classpath:/templates/rag-prompt-template.st")
    private Resource ragPromptTemplate;

    public RAGPromptServiceImpl(ChatModel chatModel, SimpleVectorStore vectorStore) {
        this.chatModel = chatModel;
        this.vectorStore = vectorStore;
    }

    @Override
    public Answer getAnswer(Question question) {
        // Step 1 - Query the vector store
        final SearchRequest searchRequest = SearchRequest.builder()
            .query(question.text()).topK(TOP_SIMILAR_RESULTS)
            .similarityThreshold(SIMILARITY_THRESHOLD)
            .build();
        final List<Document> matchingDocuments = vectorStore.similaritySearch(
            searchRequest
        );
        final List<String> documentTexts = matchingDocuments.stream().map(Document::getText).toList();

        // Step 2 - Call the chat model including the vector store results and the user input
        final PromptTemplate promptTemplate = new PromptTemplate(ragPromptTemplate);
        final Prompt prompt = promptTemplate.create(
            Map.of(
                "input", question.text(),
                "documents", String.join("\n", documentTexts)
            )
        );
        final ChatResponse chatResponse = chatModel.call(prompt);
        
        return new Answer(chatResponse.getResult().getOutput().getText());
    }
}
