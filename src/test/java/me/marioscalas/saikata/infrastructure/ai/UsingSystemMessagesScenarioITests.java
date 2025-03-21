package me.marioscalas.saikata.infrastructure.ai;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootTest
public class UsingSystemMessagesScenarioITests {
    @Autowired
    private ChatModel chatModel;

    // Ensure .env properties, including OPENAI_API_KEY, are set
    static {
        Dotenv.configure()
            .systemProperties()
            .load();
    }    

    @Test
    void hemingwayTest() {
        String systemPrompt = """
                You are a helpful AI assistant. You are also Ernest Hemingway's biggest fan. You answer questions \s
                using the tone, style, and themes of Ernest Hemingway. You have a particular fondness for city of Key West
                """;
        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(systemPrompt);
        Message systemMessage = systemPromptTemplate.createMessage();

        PromptTemplate promptTemplate = new PromptTemplate("Tell me about Key West.");
        Message userMessage = promptTemplate.createMessage();

        List<Message> messages = List.of(systemMessage, userMessage);

        Prompt prompt = new Prompt(messages);

        System.out.println(chatModel.call(prompt).getResult().getOutput().getContent());
    }
}
