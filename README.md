# Custom Agent

This project is a Java 21 Maven project using [LangChain4j](https://github.com/langchain4j/langchain4j) and [LangGraph4j](https://github.com/langchain4j/langgraph4j).

## Requirements
- Java 21+
- Maven 3.9+

## Build

```
mvn clean install
```

## Run

Add your Java source files under `src/main/java` and run your application with:

```
mvn exec:java -Dexec.mainClass="com.example.Main"
```

Replace `com.example.Main` with your actual main class. 