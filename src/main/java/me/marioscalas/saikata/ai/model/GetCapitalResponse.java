package me.marioscalas.saikata.ai.model;

import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;

public record GetCapitalResponse(
    @JsonPropertyDescription("The capital of the state or country") String capitalName,
    @JsonPropertyDescription("The city population") BigInteger population,
    @JsonPropertyDescription("Region where the capital is located") String region,
    @JsonPropertyDescription("The primary language spoken") String language,
    @JsonPropertyDescription("Currency used") String currency)
{
}