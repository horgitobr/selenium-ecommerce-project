package core.elements;

import org.openqa.selenium.By;
/**
 * Locators for Wishlist page and configurable product fallback logic.
 */
public class WishlistPageElements {

    // ===== Wishlist Table =====
    public static final By WISHLIST_ROWS =
            By.cssSelector("table#wishlist-table tbody tr");
    // Alias for row verification
    public static final By WISHLIST_ITEMS = WISHLIST_ROWS;

    // "Add to Cart" within row
    public static final By ROW_ADD_TO_CART_BUTTON =
            By.cssSelector("button.btn-cart");

    // ===== Configure Product (Fallback Option Locators) =====

    // Color swatches
    public static final By COLOR_OPTIONS_PRIMARY =
            By.cssSelector("ul.configurable-swatch-list.configurable-swatch-color li a");

    public static final By COLOR_OPTIONS_FALLBACK =
            By.cssSelector("ul.configurable-swatch-list li a");

    // Size swatches
    public static final By SIZE_OPTIONS_PRIMARY =
            By.cssSelector("ul.configurable-swatch-list.configurable-swatch-size li a");

    public static final By SIZE_OPTIONS_FALLBACK =
            By.cssSelector("ul.configurable-swatch-list li a"); // fallback by text (XS, S, M, ...)


    // ===== Product Add to Cart (Fallback Order) =====
    public static final By PRODUCT_ADD_TO_CART_BUTTON_PRIMARY =
            By.cssSelector("button.btn-cart");
    public static final By PRODUCT_ADD_TO_CART_BUTTON_ALT_TITLE =
            By.cssSelector("button[title='Add to Cart']");
    public static final By PRODUCT_ADD_TO_CART_BUTTON_ALT_ID =
            By.id("product-addtocart-button");
}
