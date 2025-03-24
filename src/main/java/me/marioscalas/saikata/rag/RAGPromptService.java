package me.marioscalas.saikata.rag;

import me.marioscalas.saikata.rag.model.Answer;
import me.marioscalas.saikata.rag.model.Question;

public interface RAGPromptService {
    Answer getAnswer(Question question);
    Answer getBoatExpertAnswer(Question question);
}
