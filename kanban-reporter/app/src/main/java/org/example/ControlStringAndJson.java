package org.example;

import com.google.gson.Gson;

import org.json.simple.JSONObject;

public class ControlStringAndJson {
    public static JSONObject stringToJson(String text) {
        Gson gson = new Gson();
        JSONObject jsonObject = gson.fromJson(text, JSONObject.class);

        return jsonObject;
    }
}
