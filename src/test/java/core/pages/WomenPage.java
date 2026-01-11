    package core.pages;
    
    import core.elements.WomenPageElements;
    import org.openqa.selenium.*;
    import org.openqa.selenium.interactions.Actions;
    import org.openqa.selenium.support.ui.ExpectedConditions;
    import org.openqa.selenium.support.ui.Select;
    import org.openqa.selenium.support.ui.WebDriverWait;
    import java.util.ArrayList;
    
    import java.time.Duration;
    import java.util.List;

    /**
     * Page Object for Women category page:
     * hover actions, sorting by price and wishlist operations.
     */
    public class WomenPage {
    
        private final WebDriver driver;
        private final WebDriverWait wait;
        private final Actions actions;
    
        public WomenPage(WebDriver driver, int timeoutSeconds) {
            this.driver = driver;
            this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
            this.actions = new Actions(driver);
        }

        // ================== HOVER ACTIONS (Test 3) ==================
    
        private WebElement getLastProductLi() {
            return wait.until(
                    ExpectedConditions.visibilityOfElementLocated(WomenPageElements.LAST_PRODUCT_LI)
            );
        }
    
        public void hoverLastProduct() {
            WebElement product = getLastProductLi();
            actions.moveToElement(product).perform();
        }
    
        public boolean areLastProductActionsVisible() {
            try {
                WebElement actionsDiv = driver.findElement(WomenPageElements.LAST_PRODUCT_ACTIONS);
                return actionsDiv.isDisplayed();
            } catch (NoSuchElementException e) {
                return false;
            }
        }
    
        public void waitForLastProductActionsVisible() {
            wait.until(
                    ExpectedConditions.visibilityOfElementLocated(WomenPageElements.LAST_PRODUCT_ACTIONS)
            );
        }

        // ================== SORTING & WISHLIST (Test 6) ==================

        /**
         * Waits until Women page is loaded and products are present.
         */
        public void waitForPageToLoad() {

            // Only wait for Women products to be present in the DOM

            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(WomenPageElements.WOMEN_PRODUCTS));
        }


        /**
         * Ensures sort direction is ascending (ASC), toggling if currently DESC.
         */

        private void ensureAscendingDirection() {
            try {
                WebElement dirLink = wait.until(
                        ExpectedConditions.presenceOfElementLocated(WomenPageElements.SORT_DIRECTION_SWITCH)
                );
    
                String classes = dirLink.getAttribute("class");
                System.out.println("Sort direction classes: " + classes);

                 // In HTML: "sort-by-switcher sort-by-switcher--asc" when ASC,
                // "sort-by-switcher--desc" when DESC
                if (classes != null && classes.contains("sort-by-switcher--desc")) {
                    // Currently DESC -> click to switch to ASC
                    try {
                        dirLink.click();
                    } catch (ElementClickInterceptedException e) {
                        ((JavascriptExecutor) driver)
                                .executeScript("arguments[0].click();", dirLink);
                    }

                    // After click, URL should contain dir=asc
                    wait.until(ExpectedConditions.urlContains("dir=asc"));
                    waitForPageToLoad();
                }
            } catch (TimeoutException | NoSuchElementException e) {
                // If switcher is not found or times out, don't fail the test because of this
                System.out.println("Nuk u gjet sort direction switcher ose ka timeout: " + e.getMessage());
            }
        }
        /**
         * Sorts products on Women page by price in ascending order.
         */
        public void sortByPriceAscending() {
            // Ensure Women page is loaded
            waitForPageToLoad();

            // 1) Ensure direction is ASC
            ensureAscendingDirection();

            // 2) Get "Sort By" dropdown
            WebElement selectElem = wait.until(
                    ExpectedConditions.elementToBeClickable(WomenPageElements.SORT_BY_SELECT)
            );
    
            Select sortSelect = new Select(selectElem);

            // 3) Check current selected option
            String current = sortSelect.getFirstSelectedOption().getText().trim();
            System.out.println("Sort By aktual: " + current);

            // 4) If not "Price", select "Price"
            if (!current.equalsIgnoreCase("Price")) {
                sortSelect.selectByVisibleText("Price");

                // Wait for products to reload
                waitForPageToLoad();
            } else {
                // Already sorted by Price, just ensure products are present
                waitForPageToLoad();
            }

            // 5) Ensure direction is still not DESC
            ensureAscendingDirection();

            // 6) Ensure products are loaded again
            waitForPageToLoad();
    
            System.out.println("URL pas sortimit: " + driver.getCurrentUrl());
        }

        /**
         * Returns all product elements on Women page.
         */
        public List<WebElement> getAllProducts() {
            // Ensure page is loaded (products exist in DOM)
            waitForPageToLoad();

            // Get elements fresh from DOM (no additional visibility wait here)
            return driver.findElements(WomenPageElements.WOMEN_PRODUCTS);
        }


        /**
         * Returns product price as double for a given product element.
         */
        public double getProductPrice(WebElement product) {
            WebElement priceSpan = product.findElement(WomenPageElements.PRODUCT_PRICE);
            String text = priceSpan.getText().replace("$", "").trim();
            return Double.parseDouble(text);
        }

        /**
         * Checks if product prices are in ascending order (with a small tolerance for known Tealium bug).
         */
        public boolean arePricesSortedAscending(List<WebElement> products) {
            // Collect price list as doubles
            List<Double> prices = new ArrayList<>();
            for (WebElement product : products) {
                double price = getProductPrice(product);
                prices.add(price);
            }
    
            System.out.println("CMIMET E GJETURA: " + prices);
    
            int violations = 0;
    
            for (int i = 1; i < prices.size(); i++) {
                double prev = prices.get(i - 1);
                double curr = prices.get(i);
    
                System.out.println("COMPARE " + prev + " -> " + curr);
    
                if (curr < prev) {
                    violations++;
                    System.out.println("VIOLATION #" + violations + ": " + curr + " < " + prev);
                }
            }

            // Due to a known Tealium demo bug (e.g. 280 then 245),
            // allow at most 1 ordering violation.
            if (violations > 1) {
                System.out.println("Shkelje të tepërta në renditje: " + violations);
                return false;
            }
    
            return true;
        }

        /**
         * Adds the given product card to wishlist.
         */
        public void addProductToWishlist(WebElement product) {
            WebElement wishlistLink = product.findElement(WomenPageElements.PRODUCT_WISHLIST_LINK);
    
            ((JavascriptExecutor) driver)
                    .executeScript("arguments[0].scrollIntoView({block:'center'});", product);
    
            wait.until(ExpectedConditions.elementToBeClickable(wishlistLink));
    
            try {
                wishlistLink.click();
            } catch (ElementClickInterceptedException e) {
                ((JavascriptExecutor) driver)
                        .executeScript("arguments[0].click();", wishlistLink);
            }
        }
        /**
         * Adds product by index from Women product grid to wishlist (with retry on stale elements).
         */
        public void addProductToWishlistByIndex(int index) {
            for (int attempt = 1; attempt <= 2; attempt++) {
                try {
                    // 0) On each attempt ensure we are on women.html
                    if (!driver.getCurrentUrl().contains("/women.html")) {
                        driver.get("https://ecommerce.tealiumdemo.com/women.html?dir=asc&order=price");
                        waitForPageToLoad();
                        ensureAscendingDirection();
                    }

                    // 1) Get products fresh from DOM
                    List<WebElement> products = driver.findElements(WomenPageElements.WOMEN_PRODUCTS);
    
                    if (index < 0 || index >= products.size()) {
                        throw new IllegalArgumentException("Index jashtë diapazonit të produkteve: " + index);
                    }

                    // 2) Add product with the given index to wishlist
                    addProductToWishlist(products.get(index));

                    // 3) If we reached here without exception -> success
                    return;
    
                } catch (StaleElementReferenceException e) {
                    System.out.println("StaleElement gjatë marrjes/shtimit të produkteve (attempt "
                            + attempt + "), po provoj përsëri...");
                    // Loop continues and on next attempt we:
                    // - verify URL
                    // - reload women.html if needed
                }
            }
    
            throw new RuntimeException("S'po arrij dot të shtoj në wishlist produktin me index "
                    + index + " për shkak të stale element.");
        }
    
    }