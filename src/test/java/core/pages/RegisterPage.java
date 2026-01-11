package core.pages;

import core.elements.RegisterPageElements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object for the Register page.
 * Handles registration form interactions and success feedback.
 */
public class RegisterPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public RegisterPage(WebDriver driver, int timeoutSeconds) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
    }
    /**
     * Returns the page title text from the Register page.
     */
    public String getTitleText() {
        WebElement title = wait.until(ExpectedConditions.visibilityOfElementLocated(RegisterPageElements.PAGE_TITLE));
        return title.getText().trim();
    }

    /**
     * Generates a unique email based on current timestamp.
     * Useful for avoiding duplicate account issues in tests.
     */
    public String generateUniqueEmail() {
        return "testuser_" + System.currentTimeMillis() + "@gmail.com";
    }
    /**
     * Fills the registration form fields with provided data.
     */
    public void fillRegisterForm(String firstName, String middleName, String lastName, String email, String password) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(RegisterPageElements.FIRST_NAME)).sendKeys(firstName);
        driver.findElement(RegisterPageElements.MIDDLE_NAME).sendKeys(middleName);
        driver.findElement(RegisterPageElements.LAST_NAME).sendKeys(lastName);
        driver.findElement(RegisterPageElements.EMAIL).sendKeys(email);
        driver.findElement(RegisterPageElements.PASSWORD).sendKeys(password);
        driver.findElement(RegisterPageElements.CONFIRM_PASSWORD).sendKeys(password);
    }

    /**
     * Scrolls to and clicks the Register button using JavaScript for stability.
     */
    public void clickRegister() {
        WebElement btn = wait.until(ExpectedConditions.visibilityOfElementLocated(RegisterPageElements.REGISTER_BUTTON));

        // Ensure the button is in viewport before interacting
        ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block:'center'});", btn);

        // Wait until the button is clickable
        wait.until(ExpectedConditions.elementToBeClickable(btn));

        // Click via JavaScript to avoid interception/overlay issues
        ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("arguments[0].click();", btn);
    }

    /**
     * Returns the success message text shown after a successful registration.
     */
    public String getSuccessMessage() {
        WebElement msg = wait.until(ExpectedConditions.visibilityOfElementLocated(RegisterPageElements.SUCCESS_MESSAGE));
        return msg.getText().trim();
    }
}
