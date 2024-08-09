package org.example;

import java.util.ArrayList;

import org.example.records.Issue;

import com.google.gson.JsonArray;

public class Main {

    public static void main(String[] args) {
        // Read Environment, Default = dev
        String environment = System.getenv("env");
        if (environment == null || (!environment.equals("dev") && !environment.equals("prod"))) {
            environment = "dev";
        }
        System.out.println(environment);

        // Read Notion and Get Database
        JsonArray database = NotionDatabaseRead.readDatabase(environment, GetResources.getProperty("NOTION_TOKEN", environment), GetResources.getProperty("NOTION_DATABASE_ID", environment));
        
        // Organize Database To Issue List
        ArrayList<Issue> issues = NotionDatabaseToIssues.databaseToIssues(database);

        // Transform Issue List To Texts For Slack
        ArrayList<String> textsToSend = IssuesToSlackText.issuesToTexts(environment, issues);

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
