package me.marioscalas.saikata.ai.internal;

import java.util.List;
import java.util.Map;

import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import me.marioscalas.saikata.ai.RAGPromptService;
import me.marioscalas.saikata.ai.model.Answer;
import me.marioscalas.saikata.ai.model.Question;

/**
 * Simple implementation for the RAG Template.
 * 
 * Note that the code contains a lot of duplication because its purpose is to demonstrate the use of the RAG model
 * in several scenarios. In a real-world application, you would refactor this code to remove the duplication.
 */
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

    private final VectorStore vectorStore;

    @Value("classpath:/templates/rag-prompt-template.st")
    private Resource ragPromptTemplate;

    @Value("classpath:/templates/rag-prompt-template-with-metadata.st")
    private Resource ragPromptWithMetadataTemplate;

    @Value("classpath:/templates/rag-prompt-template-sys-message.st")
    private Resource ragPromptSystemMessageTemplate;

    public RAGPromptServiceImpl(ChatModel chatModel, VectorStore vectorStore) {
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
        final PromptTemplate promptTemplate = new PromptTemplate(ragPromptWithMetadataTemplate);
        final Prompt prompt = promptTemplate.create(
            Map.of(
                "input", question.text(),
                "documents", String.join("\n", documentTexts)
            )
        );
        final ChatResponse chatResponse = chatModel.call(prompt);
        
        return new Answer(chatResponse.getResult().getOutput().getText());
    }

    @Override
    public Answer getBoatExpertAnswer(Question question) {
        // Step 1 - Query the vector store
        final SearchRequest searchRequest = SearchRequest.builder()
            .query(question.text()).topK(TOP_SIMILAR_RESULTS)
            .similarityThreshold(SIMILARITY_THRESHOLD)
            .build();
        final List<Document> matchingDocuments = vectorStore.similaritySearch(
            searchRequest
        );
        final List<String> documentTexts = matchingDocuments.stream().map(Document::getText).toList();

        // Step 2 - Create a system message to drive the answer format and behaviour
        final PromptTemplate systemPromptTemplate = new SystemPromptTemplate(ragPromptSystemMessageTemplate);
        final Message systemMessage = systemPromptTemplate.createMessage();

        // Step 3 - Create the user input itself structured to support the embedded documents and user request
        final PromptTemplate promptTemplate = new PromptTemplate(ragPromptTemplate);
        final Message userMessage = promptTemplate.createMessage(
            Map.of(
                "input", question.text(),
                "documents", String.join("\n", documentTexts)
            )
        );

        // Step 4 - Call the chat model with both system and user messages
        final Prompt prompt = new Prompt(List.of(systemMessage, userMessage));
        final ChatResponse chatResponse = chatModel.call(prompt);
        
        return new Answer(chatResponse.getResult().getOutput().getText());
    }
}
