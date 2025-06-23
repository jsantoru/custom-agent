package com;

import com.agents.DateAgent;
import com.agents.MathAgent;
import com.agents.PlannerAgent;
import com.tools.DateTools;
import com.tools.MathTools;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.graph.GraphState;
import com.graph.SimpleState;

import dev.langchain4j.service.AiServices;
// import org.bsc.langgraph4j;
// import dev.langchain4j.langgraph.GraphBuilder;
// import dev.langchain4j.langgraph.graph.State;

import dev.langchain4j.model.openai.OpenAiChatModel;

import java.util.*;

import org.bsc.langgraph4j.action.NodeAction;
import java.util.Collections;
import java.util.Map;

import org.bsc.langgraph4j.StateGraph;
import org.bsc.langgraph4j.GraphStateException;
import static org.bsc.langgraph4j.action.AsyncNodeAction.node_async;
import static org.bsc.langgraph4j.StateGraph.START;
import static org.bsc.langgraph4j.StateGraph.END;

// Node that adds a greeting
class GreeterNode implements NodeAction<SimpleState> {
    @Override
    public Map<String, Object> apply(SimpleState state) {
        System.out.println("GreeterNode executing. Current messages: " + state.messages());
        return Map.of(SimpleState.MESSAGES_KEY, "Hello from GreeterNode!");
    }
}

// Node that adds a response
class ResponderNode implements NodeAction<SimpleState> {
    @Override
    public Map<String, Object> apply(SimpleState state) {
        System.out.println("ResponderNode executing. Current messages: " + state.messages());
        List<String> currentMessages = state.messages();
        if (currentMessages.contains("Hello from GreeterNode!")) {
            return Map.of(SimpleState.MESSAGES_KEY, "Acknowledged greeting!");
        }
        return Map.of(SimpleState.MESSAGES_KEY, "No greeting found.");
    }
}

// new nodes

class ProcessNextNode implements NodeAction<GraphState> {
    @Override
    public Map<String, Object> apply(GraphState state) {
        System.out.println("ProcessNextNode executing. Current messages: " + state.messages());
        return state.data(); // pass through
    }
}

class PlannerNode implements NodeAction<GraphState> {
    private static final String OPENAI_API_KEY = System.getenv("OPENAI_API_KEY");
    private static final String MODEL_NAME = "gpt-4.1-nano";


    @Override
    public Map<String, Object> apply(GraphState state) throws Exception {
        System.out.println("PlannerNode executing. Current messages: " + state.messages());

        PlannerAgent plannerAgent = AiServices.builder(PlannerAgent.class)
        .chatModel(OpenAiChatModel.builder().apiKey(OPENAI_API_KEY).modelName(MODEL_NAME).build())
        .build();

        List input =  (List) state.data().get(SimpleState.MESSAGES_KEY);
        System.out.println(input.get(0));
        String json = plannerAgent.plan((String)input.get(0));
        System.out.println(json); // finds the category!!!
        /*
         * [
             {"query": "What is 2 plus 2?", "category": "math"}
            ]
         */

         // TODO: update this state machine to work with the JsonNodes that expect categories (aka tool names)

        //state.tasks = new ObjectMapper().readTree(json).findValues(".");
        return Map.of( SimpleState.MESSAGES_KEY, "4" );
    }
}

public class LangGraphApp {
    private static final String OPENAI_API_KEY = System.getenv("OPENAI_API_KEY");
    private static final String MODEL_NAME = "gpt-4.1-nano";


    
    public static void main(String[] args) throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        // Build agents
        MathAgent mathAgent = AiServices.builder(MathAgent.class)
        .tools(new MathTools())
        .chatModel(OpenAiChatModel.builder().apiKey(OPENAI_API_KEY).modelName(MODEL_NAME).build())
        .build();

        DateAgent dateAgent = AiServices.builder(DateAgent.class)
        .tools(new DateTools())
        .chatModel(OpenAiChatModel.builder().apiKey(OPENAI_API_KEY).modelName(MODEL_NAME).build())
        .build();

 

        // LangGraph steps
        // Graph<QueryState> graph = GraphBuilder.<QueryState>builder()
        //         .addNode("planner", (state) -> {
        //             String input = System.getenv("USER_QUERY");
        //             String json = plannerAgent.plan(input);
        //             state.tasks = mapper.readTree(json).findValues(".");
        //             return state;
        //         })

        // Initialize nodes
        GreeterNode greeterNode = new GreeterNode();
        ResponderNode responderNode = new ResponderNode();
        ProcessNextNode processNextNode = new ProcessNextNode();
        PlannerNode plannerNode = new PlannerNode();


        // Define the graph structure
       var stateGraph = new StateGraph<>(GraphState.SCHEMA, initData -> new GraphState(initData))
            //.addNode("greeter", node_async(greeterNode))
            //.addNode("responder", node_async(responderNode))
            .addNode("processNext", node_async(new ProcessNextNode()))
            .addNode("planner", node_async(new PlannerNode()))
            
            // Define edges
            .addEdge(START, "processNext") // Start with the greeter node
            .addEdge("processNext", "planner")
            //.addEdge("planner", "processNext")
            .addEdge("planner", END);   // End after the responder node

        // Compile the graph
        var compiledGraph = stateGraph.compile();

        // Run the graph
        // The `stream` method returns an AsyncGenerator.
        // For simplicity, we'll collect results. In a real app, you might process them as they arrive.
        // Here, the final state after execution is the item of interest.
        
        for (var item : compiledGraph.stream( Map.of( SimpleState.MESSAGES_KEY, "What is 2 plus 2?" ) ) ) {

            System.out.println( item );
        }

                

        //         .addNode("math", (state) -> {
        //             String query = state.tasks.get(state.index).get("query").asText();
        //             String result = mathAgent.solve(query);
        //             state.results.add(result);
        //             state.index++;
        //             return state;
        //         })

        //         .addNode("date", (state) -> {
        //             String query = state.tasks.get(state.index).get("query").asText();
        //             String result = dateAgent.solve(query);
        //             state.results.add(result);
        //             state.index++;
        //             return state;
        //         })

        //         .addNode("done", (state) -> {
        //             System.out.println("Final Answer: " + String.join(" ", state.results));
        //             return state;
        //         })

        //         .addEdge("planner", "process_next")

        //         .addEdge("process_next", "math", state ->
        //                 state.index < state.tasks.size() &&
        //                         "math".equalsIgnoreCase(state.tasks.get(state.index).get("category").asText()))

        //         .addEdge("process_next", "date", state ->
        //                 state.index < state.tasks.size() &&
        //                         "date".equalsIgnoreCase(state.tasks.get(state.index).get("category").asText()))

        //         .addEdge("process_next", "done", state ->
        //                 state.index >= state.tasks.size())

        //         .addEdge("math", "process_next")
        //         .addEdge("date", "process_next")

        //         .setEntryPoint("planner")
        //         .build();

        // // Example: inject query as env var or modify code to set it directly
        // System.setProperty("USER_QUERY", "What is today and what is 3 times 5 and what is 2 plus 2?");

        // QueryState finalState = graph.run(new QueryState());
    }
}