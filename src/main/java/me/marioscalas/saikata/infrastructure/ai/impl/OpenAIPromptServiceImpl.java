package me.marioscalas.saikata.infrastructure.ai.impl;

import java.util.Map;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.Valid;
import me.marioscalas.saikata.infrastructure.ApplicationException;
import me.marioscalas.saikata.infrastructure.ai.AIPromptService;
import me.marioscalas.saikata.infrastructure.ai.Answer;
import me.marioscalas.saikata.infrastructure.ai.GetCapitalQuestion;
import me.marioscalas.saikata.infrastructure.ai.GetCapitalResponse;
import me.marioscalas.saikata.infrastructure.ai.Question;

@Component
public class OpenAIPromptServiceImpl implements AIPromptService {

    @Value("classpath:/templates/get-capital-prompt.st")
    private Resource getCapitalPromptTemplate;

    @Value("classpath:/templates/get-capital-prompt-with-info.st")
    private Resource getCapitalWithInfoPromptTemplate;

    @Value("classpath:/templates/get-capital-prompt-with-json-info.st")
    private Resource getCapitalWithJsonInfoPromptTemplate;

    private final ChatModel chatModel;
    
    private final ObjectMapper objectMapper;
    
    public OpenAIPromptServiceImpl(ChatModel chatModel, ObjectMapper objectMapper) {
        this.chatModel = chatModel;
        this.objectMapper = objectMapper;
    }

    @Override
    public Answer getAnswer(@Valid Question question) {
        final PromptTemplate promptTemplate = new PromptTemplate(question.text());
        final Prompt prompt = promptTemplate.create();

        final ChatResponse chatResponse = chatModel.call(prompt);
        
        return new Answer(chatResponse.getResult().getOutput().getText());
    }

    @Override
    public Answer getCapital(GetCapitalQuestion question) {
        final PromptTemplate promptTemplate = new PromptTemplate(getCapitalPromptTemplate);
        final Prompt prompt = promptTemplate.create(
            Map.of("stateOrCountry", question.stateOrCountry())
        );

        final ChatResponse chatResponse = chatModel.call(prompt);
        
        return new Answer(chatResponse.getResult().getOutput().getText());
    }

    @Override
    public Answer getCapitalWithInfo(@Valid GetCapitalQuestion question) {
        final PromptTemplate promptTemplate = new PromptTemplate(getCapitalWithInfoPromptTemplate);
        final Prompt prompt = promptTemplate.create(
            Map.of("stateOrCountry", question.stateOrCountry())
        );

        final ChatResponse chatResponse = chatModel.call(prompt);
        
        return new Answer(chatResponse.getResult().getOutput().getText());
    }

    @Override
    public GetCapitalResponse getCapitalWithInfoV2(GetCapitalQuestion question) {
        final BeanOutputConverter<GetCapitalResponse> converter = new BeanOutputConverter<>(GetCapitalResponse.class);
        final String outputFormat = converter.getFormat();

        final PromptTemplate promptTemplate = new PromptTemplate(getCapitalWithJsonInfoPromptTemplate);
        final Prompt prompt = promptTemplate.create(
            Map.of("stateOrCountry", question.stateOrCountry(),
            "format", outputFormat)
        );

        final ChatResponse chatResponse = chatModel.call(prompt);

        try {
            return objectMapper.readValue(chatResponse.getResult().getOutput().getText(), GetCapitalResponse.class);
        } catch (Exception e) {
            throw new ApplicationException("Error converting response", e);
        }
    }
}
