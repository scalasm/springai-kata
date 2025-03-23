package me.marioscalas.saikata.ai.adapters.web;

import jakarta.validation.Valid;
import me.marioscalas.saikata.ai.AIPromptService;
import me.marioscalas.saikata.ai.RAGPromptService;
import me.marioscalas.saikata.ai.model.Answer;
import me.marioscalas.saikata.ai.model.GetCapitalQuestion;
import me.marioscalas.saikata.ai.model.GetCapitalResponse;
import me.marioscalas.saikata.ai.model.Question;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;

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
