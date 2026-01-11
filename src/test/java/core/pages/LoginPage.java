package core.pages;

import core.elements.LoginPageElements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
/**
 * Page Object for Login page.
 */

public class LoginPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public LoginPage(WebDriver driver, int timeoutSeconds) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
    }
    /**
     * Returns the header/title text displayed on the Login page.
     */
    public String getTitleText() {
        WebElement title = wait.until(
                ExpectedConditions.visibilityOfElementLocated(LoginPageElements.PAGE_TITLE)
        );
        return title.getText().trim();
    }
    /**
     * Performs login using the provided email and password.
     * Uses scroll + JS-click as a fallback for elements partially obstructed or
     * outside the viewport.
     */
    public void login(String email, String password) {

        // Populate email
        wait.until(ExpectedConditions.visibilityOfElementLocated(LoginPageElements.EMAIL)).clear();
        driver.findElement(LoginPageElements.EMAIL).sendKeys(email);

        // Populate password
        driver.findElement(LoginPageElements.PASSWORD).clear();
        driver.findElement(LoginPageElements.PASSWORD).sendKeys(password);

        // Ensure login button is visible and ready to interact

        WebElement loginBtn = wait.until(
                ExpectedConditions.visibilityOfElementLocated(LoginPageElements.LOGIN_BUTTON)
        );

        // Scroll into view to avoid click interception issues

        ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block:'center'});", loginBtn);

        wait.until(ExpectedConditions.elementToBeClickable(loginBtn));

        // Click via JavaScript as a reliable fallback

        ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("arguments[0].click();", loginBtn);
    }
}
