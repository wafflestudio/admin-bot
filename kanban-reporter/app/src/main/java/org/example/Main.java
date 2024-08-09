package org.example;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Main {

    public static void main(String[] args) {
        // Read Environment, Default = dev
        String environment = System.getenv("env");
        if (environment == null || (environment != "dev" && environment != "prod")) {
            environment = "dev";
        }
        System.out.println(environment);

        // Read Notion and Get Issues
        JsonArray database = NotionDatabaseRead.readDatabase(environment, GetResources.getProperty("NOTION_TOKEN", environment), GetResources.getProperty("NOTION_DATABASE_ID", environment));
        
        // Filter Needed Properties
        NotionDatabaseToProperties notionDatabaseToProperties = new NotionDatabaseToProperties();
        ArrayList<HashMap<String, String>> issues = new ArrayList<HashMap<String, String>>();

        for (JsonElement jsonElement : database) { // fix: 함수 분리
            HashMap<String, String> issueProperties = new HashMap<String, String>();
            JsonObject issue = jsonElement.getAsJsonObject();
            issueProperties.putAll(notionDatabaseToProperties.getProperty(issue, "id"));
            issueProperties.putAll(notionDatabaseToProperties.getProperty(issue, "title"));
            issueProperties.putAll(notionDatabaseToProperties.getProperty(issue, "start"));
            issueProperties.putAll(notionDatabaseToProperties.getProperty(issue, "end"));
            issueProperties.putAll(notionDatabaseToProperties.getProperty(issue, "assignees"));
            issues.add(issueProperties);
        }

        // Get Text To Send To Slack
        ArrayList<String> textsToSend = NotionToSlack.issuesToTexts(environment, issues);

        // Create Slack Thread
        String slackBotToken = GetResources.getProperty("SLACK_BOT_TOKEN", environment);
        String slackChannelId = GetResources.getProperty("SLACK_CHANNEL_ID", environment);
        String threadTs = SlackBot.createThread(slackBotToken, slackChannelId, GetResources.getProperty("THREAD_NAME"));

        // Create Comments Into The Thread
        Boolean success = true;
        for (String comment : textsToSend) {
            success = SlackBot.createComment(slackBotToken, slackChannelId, threadTs, comment);
        }
        System.out.println(success);
    }
}
