package core.utilities;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * WaitUtils:
 * Common explicit wait utilities for WebDriver interactions.
 * Helps improve test stability by waiting for UI conditions.
 */
public class WaitUtils {

    // Waits until a single element becomes visible.
    public static WebElement waitForVisibility(WebDriver driver, WebElement element, int seconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
        return wait.until(ExpectedConditions.visibilityOf(element));
    }

    // Waits until all elements in a list become visible.
    // Useful when expecting a grid/list/table to finish rendering.
    public static void waitForAllVisible(WebDriver driver, List<WebElement> elements, int seconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
        wait.until(ExpectedConditions.visibilityOfAllElements(elements));
    }

    // Waits until an element becomes clickable (visible + enabled).
    // Commonly required before performing click actions.
    public static WebElement waitForClickable(WebDriver driver, WebElement element, int seconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(seconds));
        return wait.until(ExpectedConditions.elementToBeClickable(element));
    }
}
