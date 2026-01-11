package core.elements;

import org.openqa.selenium.By;
/**
 * Locators for Sale category page:
 * product grid and discounted pricing.
 */

public class SalePageElements {

    // ===== Page Header =====
    public static final By PAGE_TITLE =
            By.cssSelector("div.page-title h1");

    // ===== Product Grid =====
    public static final By SALE_PRODUCTS =
            By.cssSelector("div.category-products ul.products-grid li.item");

    // ===== Pricing =====
    public static final By OLD_PRICE =
            By.cssSelector("p.old-price span.price"); // Original (strikethrough) price

    // Çmimi final (i zbritur)
    public static final By SPECIAL_PRICE =
            By.cssSelector("p.special-price span.price"); // Final discounted price
}
