import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class App {

    public static void main(String[] args) throws Exception {
        // Read Environment, Default = dev
        String environment = System.getProperty("env", "dev");

        Map<String, String> secrets = new HashMap<>();
        secrets = load_secrets(environment);
    }

    private static Map<String, String> load_secrets(String environment) {
        // Set path for config.properties
        String configFileName = "../../../resources/" + environment + "/config.properties";

        // Load Properties
        Properties properties = new Properties();

        try (InputStream input = new FileInputStream(configFileName)) {
            Map<String, String> secrets = new HashMap<>();
            String notionDatabaseId;
            String notionToken;

            // Load properties file
            properties.load(input);

            // Get properties
            notionDatabaseId = properties.getProperty("NOTION_DATABASE_ID", null);
            notionToken = properties.getProperty("NOTION_TOKEN", null);

            // Fill HashMap
            secrets.put("notionDatabaseId", notionDatabaseId);
            secrets.put("notionToken", notionToken);
            
            return secrets;
        } catch (IOException e) {
            return new HashMap<>();
        }
    }
}
