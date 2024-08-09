package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class GetResources {
    public static String getProperty(String key, String environment) {
        // Load Properties
        Properties properties = new Properties();

        try (InputStream input = Main.class.getClassLoader().getResourceAsStream(environment + "/config.properties")) {
            if (input == null) return "";

            // Load property
            InputStreamReader reader = new InputStreamReader(input, "UTF-8");
            properties.load(reader);
            return properties.getProperty(key, "");
        } catch (IOException e) {
            return "";
        }
    }

    public static String getProperty(String key) {
        return getProperty(key, "main");
    }

    public static JSONObject getQuery(String environment) {
        // JSONParser 객체 생성
        JSONParser parser = new JSONParser();

        // ClassLoader를 사용하여 리소스 파일 읽기
        try (InputStream input = Main.class.getClassLoader().getResourceAsStream(environment + "/filter.json")) {
            if (input == null) {
                return null;
            }
            JSONObject jsonObject = (JSONObject) parser.parse(new InputStreamReader(input, "UTF-8"));
            return jsonObject;
        } catch (Exception e) {
            return null;
        }
    }
}
