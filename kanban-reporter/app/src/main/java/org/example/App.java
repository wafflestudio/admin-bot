package org.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class App {

    public static void main(String[] args) {
        // Read Environment, Default = dev
        String environment = System.getProperty("env", "dev");
        
        // Read Secrets
        Map<String, String> secrets = new HashMap<>();
        secrets = load_secrets(environment);
        // System.out.println(secrets);

        // Read Notion and Get Issues
        NotionDatabaseRead notionDatabaseRead = new NotionDatabaseRead(environment, secrets.get("notionToken"), secrets.get("notionDatabaseId"));
        JsonArray database = notionDatabaseRead.readDatabase();
        
        // Get Needed Properties
        NotionDatabaseToProperties notionDatabaseToProperties = new NotionDatabaseToProperties();

        JsonObject a = database.get(0).getAsJsonObject();
        System.out.println(notionDatabaseToProperties.getProperty(a, "id"));
        System.out.println(database.size());
    }

    private static Map<String, String> load_secrets(String environment) {
        // Set path for config.properties
        String configFileName = "app/src/main/resources/" + environment + "/config.properties";

        // Test
        // File file = new File(configFileName);
        // System.out.println("Trying to read file: " + file.getAbsolutePath());

        // Load Properties
        Properties properties = new Properties();

        try (InputStream input = new FileInputStream(configFileName)) {
            Map<String, String> secrets = new HashMap<>();
            String notionDatabaseId;
            String notionToken;

            // Load properties file
            properties.load(input);

            // Get properties
            notionDatabaseId = properties.getProperty("NOTION_DATABASE_ID", null);
            notionToken = properties.getProperty("NOTION_TOKEN", null);

            // Fill HashMap
            secrets.put("notionDatabaseId", notionDatabaseId);
            secrets.put("notionToken", notionToken);
            
            return secrets;
        } catch (IOException e) {
            return new HashMap<>();
        }
    }
}
