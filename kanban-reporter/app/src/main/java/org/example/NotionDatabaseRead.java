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
    public static JsonArray readDatabase(String environment, String notionToken, String databaseId) {
        try {
            OkHttpClient client = new OkHttpClient();
            
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            String bodyString = GetResources.getQuery(environment).toJSONString();
            RequestBody body = RequestBody.create(bodyString, JSON);

            Request request = new Request.Builder()
                .url(GetResources.getProperty("NOTIONAPI_BASE_URL") + databaseId + "/query")
                .addHeader("Authorization", "Bearer " + notionToken)
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
