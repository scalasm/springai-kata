package me.marioscalas.saikata.infrastructure.ai.impl;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import me.marioscalas.saikata.infrastructure.ai.AIPromptService;
import me.marioscalas.saikata.infrastructure.ai.Answer;
import me.marioscalas.saikata.infrastructure.ai.Question;

@Component
public class OpenAIPromptServiceImpl implements AIPromptService {

    private final ChatModel chatModel;

    public OpenAIPromptServiceImpl(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @Override
    public Answer getAnswer(Question question) {
        Assert.hasText(question.text(), "Question must not be empty");
        
        final PromptTemplate promptTemplate = new PromptTemplate(question.text());
        final Prompt prompt = promptTemplate.create();

        final ChatResponse chatResponse = chatModel.call(prompt);
        
        return new Answer(chatResponse.getResult().getOutput().getText());
    }
}
