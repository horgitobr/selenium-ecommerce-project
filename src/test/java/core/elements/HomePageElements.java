package core.elements;

import org.openqa.selenium.By;
/**
 * Locators for Home Page navigation and account-related UI elements.
 * Used by HomePage (Page Object) and tests for top-level navigation flows.
 */
public class HomePageElements {

    // Account menu
    public static final By ACCOUNT_LINK = By.cssSelector("a.skip-account");

    // Dropdown options inside Account menu
    public static final By REGISTER_LINK = By.linkText("Register");
    public static final By SIGN_IN_LINK  = By.linkText("Log In");
    public static final By LOG_OUT_LINK  = By.linkText("Log Out");
    public static final By ACCOUNT_LABEL = By.cssSelector("span.label");  // Shown after expanding Account menu
    public static final By WELCOME_MSG = By.cssSelector("p.welcome-msg");  // Shown after successful login


    // ===== Top Navigation =====
    public static final By WOMAN_MENU = By.linkText("WOMEN");
    public static final By VIEW_ALL_WOMAN = By.linkText("View All Women");

    // Top menu - SALE

    public static final By SALE_MENU = By.linkText("SALE");

    // Top menu - MEN
    public static final By MEN_MENU = By.cssSelector("#nav a[href*='/men.html']");
    public static final By VIEW_ALL_MEN = By.linkText("View All Men");


    // ===== Wishlist =====
    // Appears inside Account dropdown after user is authenticated
    public static final By MY_WISHLIST_MENU_ITEM =
            By.partialLinkText("My Wishlist");
    // inside HomePageElements
    public static final By MY_WISHLIST_LINK =
            By.partialLinkText("My Wishlist");

}


