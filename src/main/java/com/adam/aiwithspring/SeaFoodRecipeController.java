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
                    defaultValue = "Suggest a recipe for dinner"
            )
            String message) {
        final String systemMessage = """
                Suggest Sea Food recipe. If someone asks about Something else just say i don't know
                """;
        return this.chatClient.prompt()
                .system(c -> c.text(systemMessage))
                .user(message)
                .call()
                .content();
    }

}
