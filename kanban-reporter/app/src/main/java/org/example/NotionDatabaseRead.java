package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;

public class NotionDatabaseRead {
    private final String NOTION_TOKEN;
    private final String DATABASE_ID;

    public NotionDatabaseRead(String token, String databaseId) {
        NOTION_TOKEN = token;
        DATABASE_ID = databaseId;
    }

    public void readDatabase() {
        OkHttpClient notionClient = new OkHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();
    }
}
