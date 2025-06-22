package com.tools;


import dev.langchain4j.agent.tool.Tool;

import java.time.LocalDate;

public class DateTools {

    @Tool(name = "today", value = "Returns today's date.")
    public String today() {
        return "Today is: " + LocalDate.now();
    }

    @Tool(name = "inDays", value = "Returns the date after adding the given number of days to today.")
    public String inDays(int days) {
        return "In " + days + " days: " + LocalDate.now().plusDays(days);
    }
}