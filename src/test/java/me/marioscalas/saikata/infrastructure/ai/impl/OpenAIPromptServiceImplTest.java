package me.marioscalas.saikata.infrastructure.ai.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import me.marioscalas.saikata.SpringaiKataApplication;
import me.marioscalas.saikata.infrastructure.ai.Answer;
import me.marioscalas.saikata.infrastructure.ai.Question;

@SpringBootTest(classes = SpringaiKataApplication.class)
public class OpenAIPromptServiceImplTest {

    @Autowired
    private OpenAIPromptServiceImpl openAIPromptService;

    @Test
    public void testGetAnswer() {
        final Answer answer = openAIPromptService.getAnswer(new Question("Write a python script that can generate all numbers from 1 to 100"));
        System.out.println(answer);
    }
}