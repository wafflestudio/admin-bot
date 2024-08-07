package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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
            
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            String jsonString = "{}";

            RequestBody body = RequestBody.create(jsonString, JSON);

            Request request = new Request.Builder()
                .url("https://api.notion.com/v1/databases/" + DATABASE_ID + "/query")
                .addHeader("Authorization", "Bearer " + NOTION_TOKEN)
                .addHeader("Notion-Version", "2022-06-28")
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

            Response response = client.newCall(request).execute();

            System.out.println(response.code());
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
