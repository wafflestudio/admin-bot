package org.example;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class NotionDatabaseToIssues {
    public static ArrayList<HashMap<String, String>> databaseToIssues(JsonArray database) {
        ArrayList<HashMap<String, String>> issues = new ArrayList<HashMap<String, String>>();

        for (JsonElement jsonElement : database) { // fix: 함수 분리
            HashMap<String, String> issueProperties = new HashMap<String, String>();
            JsonObject issue = jsonElement.getAsJsonObject();
            issueProperties.putAll(KanbanJsonToProperty.getProperty(issue, "id"));
            issueProperties.putAll(KanbanJsonToProperty.getProperty(issue, "title"));
            issueProperties.putAll(KanbanJsonToProperty.getProperty(issue, "start"));
            issueProperties.putAll(KanbanJsonToProperty.getProperty(issue, "end"));
            issueProperties.putAll(KanbanJsonToProperty.getProperty(issue, "assignees"));
            issues.add(issueProperties);
        }
        return issues;
    }
}
