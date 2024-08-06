import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class App {
    static String notionDatabaseId;
    static String notionToken;
    public static void main(String[] args) throws Exception {
        // Read Environment, Default = dev
        String environment = System.getProperty("env", "dev");

        // Set path for config.properties
        String configFileName = "../config/" + environment + "/config.properties";

        // Load Properties
        Properties properties = new Properties();

        try (InputStream input = new FileInputStream(configFileName)) {
            // Load properties file
            properties.load(input);

            // Get properties
            notionDatabaseId = properties.getProperty("NOTION_DATABASE_ID", null);
            notionToken = properties.getProperty("NOTION_TOKEN", null);

            // Print properties
            System.out.println("Notion Database ID: " + notionDatabaseId);
            System.out.println("Notion Token: " + notionToken);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
