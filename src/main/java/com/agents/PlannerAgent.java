package com.agents;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public interface PlannerAgent {

    @SystemMessage("""
        You are a planner agent. Given a user query, split it into smaller independent queries.
        For each, assign a category: "math" or "date".
        Return a JSON array of objects with fields:
          - "query": the sub-query text
          - "category": either "math" or "date"
        
        Example input:
        "What is today and what is 2 plus 2?"
        
        Example output:
        [
          {"query": "What is today?", "category": "date"},
          {"query": "What is 2 plus 2?", "category": "math"}
        ]
        """)
    String plan(String input);


    @UserMessage("What is today and what is 2 plus 2?")
    String example1();
}
