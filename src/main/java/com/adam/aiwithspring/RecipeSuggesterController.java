package com.adam.aiwithspring;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/recipes/suggester")
public class RecipeSuggesterController {
    private final ChatClient chatClient;


    public RecipeSuggesterController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping
    public List<String> suggestRecipeFromIngredient(
            @RequestParam(
                    name="ingredients",
                    defaultValue = "Shrimp"
            )String ingredient
    ){
        final ListOutputConverter listOutputConverter = new ListOutputConverter(new DefaultConversionService());

        String message = """
        please suggest me the best 10
        dishes containing the ingredient %s.
        just say i don't know if you don't know the answer.
        %s
        """.formatted(ingredient.trim(), listOutputConverter.getFormat());

        String response = this.chatClient.prompt()
                .user(message)
                .call()
                .content();

        return listOutputConverter.convert(response);
    }


}
