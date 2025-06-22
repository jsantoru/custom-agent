# Custom Agent - Multi-Agent System

This project is a Java 21 Maven project demonstrating a multi-agent system using [LangChain4j](https://github.com/langchain4j/langchain4j) and [LangGraph4j](https://github.com/langchain4j/langgraph4j). The system implements a sophisticated agent architecture with specialized agents for different tasks.

## Project Structure

The main entry point is **`MultiStepAgentApp.java`**, which orchestrates a multi-agent system consisting of:

- **PlannerAgent**: Splits complex queries into sub-tasks
- **MathAgent**: Handles mathematical operations using MathTools
- **DateAgent**: Manages date-related queries using DateTools
- **RouterAgent**: Routes queries to appropriate specialized agents

## Requirements
- Java 21+
- Maven 3.9+
- OpenAI API Key (set as environment variable `OPENAI_API_KEY`)

## Build

```bash
mvn clean install
```

## Run

### Main Application (Recommended)
The primary entry point is `MultiStepAgentApp.java` which demonstrates the full multi-agent system:

```bash
mvn exec:java -Dexec.mainClass="com.MultiStepAgentApp"
```

### Simple Hello World
For a basic test, you can run the simple `Main.java`:

```bash
mvn exec:java -Dexec.mainClass="com.Main"
```

## How It Works

1. **Query Processing**: The system receives complex queries that may require multiple types of processing
2. **Planning**: The PlannerAgent breaks down complex queries into sub-tasks
3. **Specialized Processing**: Each sub-task is routed to the appropriate specialized agent:
   - Math operations → MathAgent
   - Date queries → DateAgent
4. **Result Aggregation**: Results from all agents are combined into a final response

## Example Usage

The system can handle queries like:
- "What is today and what is 2 plus 2?"
- "Calculate the date 30 days from now and add 15 to 25"

## Configuration

Make sure to set your OpenAI API key as an environment variable:
```bash
export OPENAI_API_KEY="your-api-key-here"
```

## Development

The project uses VS Code with Java extensions. Debug configurations are provided in `.vscode/launch.json` for easy development and debugging. 