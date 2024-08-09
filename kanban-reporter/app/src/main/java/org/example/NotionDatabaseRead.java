package org.example;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NotionDatabaseRead {
    private final String NOTION_TOKEN;
    private final String DATABASE_ID;
    private final String ENVIRONMENT;

    public NotionDatabaseRead(String environment, String token, String databaseId) {
        NOTION_TOKEN = token;
        DATABASE_ID = databaseId;
        ENVIRONMENT = environment;
    }

    public JsonArray readDatabase() {
        try {
            OkHttpClient client = new OkHttpClient();
            
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            String bodyString = GetResources.getQuery(ENVIRONMENT).toJSONString();
            RequestBody body = RequestBody.create(bodyString, JSON);

            Request request = new Request.Builder()
                .url(GetResources.getProperty("NOTIONAPI_BASE_URL") + DATABASE_ID + "/query")
                .addHeader("Authorization", "Bearer " + NOTION_TOKEN)
                .addHeader("Notion-Version", "2022-06-28")
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

            Response response = client.newCall(request).execute();
            
            if (response.isSuccessful()) {
                JsonObject jsonObject = JsonParser.parseString(response.body().string()).getAsJsonObject();
                JsonArray result = jsonObject.getAsJsonArray("results");
                return result;
            }
            else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }
}
