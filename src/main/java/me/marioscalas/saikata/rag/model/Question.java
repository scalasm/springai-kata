package me.marioscalas.saikata.rag.model;

import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Prompt to be submitted to the AI overlord")
public record Question(
    @NotNull
    @Schema(description = "The text of the question", example = "write a python program to count from 1 to 100")
    String text
) {
}
