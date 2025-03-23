package me.marioscalas.saikata.ai.internal;

import java.util.Map;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import jakarta.validation.Valid;
import me.marioscalas.saikata.ai.AIPromptService;
import me.marioscalas.saikata.ai.model.Answer;
import me.marioscalas.saikata.ai.model.GetCapitalQuestion;
import me.marioscalas.saikata.ai.model.GetCapitalResponse;
import me.marioscalas.saikata.ai.model.Question;

@Component
public class OpenAIPromptServiceImpl implements AIPromptService {

    @Value("classpath:/templates/get-capital-prompt.st")
    private Resource getCapitalPromptTemplate;

    @Value("classpath:/templates/get-capital-prompt-with-info.st")
    private Resource getCapitalWithInfoPromptTemplate;

    @Value("classpath:/templates/get-capital-prompt-with-json-info.st")
    private Resource getCapitalWithJsonInfoPromptTemplate;

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
    public Answer getCapital(@Valid GetCapitalQuestion question) {
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
    public GetCapitalResponse getCapitalWithInfoV2(@Valid GetCapitalQuestion question) {
        final BeanOutputConverter<GetCapitalResponse> converter = new BeanOutputConverter<>(GetCapitalResponse.class);
        final String outputFormat = converter.getFormat();

        final PromptTemplate promptTemplate = new PromptTemplate(getCapitalWithJsonInfoPromptTemplate);
        final Prompt prompt = promptTemplate.create(
            Map.of("stateOrCountry", question.stateOrCountry(),
            "format", outputFormat)
        );

        final ChatResponse chatResponse = chatModel.call(prompt);

        return converter.convert(chatResponse.getResult().getOutput().getText());
    }
}
