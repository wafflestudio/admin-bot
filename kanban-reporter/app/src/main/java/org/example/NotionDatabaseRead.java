package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class NotionDatabaseRead {
    private final String NOTION_TOKEN;
    private final String DATABASE_ID;

    public NotionDatabaseRead(String token, String databaseId) {
        NOTION_TOKEN = token;
        DATABASE_ID = databaseId;
    }

    public void readDatabase() {
        List<Map<String, Object>> issues = new ArrayList<>();

        try {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                .url("https://api.notion.com/v1/databases/" + DATABASE_ID)
                .build();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}
