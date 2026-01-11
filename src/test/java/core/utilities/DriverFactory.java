package core.utilities;


import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 * DriverFactory:
 * Provides a singleton WebDriver instance for test execution.
 * Ensures browser lifecycle is managed centrally via BaseTest.
 */
public class DriverFactory {

    // Singleton instance (shared within the test run)
    private static WebDriver driver;

    /**
     * Returns the active WebDriver instance or initializes a new one if none exists.
     */
    public static WebDriver getDriver() {
        if (driver == null) {
            // WebDriverManager handles driver binaries for Chrome
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver();
            // Standardize initial browser state
            driver.manage().window().maximize();
        }
        return driver;
    }

    /**
     * Gracefully closes the browser and clears the driver reference.
     */
    public static void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;  // allow recreation for next test class
        }
    }
}
