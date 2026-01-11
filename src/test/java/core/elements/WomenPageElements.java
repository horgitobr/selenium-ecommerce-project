package core.elements;

import org.openqa.selenium.By;
/**
 * Locators for Women category page:
 * hover actions, product grid, sorting, and wishlist link.
 */
public class WomenPageElements {

    // ===== Hover Actions =====
    public static final By LAST_PRODUCT_LI =
            By.cssSelector("ul.products-grid li.item.last");

    public static final By LAST_PRODUCT_ACTIONS =
            By.cssSelector("ul.products-grid li.item.last div.actions");

    // ===== Product Grid =====


    public static final By WOMEN_PRODUCTS =
            By.cssSelector("div.category-products ul.products-grid li.item");

    // ===== Sorting Controls =====
    public static final By SORT_BY_SELECT =
            By.cssSelector("div.sort-by select[title='Sort By']");

    //  (ASC/DESC) sorting
    public static final By SORT_DIRECTION_SWITCH =
            By.cssSelector("div.sort-by a.sort-by-switcher");

    // Span of price inside product
    public static final By PRODUCT_PRICE =
            By.cssSelector("div.price-box span.price");

    // Link "Add to Wishlist" inside product
    public static final By PRODUCT_WISHLIST_LINK =
            By.cssSelector("a.link-wishlist");
}
