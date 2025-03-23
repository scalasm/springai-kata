package me.marioscalas.saikata.infrastructure.web.ai;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import me.marioscalas.saikata.infrastructure.ai.AIPromptService;
import me.marioscalas.saikata.infrastructure.ai.Answer;
import me.marioscalas.saikata.infrastructure.ai.GetCapitalQuestion;
import me.marioscalas.saikata.infrastructure.ai.GetCapitalResponse;
import me.marioscalas.saikata.infrastructure.ai.Question;
import me.marioscalas.saikata.infrastructure.ai.RAGPromptService;

/**
 * Simple REST API for a chatbot.
 */
@RestController
@RequestMapping("/api/v1/rag")
public class RAGAIPromptController {
    
    private final RAGPromptService aiPromptService;
    
    RAGAIPromptController(RAGPromptService aiPromptService) {
        this.aiPromptService = aiPromptService;
    }
    
    @Operation(summary = "Submit a prompt to the RAG'ed AI overlord")
    @PostMapping(value = "/prompt", produces = "application/json", consumes = "application/json")
    public ResponseEntity<Answer> submitQuestion(@RequestBody @Valid Question question) {
        return ResponseEntity.ok().body(
            aiPromptService.getAnswer(question)    
        );
    }
}
