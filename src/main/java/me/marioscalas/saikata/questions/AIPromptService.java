package me.marioscalas.saikata.questions;

import me.marioscalas.saikata.questions.model.Answer;
import me.marioscalas.saikata.questions.model.GetCapitalQuestion;
import me.marioscalas.saikata.questions.model.GetCapitalResponse;
import me.marioscalas.saikata.questions.model.Question;

public interface AIPromptService {
    Answer getAnswer(Question question);

    Answer getCapital(GetCapitalQuestion question);

    Answer getCapitalWithInfo(GetCapitalQuestion question);

    GetCapitalResponse getCapitalWithInfoV2(GetCapitalQuestion question);
}
