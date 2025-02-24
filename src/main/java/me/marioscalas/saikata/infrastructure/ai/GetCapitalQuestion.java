package me.marioscalas.saikata.infrastructure.ai;

import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Ask the AI overlord for the capital of a state or country")
public record GetCapitalQuestion(
    @NotNull
    @Schema(description = "The state or country you want to know the capital for", example = "Italy")
    String stateOrCountry
) {
}
