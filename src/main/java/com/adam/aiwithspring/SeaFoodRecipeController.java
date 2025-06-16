package com.adam.aiwithspring;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/recipes/sea-food")
public class SeaFoodRecipeController {

    private final ChatClient chatClient;

    public SeaFoodRecipeController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping("/suggest-recipe")
    public String SugguestRecipie(
            @RequestParam(
                    name = "message",
                    defaultValue = "I would like a simple seafood recipe for dinner"
            )
            String message) {
        final String systemMessage = """
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



        return this.chatClient.prompt()
                .system(c -> c.text(systemMessage))
                .user(message)
                .call()
                .content();
    }

}
