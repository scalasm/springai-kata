package me.marioscalas.saikata.infrastructure.ai;

public interface AIPromptService {
    Answer getAnswer(Question question);

    Answer getCapital(GetCapitalQuestion question);

    Answer getCapitalWithInfo(GetCapitalQuestion question);

    GetCapitalResponse getCapitalWithInfoV2(GetCapitalQuestion question);
}
