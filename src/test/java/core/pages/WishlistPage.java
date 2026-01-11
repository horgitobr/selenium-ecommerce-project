package core.pages;

import core.elements.WishlistPageElements;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
/**
 * Page Object for Wishlist page and product configuration flow.
 */
public class WishlistPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private static final String BASE_URL = "https://ecommerce.tealiumdemo.com";

    // Success message after adding to cart
    private static final By SUCCESS_MSG = By.cssSelector("li.success-msg span");

    public WishlistPage(WebDriver driver, int timeoutSeconds) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
    }

    /**
     * Waits until wishlist page is loaded.
     * URL must contain "/wishlist" and the table must be present.
     */
    public void waitForWishlistToLoad() {
        // URL should contain "/wishlist"
        wait.until(ExpectedConditions.urlContains("/wishlist"));
        // Wishlist table must exist (even if it has 0 rows)
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("table#wishlist-table")
        ));
    }

    /**
     * Returns all wishlist row elements.
     */
    public List<WebElement> getWishlistItems() {
        waitForWishlistToLoad();
        return driver.findElements(WishlistPageElements.WISHLIST_ROWS);
    }

    /**
     * Clicks "Add to Cart" for a specific wishlist row.
     */
    public void clickAddToCartForRow(WebElement row) {
        WebElement btn = row.findElement(WishlistPageElements.ROW_ADD_TO_CART_BUTTON);
        safeClick(btn);
    }

    /**
     * Scrolls the given element into the center of the viewport.
     */
    private void scrollIntoViewCenter(WebElement element) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});",
                element
        );
    }

    /**
     * Safe click helper:
     *  - scrolls the element into view
     *  - waits for elementToBeClickable
     *  - falls back to JS click if intercepted
     */
    private void safeClick(WebElement element) {
        scrollIntoViewCenter(element);

        try {
            wait.until(ExpectedConditions.elementToBeClickable(element));
            element.click();
        } catch (ElementClickInterceptedException e) {
            System.out.println("Klikimi u interceptua, po provoj JS click...");
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        }
    }

    /**
     * Clicks the first element found by primary or fallback locator (if primary is empty),
     * using normal click and then JS click if needed.
     */
    private void clickFirstIfExists(By primary, By fallback, String label) {
        List<WebElement> elems = driver.findElements(primary);

        if (elems.isEmpty() && fallback != null) {
            elems = driver.findElements(fallback);
        }

        if (!elems.isEmpty()) {
            WebElement e = elems.get(0);
            scrollIntoViewCenter(e);
            try {
                e.click();
            } catch (ElementClickInterceptedException ex) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", e);
            }
            System.out.println("✅ " + label + " u zgjodh.");
        } else {
            System.out.println("⚠ Nuk u gjet " + label + " – po vazhdoj pa e zgjedhur.");
        }
    }

    /**
     * Fallback: if we have dropdowns (no swatches), selects first non-empty option
     * from each select.super-attribute-select.
     */
    private void selectFirstOptionsFromDropdowns() {
        List<WebElement> selects = driver.findElements(
                By.cssSelector("select.super-attribute-select")
        );

        for (WebElement selectElem : selects) {
            try {
                Select select = new Select(selectElem);
                List<WebElement> options = select.getOptions();
                if (options.size() > 1) {
                    // index 0 is usually "Choose an Option..."
                    select.selectByIndex(1);
                    System.out.println("✅ U zgjodh opsioni i parë nga dropdown-i: "
                            + selectElem.getAttribute("id"));
                }
            } catch (Exception e) {
                System.out.println("⚠ Nuk u arrit të zgjidhet nga dropdown: "
                        + e.getMessage());
            }
        }
    }

    /**
     * On the product configuration page:
     *  - selects first color swatch (from #configurable_swatch_color) if present
     *  - selects first size swatch (from #configurable_swatch_size) if present
     *  - fallback: uses primary/fallback swatch locators and dropdowns
     *  - finds the real "Add to Cart" button (button.btn-cart in form#product_addtocart_form
     *    or other variants)
     *  - clicks via JS and waits for redirect / success / cart table
     *  - if still on configure page, navigates manually to /checkout/cart
     */
    private void configureProductAndAddToCart() {
        String url = driver.getCurrentUrl();
        System.out.println("Në faqen e konfigurimit: " + url);

        // 1) COLOR – try swatch from #configurable_swatch_color
        boolean colorChosen = false;
        List<WebElement> colorSwatches = driver.findElements(
                By.cssSelector("#configurable_swatch_color a.swatch-link")
        );
        if (!colorSwatches.isEmpty()) {
            WebElement color = colorSwatches.get(0);
            scrollIntoViewCenter(color);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", color);
            System.out.println("✅ Color (swatch) u zgjodh nga #configurable_swatch_color.");
            colorChosen = true;
        }

        // fallback if no swatch / not found

        if (!colorChosen) {
            clickFirstIfExists(
                    WishlistPageElements.COLOR_OPTIONS_PRIMARY,
                    WishlistPageElements.COLOR_OPTIONS_FALLBACK,
                    "Color"
            );
        }

        // 2) SIZE – try swatch from #configurable_swatch_size
        boolean sizeChosen = false;
        List<WebElement> sizeSwatches = driver.findElements(
                By.cssSelector("#configurable_swatch_size a.swatch-link")
        );
        if (!sizeSwatches.isEmpty()) {
            WebElement size = sizeSwatches.get(0);
            scrollIntoViewCenter(size);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", size);
            System.out.println("✅ Size (swatch) u zgjodh nga #configurable_swatch_size.");
            sizeChosen = true;
        }

        if (!sizeChosen) {
            clickFirstIfExists(
                    WishlistPageElements.SIZE_OPTIONS_PRIMARY,
                    WishlistPageElements.SIZE_OPTIONS_FALLBACK,
                    "Size"
            );
        }

        // 3) Additional fallback: dropdown super-attributes
        selectFirstOptionsFromDropdowns();

        // 4) LOCATE "ADD TO CART" BUTTON

        // 4.a – try button inside form#product_addtocart_form
        WebElement addToCartBtn = null;
        try {
            addToCartBtn = driver.findElement(
                    By.cssSelector("form#product_addtocart_form button.btn-cart")
            );
            if (!addToCartBtn.isDisplayed()) {
                addToCartBtn = null;
            }
        } catch (Exception ignored) {
        }

        // 4.b – if not found, use generic XPATH (button or link with "Add to Cart")
        if (addToCartBtn == null) {
            List<WebElement> candidates = driver.findElements(
                    By.xpath(
                            "//button[contains(normalize-space(.), 'Add to Cart') " +
                                    " or contains(@title, 'Add to Cart')]" +
                                    " | " +
                                    "//a[contains(normalize-space(.), 'Add to Cart') " +
                                    " or contains(@title, 'Add to Cart')]"
                    )
            );

            for (WebElement el : candidates) {
                try {
                    if (el.isDisplayed()) {
                        addToCartBtn = el;
                        break;
                    }
                } catch (StaleElementReferenceException ignored) {
                }
            }

            // 4.c – if still not found, use span + ancestor button/link
            if (addToCartBtn == null) {
                List<WebElement> spanCandidates = driver.findElements(
                        By.xpath(
                                "//span[contains(normalize-space(.), 'Add to Cart') " +
                                        " or contains(@title, 'Add to Cart')]"
                        )
                );
                for (WebElement span : spanCandidates) {
                    try {
                        if (!span.isDisplayed()) continue;
                        WebElement ancestorButtonOrLink = span.findElement(
                                By.xpath("./ancestor::*[self::button or self::a][1]")
                        );
                        if (ancestorButtonOrLink != null && ancestorButtonOrLink.isDisplayed()) {
                            addToCartBtn = ancestorButtonOrLink;
                            break;
                        }
                    } catch (Exception ignored2) {
                    }
                }
            }
        }

        if (addToCartBtn == null) {
            throw new RuntimeException("S'u gjet asnjë element real (button/a) me 'Add to Cart' në këtë faqe!");
        }

        // 5) Scroll into center
        scrollIntoViewCenter(addToCartBtn);

        // 6) Click via JavaScript
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", addToCartBtn);
        System.out.println("✅ JS click në ADD TO CART u krye.");

        // 7) After clicking, try to detect redirect / success message / cart table.
        //    Do not throw if none are found within timeout.
        WebDriverWait postClickWait = new WebDriverWait(driver, Duration.ofSeconds(10));
        try {
            postClickWait.until(
                    ExpectedConditions.or(
                            ExpectedConditions.urlContains("/checkout/cart"),
                            ExpectedConditions.visibilityOfElementLocated(SUCCESS_MSG),
                            ExpectedConditions.presenceOfElementLocated(
                                    By.cssSelector("table#shopping-cart-table"))
                    )
            );
        } catch (TimeoutException e) {
            System.out.println("⚠ Asnjë redirect / mesazh suksesi i dukshëm – po vazhdoj dhe do hap cart manualisht.");
        }

        // 8) If still on configure page, navigate to cart manually

        if (!driver.getCurrentUrl().contains("/checkout/cart")) {
            System.out.println("✅ Po hap manualisht Shopping Cart...");
            driver.get(BASE_URL + "/checkout/cart/");
            new WebDriverWait(driver, Duration.ofSeconds(20))
                    .until(ExpectedConditions.urlContains("/checkout/cart"));
        }

        System.out.println("Pas konfigurimit jemi në: " + driver.getCurrentUrl());
    }


    /**
     * Adds the first N products from wishlist to cart.
     * Used by Test7_ShoppingCartTest.
     */
    public void addFirstNProductsToCart(int howMany) {
        // Ensure wishlist page is loaded before starting

        waitForWishlistToLoad();

        for (int i = 0; i < howMany; i++) {
            // Read current wishlist rows
            List<WebElement> rows = getWishlistItems();

            if (rows.isEmpty()) {
                System.out.println("Wishlist është bosh – u shtuan vetëm " + i + " produkte në cart.");
                break; // nuk ka më çfarë të shtojmë
            }

            System.out.println("Po shtoj në cart produktin #" + (i + 1));

            // Always use current first row
            WebElement row = rows.get(0);


            // 1) Click "Add to Cart" from wishlist row
            clickAddToCartForRow(row);

            // 2) If configuration page opens, select options and add to cart
            if (driver.getCurrentUrl().contains("/wishlist/index/configure")) {
                configureProductAndAddToCart();
            }

            // 3) If more products need to be added, navigate back to wishlist
            if (i < howMany - 1) {
                driver.get(BASE_URL + "/wishlist/");
                waitForWishlistToLoad();
            }
        }
    }

}
