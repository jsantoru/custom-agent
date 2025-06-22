package com.tools;

import dev.langchain4j.agent.tool.Tool;

public class MathTools {

    @Tool(name = "multiply", value = "Multiply two numbers.")
    public int multiply(int a, int b) {
        return a * b;
    }

    @Tool(name = "square", value = "Return the square of an integer.")
    public int square(int x) {
        return x * x;
    }

    @Tool(name = "add", value = "Add two numbers together.")
    public int add(int a, int b) {
        return a + b;
    }

    @Tool(name = "subtract", value = "Subtract the second number from the first.")
    public int subtract(int a, int b) {
        return a - b;
    }

    @Tool(name = "divide", value = "Divide the first number by the second. Avoid division by zero.")
    public double divide(double a, double b) {
        if (b == 0) throw new IllegalArgumentException("Cannot divide by zero");
        return a / b;
    }

    @Tool(name = "percentage", value = "Calculate what percent the first number is of the second.")
    public double percentage(double part, double total) {
        return (part / total) * 100;
    }
}