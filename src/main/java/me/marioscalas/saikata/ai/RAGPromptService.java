package me.marioscalas.saikata.ai;

import me.marioscalas.saikata.ai.model.Answer;
import me.marioscalas.saikata.ai.model.Question;

public interface RAGPromptService {
    Answer getAnswer(Question question);
    Answer getBoatExpertAnswer(Question question);
}
