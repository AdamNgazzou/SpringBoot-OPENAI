package com.adam.aiwithspring;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/recipes/chef")
public class RecipeChefController {

        private final List<Message> conversation;

        private final ChatClient chatClient;

        public RecipeChefController(ChatClient.Builder chatClientBuilder) {
            this.chatClient = chatClientBuilder.build();
            this.conversation = new ArrayList<>();
            final String systemMessageString = """
                You are a helpful seafood recipe expert. When suggesting recipes:
                1. If allergies or dietary restrictions are mentioned, adapt the recipe accordingly
                2. If no allergies or restrictions are mentioned, assume there are none and provide a recipe immediately
                3. Never ask about allergies or restrictions - only consider them if explicitly mentioned
                
                Always provide a complete seafood recipe in this format:
                - Recipe name
                - Ingredients list
                - Cooking instructions
                
                Keep the recipe simple and straightforward but delicious.
                """;
            final SystemMessage systemMessage = new SystemMessage(systemMessageString);
            this.conversation.add(systemMessage);

        }

    @GetMapping("/suggest-recipe")
    public String SugguestRecipie(
            @RequestParam(
                    name = "message",
                    defaultValue = "I would like a simple seafood recipe for dinner"
            )
            String message) {

        final Message userMessage = new UserMessage(message);
        this.conversation.add(userMessage);
        String modelResponse = this.chatClient.prompt()
                .messages(this.conversation)
                .call()
                .content();
        final Message assistantMessage = new AssistantMessage(modelResponse);
        this.conversation.add(assistantMessage);
        return modelResponse;
    }



}
