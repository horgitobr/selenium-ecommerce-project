package core.pages;

import core.elements.ShoppingCartElements; // nëse s’e përdor, mund ta heqësh
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Page Object for the Shopping Cart page.
 * Provides helpers for quantity changes, totals and empty cart validations.
 */
public class ShoppingCartPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public ShoppingCartPage(WebDriver driver, int timeoutSeconds) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
    }

    // ================== BASIC HELPERS ==================

    /** Waits until cart page is loaded and at least one row is present. */
    private void waitForCartPageInternal() {
        wait.until(ExpectedConditions.urlContains("/checkout/cart"));
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("table#shopping-cart-table tbody tr")
        ));
    }

    /** Public wrapper used by tests to ensure cart page is loaded. */
    public void waitForCartToLoad() {
        waitForCartPageInternal();
    }

    /** Returns all row elements in the cart table. */
    private List<WebElement> getCartRows() {
        waitForCartPageInternal();
        return driver.findElements(By.cssSelector("table#shopping-cart-table tbody tr"));
    }

    // ================== METHODS THAT CHANGE QTY ==================

    /**
     * Sets quantity of the first item to 2 and waits until subtotal is updated.
     */

    public void setQuantityForFirstItemTo2AndUpdate() {
        List<WebElement> rows = getCartRows();
        if (rows.isEmpty()) {
            System.out.println("Cart është bosh – nuk u gjet asnjë rresht produkti.");
            throw new RuntimeException("S'ka produkte në cart!");
        }

        WebElement firstRow = rows.get(0);

        // Quantity input
        WebElement qtyInput = firstRow.findElement(
                By.cssSelector("td.product-cart-actions input.qty")
        );
        qtyInput.clear();
        qtyInput.sendKeys("2");

        // Subtotal value before update
        By firstSubtotalLocator = By.cssSelector(
                "table#shopping-cart-table tbody tr:first-child td.product-cart-total span.price"
        );
        String oldSubtotalText = driver.findElement(firstSubtotalLocator).getText();

        // Update button
        WebElement updateBtn = firstRow.findElement(
                By.cssSelector("td.product-cart-actions button.btn-update")
        );
        updateBtn.click();

        // Wait until subtotal text actually changes (not hard-coded value)
        wait.until(
                ExpectedConditions.not(
                        ExpectedConditions.textToBePresentInElementLocated(firstSubtotalLocator, oldSubtotalText)
                )
        );
    }

    /**
     * Returns subtotal of the first row as double.
     */
    public double getFirstRowSubtotal() {
        waitForCartPageInternal();
        WebElement priceEl = driver.findElement(
                By.cssSelector("table#shopping-cart-table tbody tr:first-child td.product-cart-total span.price")
        );
        String text = priceEl.getText().replace("$", "").trim();
        return Double.parseDouble(text);
    }

    // ================== METHODS USED BY TEST7 ==================

    /**
     * Sums all row subtotals in the cart table.
     */

    public double getSumOfAllSubtotals() {
        waitForCartPageInternal();
        List<WebElement> subtotalEls = driver.findElements(
                By.cssSelector("table#shopping-cart-table tbody tr td.product-cart-total span.price")
        );

        double sum = 0.0;
        for (WebElement el : subtotalEls) {
            String txt = el.getText().replace("$", "").trim();
            if (!txt.isEmpty()) {
                sum += Double.parseDouble(txt);
            }
        }
        return sum;
    }

    /**
     * Reads Grand Total value from the cart totals table.
     */
    public double getGrandTotal() {
        waitForCartPageInternal();

        // Take the last price element from the totals table
        List<WebElement> totalPriceEls = driver.findElements(
                By.cssSelector("table#shopping-cart-totals-table span.price")
        );

        if (totalPriceEls.isEmpty()) {
            throw new RuntimeException("Nuk u gjet asnjë element për Grand Total në cart!");
        }

        WebElement last = totalPriceEls.get(totalPriceEls.size() - 1);
        String txt = last.getText().replace("$", "").trim();
        return Double.parseDouble(txt);
    }
    /**
     * Returns number of product rows in the cart.
     * If table is not present (empty cart), returns 0.
     */
    public int getCartItemCount() {
        // Don't call when cart is empty (after "Shopping Cart is Empty" message)

        try {
            waitForCartPageInternal();
            return driver.findElements(ShoppingCartElements.CART_ROWS).size();
        } catch (Exception e) {
            // No table present -> empty cart
            return 0;
        }
    }

    /**
     * Deletes the first item from the cart and waits until:
     *  - if previousCount > 1 -> row count decreases
     *  - if previousCount == 1 -> empty cart message is displayed
     */
    public void deleteFirstItemAndWait(int previousCount) {

        // Ensure we are on cart page and table exists

        waitForCartPageInternal();

        // 1) Locate "Remove Item" link in the first row
        WebElement deleteLink = wait.until(
                ExpectedConditions.elementToBeClickable(ShoppingCartElements.FIRST_ROW_REMOVE_LINK)
        );

        // 2) Scroll to center of viewport
        ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block:'center'});", deleteLink);

        // 3) Try normal click, fallback to JS click if needed
        try {
            deleteLink.click();
        } catch (Exception e) {
            ((org.openqa.selenium.JavascriptExecutor) driver)
                    .executeScript("arguments[0].click();", deleteLink);
        }


        // 4) Wait for result based on previous row count
        if (previousCount > 1) {
            // Last product removed -> expect empty cart message

            wait.until(ExpectedConditions.numberOfElementsToBeLessThan(
                    ShoppingCartElements.CART_ROWS,
                    previousCount
            ));
        } else {
            // Last product removed -> expect empty cart message

            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    ShoppingCartElements.EMPTY_CART_MESSAGE
            ));
        }
    }


    /**
     * Checks whether Shopping Cart Empty message is visible and text matches expectations.
     */
    public boolean isCartEmptyMessageVisible() {
        try {
            WebElement title = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(ShoppingCartElements.EMPTY_CART_TITLE)
            );
            WebElement msg = driver.findElement(ShoppingCartElements.EMPTY_CART_MESSAGE);

            String titleText = title.getText().trim();
            String msgText   = msg.getText().toLowerCase();

            return titleText.equalsIgnoreCase("Shopping Cart is Empty")
                    && msgText.contains("you have no items in your shopping cart");
        } catch (Exception e) {
            return false;
        }
    }

}
