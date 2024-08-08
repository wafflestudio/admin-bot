package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class App {

    public static void main(String[] args) {
        // Read Environment, Default = dev
        String environment = System.getenv("env");
        if (environment == null) {
            environment = "dev";
        }
        System.out.println(environment);
        
        // Read Secrets
        Map<String, String> secrets = new HashMap<>();
        secrets = load_secrets(environment);
        // System.out.println(secrets);

        // Read Notion and Get Issues
        NotionDatabaseRead notionDatabaseRead = new NotionDatabaseRead(environment, secrets.get("notionToken"), secrets.get("notionDatabaseId"));
        JsonArray database = notionDatabaseRead.readDatabase();
        
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
        NotionToSlack notionToSlack = new NotionToSlack();
        ArrayList<String> textsToSend = notionToSlack.issuesToTexts(issues);

        // Create Slack Thread
        SlackBot slackBot = new SlackBot(secrets.get("slackBotToken"), secrets.get("slackChannelId"));
        String threadTs = slackBot.createThread("Initial Test");

        // Create Comments Into The Thread
        Boolean success = true;
        for (String comment : textsToSend) {
            success = slackBot.createComment(threadTs, comment);
        }
        System.out.println(success);
    }

    private static Map<String, String> load_secrets(String environment) {
        // Load Properties
        Properties properties = new Properties();

        try (InputStream input = App.class.getClassLoader().getResourceAsStream(environment + "/config.properties")) {
            Map<String, String> secrets = new HashMap<>();
            if (input == null) {
                return secrets;
            }

            // Load properties file
            properties.load(input);

            // Get properties
            String notionDatabaseId = properties.getProperty("NOTION_DATABASE_ID", null);
            String notionToken = properties.getProperty("NOTION_TOKEN", null);
            String slackChannelId = properties.getProperty("SLACK_CHANNEL_ID", null);
            String slackBotToken = properties.getProperty("SLACK_BOT_TOKEN", null);

            // Fill HashMap
            secrets.put("notionDatabaseId", notionDatabaseId);
            secrets.put("notionToken", notionToken);
            secrets.put("slackBotToken", slackBotToken);
            secrets.put("slackChannelId", slackChannelId);
            
            return secrets;
        } catch (IOException e) {
            return new HashMap<>();
        }
    }
}
