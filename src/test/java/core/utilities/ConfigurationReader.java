package core.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * ConfigurationReader:
 * Loads key/value settings from config.properties located under src/test/resources.
 * Provides simple accessor methods for retrieving config values.
 */
public class ConfigurationReader {

    private static final Properties props = new Properties();

    static {
        // Load config.properties once at class initialization

        try (InputStream input = ConfigurationReader.class
                .getClassLoader()
                .getResourceAsStream("config.properties")) {

            if (input == null) {
                // Critical failure if missing configuration file

                throw new RuntimeException("config.properties not found in src/test/resources");
            }

            props.load(input);
        } catch (IOException e) {
            // Fail fast: test execution should not continue without configuration

            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    /**
     * Retrieves a configuration value by key (as String).
     */
    public static String get(String key) {
        return props.getProperty(key);
    }
    /**
     * Retrieves a configuration value and parses it as int.
     */
    public static int getInt(String key) {
        return Integer.parseInt(get(key));
    }
}
