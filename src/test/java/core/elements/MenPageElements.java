
package core.elements;

import org.openqa.selenium.By;
/**
 * Locators for Men category page:
 * product grid and filtering panel.
 */

public class MenPageElements {

    // ===== Product Grid =====
    public static final By MEN_PRODUCTS =
            By.cssSelector("div.category-products ul.products-grid li.item");

    // ===== Filtering =====

    // Color filter -> Black (left filtering panel)
    // Uses image alt attribute to uniquely target the color
    public static final By COLOR_FILTER_BLACK =
            By.xpath("//dt[normalize-space()='Color']/following-sibling::dd[1]" +
                    "//img[@alt='Black']/ancestor::a[1]");

    // Price filter -> $0.00 - $99.99 (matches link containing price=-100)

    public static final By PRICE_FILTER_0_99 =
            By.xpath("//dt[normalize-space()='Price']/following-sibling::dd[1]" +
                    "//a[contains(@href,'price=-100')]");


}
