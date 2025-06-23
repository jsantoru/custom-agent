package com.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bsc.langgraph4j.state.AgentState;
import org.bsc.langgraph4j.state.Channel;
import org.bsc.langgraph4j.state.Channels;

import com.fasterxml.jackson.databind.JsonNode;

public class GraphState extends AgentState {
    public static final String MESSAGES_KEY = "messages";

    // Define the schema for the state.
    // MESSAGES_KEY will hold a list of strings, and new messages will be appended.
    public static final Map<String, Channel<?>> SCHEMA = Map.of(
            MESSAGES_KEY, Channels.appender(ArrayList::new)
    );

    public GraphState(Map<String, Object> initData) {
        super(initData);
    }

    public List<String> messages() {
        return this.<List<String>>value("messages")
                .orElse( List.of() );
    }

    // additional state 

    public List<JsonNode> tasks;
    public int index = 0;
    public List<String> results = new ArrayList<>();

    public List<JsonNode> getTasks() {
        return tasks;
    }

    public int getIndex() {
        return index;
    }

    public List<String> getResults() {
        return results;
    }
}
