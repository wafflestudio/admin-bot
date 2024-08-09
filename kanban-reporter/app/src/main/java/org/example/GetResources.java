package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class GetResources {
    public static String getProperty(String key, String environment) {
        // Load Properties
        Properties properties = new Properties();

        try (InputStream input = Main.class.getClassLoader().getResourceAsStream(environment + "/config.properties")) {
            if (input == null) return "";

            // Load property
            properties.load(input);
            return properties.getProperty(key, "");
        } catch (IOException e) {
            return "";
        }
    }

    public static String getProperty(String key) {
        return getProperty(key, "main");
    }
}
