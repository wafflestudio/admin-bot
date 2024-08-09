package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

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
}
