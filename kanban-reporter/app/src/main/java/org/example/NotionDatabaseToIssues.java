package org.example;

import java.util.ArrayList;

import org.example.records.Issue;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class NotionDatabaseToIssues {
    public static ArrayList<Issue> databaseToIssues(JsonArray database) {
        ArrayList<Issue> issues = new ArrayList<Issue>();

        for (JsonElement jsonElement : database) {
            JsonObject issueJson = jsonElement.getAsJsonObject();
            String id = KanbanJsonToProperty.getProperty(issueJson, "id");
            String title = KanbanJsonToProperty.getProperty(issueJson, "title");
            String start = KanbanJsonToProperty.getProperty(issueJson, "start");
            String end = KanbanJsonToProperty.getProperty(issueJson, "end");
            ArrayList<String> assignees = KanbanJsonToProperty.getAssignees(issueJson);
            issues.add(
                new Issue(id, title, start, end, assignees)
            );
        }
        return issues;
    }
}
