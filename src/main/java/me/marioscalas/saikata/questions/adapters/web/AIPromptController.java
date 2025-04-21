package me.marioscalas.saikata.questions.adapters.web;

import jakarta.validation.Valid;
import me.marioscalas.saikata.questions.AIPromptService;
import me.marioscalas.saikata.questions.model.Answer;
import me.marioscalas.saikata.questions.model.GetCapitalQuestion;
import me.marioscalas.saikata.questions.model.GetCapitalResponse;
import me.marioscalas.saikata.questions.model.Question;

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
@RequestMapping("/api/v1")
public class AIPromptController {
    
    private final AIPromptService aiPromptService;
    
    AIPromptController(AIPromptService aiPromptService) {
        this.aiPromptService = aiPromptService;
    }
    
    @Operation(summary = "Submit a Prompt to the AI overlord")
    @PostMapping(value = "/ai", produces = "application/json", consumes = "application/json")
    public ResponseEntity<Answer> submitQuestion(@RequestBody @Valid Question question) {
        return ResponseEntity.ok().body(
            aiPromptService.getAnswer(question)    
        );
    }

    @Operation(summary = "Get the capital of state or country")
    @PostMapping(value = "/capital", produces = "application/json", consumes = "application/json")
    public ResponseEntity<Answer> getCapital(@RequestBody @Valid GetCapitalQuestion question) {
        return ResponseEntity.ok().body(
            aiPromptService.getCapital(question)
        );
    }

    @Operation(summary = "Get the capital of state or country, with additional useful information")
    @PostMapping(value = "/capitalWithInfo", produces = "application/json", consumes = "application/json")
    public ResponseEntity<Answer> submitQuestionWithInfo(@RequestBody @Valid GetCapitalQuestion question) {
        return ResponseEntity.ok().body(
            aiPromptService.getCapitalWithInfo(question)
        );
    }

    @Operation(summary = "Get the capital of state or country, with additional useful information in JSON format")
    @PostMapping(value = "/capitalWithInfoV2", produces = "application/json", consumes = "application/json")
    public ResponseEntity<GetCapitalResponse> submitQuestionWithInfoV2(@RequestBody @Valid GetCapitalQuestion question) {
        return ResponseEntity.ok().body(
            aiPromptService.getCapitalWithInfoV2(question)
        );
    }
}
