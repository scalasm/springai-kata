package me.marioscalas.saikata.infrastructure.ai.impl;

import java.util.Map;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import jakarta.validation.Valid;
import me.marioscalas.saikata.infrastructure.ai.AIPromptService;
import me.marioscalas.saikata.infrastructure.ai.Answer;
import me.marioscalas.saikata.infrastructure.ai.CapitalQuestion;
import me.marioscalas.saikata.infrastructure.ai.Question;

@Component
public class OpenAIPromptServiceImpl implements AIPromptService {

    @Value("classpath:/templates/get-capital-prompt.st")
    private Resource getCapitalPromptTemplate;

    private final ChatModel chatModel;

    public OpenAIPromptServiceImpl(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @Override
    public Answer getAnswer(@Valid Question question) {
        final PromptTemplate promptTemplate = new PromptTemplate(question.text());
        final Prompt prompt = promptTemplate.create();

        final ChatResponse chatResponse = chatModel.call(prompt);
        
        return new Answer(chatResponse.getResult().getOutput().getText());
    }

    @Override
    public Answer getCapital(CapitalQuestion question) {
        final PromptTemplate promptTemplate = new PromptTemplate(getCapitalPromptTemplate);
        final Prompt prompt = promptTemplate.create(
            Map.of("stateOrCountry", question.stateOrCountry())
        );

        final ChatResponse chatResponse = chatModel.call(prompt);
        
        return new Answer(chatResponse.getResult().getOutput().getText());
    }
}
