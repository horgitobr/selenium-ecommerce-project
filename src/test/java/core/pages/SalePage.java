package core.pages;

import core.elements.SalePageElements;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Page Object for the Sale page (discounted products and price validation).
 */
public class SalePage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public SalePage(WebDriver driver, int timeoutSeconds) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
    }

    /**
     * Ensures that the Sale page is actually loaded (by URL and title).
     */
    public void waitForPageToLoad() {

        // Debug information: log current URL
        System.out.println("URL AKTUALE: " + driver.getCurrentUrl());

        try {
            wait.until(ExpectedConditions.or(
                    // Either URL contains "sale"
                    ExpectedConditions.urlContains("sale"),
                    // Or page title element contains "Sale"
                    ExpectedConditions.textToBePresentInElementLocated(
                            SalePageElements.PAGE_TITLE,
                            "Sale"
                    )
            ));
        } catch (TimeoutException e) {
            throw new TimeoutException(
                    "Faqja e 'Sale' nuk u ngarkua siç duhet. URL: " + driver.getCurrentUrl(),
                    e
            );
        }
    }

    /**
     * Returns all products displayed on the Sale page.
     */
    public List<WebElement> getSaleProducts() {

        // 1) Wait for Sale page to be open
        waitForPageToLoad();

        // 2) Wait for products to be present in the DOM
        List<WebElement> products = wait.until(
                ExpectedConditions.presenceOfAllElementsLocatedBy(SalePageElements.SALE_PRODUCTS)
        );

        // 3) Scroll to the first product to ensure visibility
        if (!products.isEmpty()) {
            ((JavascriptExecutor) driver)
                    .executeScript("arguments[0].scrollIntoView({block:'center'});", products.get(0));
        }

        // 4) Wait for products to be visible
        products = wait.until(
                ExpectedConditions.visibilityOfAllElementsLocatedBy(SalePageElements.SALE_PRODUCTS)
        );

        System.out.println("U gjetën " + products.size() + " produkte në faqen e Sale.");
        return products;
    }

    /**
     * Returns the "old price" element (original price) for the given product.
     */
    public WebElement getOldPrice(WebElement product) {
        return product.findElement(SalePageElements.OLD_PRICE);
    }

    /**
     * Returns the "special price" element (discounted price) for the given product.
     */
    public WebElement getSpecialPrice(WebElement product) {
        return product.findElement(SalePageElements.SPECIAL_PRICE);
    }

    /**
     * Checks if the given product has both old and special prices visible.
     */
    public boolean hasBothPrices(WebElement product) {
        try {
            WebElement oldPrice = getOldPrice(product);
            WebElement specialPrice = getSpecialPrice(product);
            return oldPrice.isDisplayed() && specialPrice.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // Wrapper methods used by tests (name alignment with Test4)
    public WebElement getOriginalPriceElement(WebElement product) {
        return getOldPrice(product);
    }

    public WebElement getFinalPriceElement(WebElement product) {
        return getSpecialPrice(product);
    }

    /**
     * Returns the text-decoration CSS value for the given price element,
     * using a cross-browser approach (text-decoration / text-decoration-line).
     */
    public String getTextDecoration(WebElement priceElement) {
        try {
            String value = priceElement.getCssValue("text-decoration");
            if (value != null && !value.isBlank()) {
                return value;
            }

            value = priceElement.getCssValue("text-decoration-line");
            if (value != null && !value.isBlank()) {
                return value;
            }

            return "";
        } catch (Exception e) {
            return "";
        }
    }
}

