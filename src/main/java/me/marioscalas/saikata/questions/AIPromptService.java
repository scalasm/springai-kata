package me.marioscalas.saikata.ai;

import me.marioscalas.saikata.ai.model.Answer;
import me.marioscalas.saikata.ai.model.GetCapitalQuestion;
import me.marioscalas.saikata.ai.model.GetCapitalResponse;
import me.marioscalas.saikata.ai.model.Question;

public interface AIPromptService {
    Answer getAnswer(Question question);

    Answer getCapital(GetCapitalQuestion question);

    Answer getCapitalWithInfo(GetCapitalQuestion question);

    GetCapitalResponse getCapitalWithInfoV2(GetCapitalQuestion question);
}
