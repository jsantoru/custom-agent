package com.agents;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public interface RouterAgent {

    @SystemMessage("""
        You are a routing agent that classifies user requests into exactly one category: "math", "date", or "default".

        Route to "math" if the request involves calculations, numbers, or any of these operations:
        - multiply: Multiply two numbers.
        - square: Return the square of an integer.
        - add: Add two numbers together.
        - subtract: Subtract the second number from the first.
        - divide: Divide the first number by the second (avoid division by zero).
        - percentage: Calculate what percent the first number is of the second.

        Route to "date" if the request is about today’s date, future dates, or calendar/time.

        Otherwise, route to "default".

        Respond with only the category string without explanation or extra text.
        """)
    String route(String input);

    // Few-shot examples for better routing accuracy
    @UserMessage("What is 5 plus 10?")
    String example1();

    @UserMessage("What day is it today?")
    String example2();

    @UserMessage("Tell me a joke.")
    String example3();
}
