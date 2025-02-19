package me.marioscalas.saikata.infrastructure.ai;

public interface AIPromptService {
    Answer getAnswer(Question question);

    Answer getCapital(CapitalQuestion question);
}
