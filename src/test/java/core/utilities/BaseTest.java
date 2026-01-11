package core.utilities;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

/**
 * BaseTest:
 * Centralizes WebDriver setup and teardown for all test classes.
 * Each test inherits browser initialization from this class.
 */
public class BaseTest {

    protected WebDriver driver;

    @BeforeMethod
    public void setUp() {
        // Initialize WebDriver instance (browser type defined in DriverFactory).
        driver = DriverFactory.getDriver();
        // Navigate to base application URL (defined in config.properties).

        driver.get(ConfigurationReader.get("url"));
    }

    @AfterMethod
    public void tearDown() {
        // Clean up driver instance and close browser after each test.

        DriverFactory.quitDriver();
    }
}
