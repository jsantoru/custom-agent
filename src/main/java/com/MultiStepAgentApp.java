package com;

import com.agents.DateAgent;
import com.agents.MathAgent;
import com.agents.PlannerAgent;

import com.tools.DateTools;
import com.tools.MathTools;

import dev.langchain4j.service.AiServices;
import dev.langchain4j.model.openai.OpenAiChatModel;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MultiStepAgentApp {

    private static final ObjectMapper mapper = new ObjectMapper();

    private static final String OPENAI_API_KEY = System.getenv("OPENAI_API_KEY");
    private static final String MODEL_NAME = "gpt-4.1-nano";
    public static void main(String[] args) throws Exception {

        MathAgent mathAgent = AiServices.builder(MathAgent.class)
                .tools(new MathTools())
                .chatModel(OpenAiChatModel.builder().apiKey(OPENAI_API_KEY).modelName(MODEL_NAME).build())
                .build();

        DateAgent dateAgent = AiServices.builder(DateAgent.class)
                .tools(new DateTools())
                .chatModel(OpenAiChatModel.builder().apiKey(OPENAI_API_KEY).modelName(MODEL_NAME).build())
                .build();

        PlannerAgent plannerAgent = AiServices.builder(PlannerAgent.class)
                .chatModel(OpenAiChatModel.builder().apiKey(OPENAI_API_KEY).modelName(MODEL_NAME).build())
                .build();

        // Example complex query
        String complexQuery = "What is today and what is 2 plus 2?";

        // Step 1: Planner splits the query
        String planJson = plannerAgent.plan(complexQuery);

        System.out.println("Planner output (JSON): " + planJson);

        // Step 2: Parse JSON and run each sub-query on the correct agent
        JsonNode planArray = mapper.readTree(planJson);

        List<String> answers = new ArrayList<>();

        for (JsonNode item : planArray) {
            String subQuery = item.get("query").asText();
            String category = item.get("category").asText();

            String answer;
            if ("math".equalsIgnoreCase(category)) {
                answer = mathAgent.solve(subQuery);
            } else if ("date".equalsIgnoreCase(category)) {
                answer = dateAgent.solve(subQuery);
            } else {
                answer = "Sorry, I cannot answer that.";
            }

            answers.add(answer);
        }

        // Step 3: Aggregate answers
        String finalAnswer = String.join(" ", answers);
        System.out.println("Final aggregated answer:");
        System.out.println(finalAnswer);
    }
}
