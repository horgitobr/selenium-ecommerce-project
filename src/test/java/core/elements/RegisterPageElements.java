package core.elements;

import org.openqa.selenium.By;
/**
 * Locators for Register page form fields and validation messages.
 */
public class RegisterPageElements {

    // ===== Form Fields =====
    public static final By FIRST_NAME = By.id("firstname");
    public static final By MIDDLE_NAME = By.id("middlename");
    public static final By LAST_NAME = By.id("lastname");
    public static final By EMAIL = By.id("email_address");
    public static final By PASSWORD = By.id("password");
    public static final By CONFIRM_PASSWORD = By.id("confirmation");

    // ===== Actions =====
    public static final By REGISTER_BUTTON = By.cssSelector("button[title='Register']");

    // ===== Validation / Feedback =====
    public static final By SUCCESS_MESSAGE = By.cssSelector("li.success-msg span");

    // Page title
    public static final By PAGE_TITLE = By.cssSelector("div.page-title h1");
}
