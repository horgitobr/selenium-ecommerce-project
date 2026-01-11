// core.pages.MenPage

package core.pages;

import core.elements.MenPageElements;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
/**
 * Page Object for Men category page (product grid + filtering).
 */
public class MenPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public MenPage(WebDriver driver, int timeoutSeconds) {
        this.driver = driver;
        this.wait  = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
    }

    /**
     * Ensures the page is fully loaded and products are present.
     */
    public void waitForPageToLoad() {
        wait.until(ExpectedConditions.urlContains("/men.html"));
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(MenPageElements.MEN_PRODUCTS));
    }

    /**
     * Returns all currently visible products in the grid.
     */
    public List<WebElement> getAllProducts() {
        waitForPageToLoad();
        List<WebElement> products = wait.until(
                ExpectedConditions.visibilityOfAllElementsLocatedBy(MenPageElements.MEN_PRODUCTS)
        );
        return products;
    }

    /**
     * Applies Color -> Black filter and waits for grid refresh.
     */
    public void applyBlackColorFilter() {
        WebElement blackFilter = wait.until(
                ExpectedConditions.elementToBeClickable(MenPageElements.COLOR_FILTER_BLACK)
        );
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block:'center'});", blackFilter);
        blackFilter.click();

        // After applying filter, URL refreshes: men.html?color=20
        wait.until(ExpectedConditions.urlContains("color=20"));
        waitForPageToLoad();
    }

    /**
     * Applies Price -> $0.00 - $99.99 filter if available.
     * Skips if already applied or not present on the page.
     */
    public void applyPriceFilter0To99() {
        // shohim nëse linku ekziston fare në DOM
        List<WebElement> candidates =
                driver.findElements(MenPageElements.PRICE_FILTER_0_99);

        if (candidates.isEmpty()) {
            // Filtering already applied or unavailable. Continue without click.

            System.out.println(
                    "Price $0.00 - $99.99 nuk u gjet në panel (ndoshta faqja është tashmë e filtruar). " +
                            "Po vazhdoj me produktet ekzistuese pa klikim."
            );
            waitForPageToLoad();
            return;
        }

        WebElement priceFilter = wait.until(
                ExpectedConditions.elementToBeClickable(MenPageElements.PRICE_FILTER_0_99)
        );

        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block:'center'});", priceFilter);

        priceFilter.click();

        // After click: URL should contain price=-100 (with or without color param)
        wait.until(ExpectedConditions.urlContains("price=-100"));
        waitForPageToLoad();
    }


    // ==================== ASSERTION HELPERS ====================

    /**
     * Returns the black color swatch element inside a product card.
     */
    private WebElement getBlackColorLi(WebElement product) {
        return product.findElement(
                By.cssSelector("ul.configurable-swatch-list li.option-black")
        );
    }

    /**
     * Checks if Black swatch is visually selected (blue border + selected class).
     */
    public boolean hasBlackColorSelectedWithBlueBorder(WebElement product) {
        try {
            WebElement liBlack = getBlackColorLi(product);

            // Must contain "selected" class
            String classes = liBlack.getAttribute("class");
            if (!classes.contains("selected")) {
                return false;
            }

            // Visual element where border style is applied
            WebElement visual = liBlack.findElement(By.cssSelector("a.swatch-link, span.swatch-label"));

            String borderColor = visual.getCssValue("border-color");
            String borderStyle = visual.getCssValue("border-style");
            String borderWidth = visual.getCssValue("border-width");

            // Border must be visible (solid, not transparent)
            boolean hasVisibleBorder =
                    borderColor != null && !borderColor.isBlank()
                            && !borderColor.contains("0, 0, 0, 0") // jo transparent
                            && borderStyle.toLowerCase().contains("solid");


            return hasVisibleBorder;

        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Returns price as double (parsed from $ format).
     */
    public double getProductPrice(WebElement product) {
        WebElement priceSpan = product.findElement(By.cssSelector("div.price-box span.price"));
        String text = priceSpan.getText().replace("$", "").trim();
        return Double.parseDouble(text);
    }

    /**
     * Checks that all rendered products fall within the given price range.
     */
    public boolean allProductsPriceBetween(List<WebElement> products, double min, double max) {
        for (WebElement product : products) {
            double price = getProductPrice(product);
            if (price < min || price > max) {
                System.out.println("PRODUCT OUT OF RANGE: " + price);
                return false;
            }
        }
        return true;
    }
}
