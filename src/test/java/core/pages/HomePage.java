package core.pages;

import core.elements.HomePageElements;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HomePage {

    private final WebDriver driver;
    private final WebDriverWait wait;
    private final Actions actions;

    public HomePage(WebDriver driver, int timeoutSeconds) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        this.actions = new Actions(driver);
    }

    // ================== ACCOUNT MENU ==================

    /**
     * Opens the Account dropdown with a small retry in case of stale elements.
     */
    private void openAccountMenu() {
        for (int attempt = 1; attempt <= 2; attempt++) {
            try {
                WebElement accountLink = wait.until(
                        ExpectedConditions.elementToBeClickable(HomePageElements.ACCOUNT_LINK)
                );

                accountLink.click();
                return; // success, get out of method

            } catch (StaleElementReferenceException e) {
                System.out.println("StaleElement te account menu (attempt "
                        + attempt + "), po provoj përsëri...");
                // loop vazhdon, në attempt tjetër do ta gjejë elementin nga e para
            }
        }

        throw new RuntimeException("Nuk munda të hap menunë e account pas disa tentativave.");
    }

    public void goToRegister() {
        openAccountMenu();
        wait.until(ExpectedConditions.elementToBeClickable(HomePageElements.REGISTER_LINK))
                .click();
    }

    public void goToSignIn() {
        openAccountMenu();
        wait.until(ExpectedConditions.elementToBeClickable(HomePageElements.SIGN_IN_LINK))
                .click();
    }

    public void logout() {
        openAccountMenu();
        wait.until(ExpectedConditions.elementToBeClickable(HomePageElements.LOG_OUT_LINK))
                .click();
    }

    // ================== WELCOME MESSAGE ==================

    public String getWelcomeMessage() {
        return wait.until(
                        ExpectedConditions.visibilityOfElementLocated(HomePageElements.WELCOME_MSG)
                )
                .getText()
                .trim();
    }

    /**
     * Returns true if the user appears to be logged in based on the welcome message.
     */
    public boolean isUserLoggedIn() {
        try {
            String text = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(HomePageElements.WELCOME_MSG)
            ).getText();

            if (text == null) return false;

            String lower = text.toLowerCase();
            return lower.contains("welcome");
        } catch (TimeoutException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    // ================== WOMEN ==================

    public void goToAllWomenPage() {
        WebElement womanMenu = wait.until(
                ExpectedConditions.visibilityOfElementLocated(HomePageElements.WOMAN_MENU)
        );

        actions.moveToElement(womanMenu)
                .pause(Duration.ofMillis(500))
                .perform();

        wait.until(ExpectedConditions.elementToBeClickable(HomePageElements.VIEW_ALL_WOMAN))
                .click();
    }

    // ================== MEN ==================

    public void goToAllMenPage() {
        WebElement menMenu = wait.until(
                ExpectedConditions.visibilityOfElementLocated(HomePageElements.MEN_MENU)
        );

        actions.moveToElement(menMenu)
                .pause(Duration.ofMillis(500))
                .perform();

        wait.until(ExpectedConditions.elementToBeClickable(HomePageElements.VIEW_ALL_MEN))
                .click();
    }

    // ================== SALE ==================

    /**
     * Navigates to the SALE page by clicking the "SALE" link in the navbar.
     */
    public void goToAllSalePage() {

        WebElement saleMenu = wait.until(
                ExpectedConditions.elementToBeClickable(HomePageElements.SALE_MENU)
        );

        System.out.println("URL PARA klikimit te SALE: " + driver.getCurrentUrl());

        try {
            saleMenu.click();
        } catch (Exception ex) {
            System.out.println("Click normal te 'SALE' deshtoi, po provoj me JavaScript...");
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", saleMenu);
        }

        System.out.println("URL PAS klikimit te SALE: " + driver.getCurrentUrl());
    }

    // ================== WISHLIST ==================

    /**
     * Opens the Account menu and returns the text of the "My Wishlist (x items)" entry.
     */
    public String getMyWishlistMenuText() {
        openAccountMenu();
        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(HomePageElements.MY_WISHLIST_MENU_ITEM)
        ).getText().trim();
    }

    /**
     * Opens the Account menu and returns the text of the "My Wishlist" link.
     */
    public String getMyWishlistTextFromAccountMenu() {
        openAccountMenu();

        WebElement wl = wait.until(
                ExpectedConditions.visibilityOfElementLocated(HomePageElements.MY_WISHLIST_LINK)
        );

        return wl.getText().trim();   // p.sh. "My Wishlist (2 items)"
    }
    // ================== WISHLIST ==================

    /**
     * Opens the Account menu and clicks on "My Wishlist".
     */
// ================== ACCOUNT -> MY WISHLIST ==================
    public void goToMyWishlist() {
        openAccountMenu();
        wait.until(ExpectedConditions.elementToBeClickable(HomePageElements.MY_WISHLIST_LINK))
                .click();
    }
    /**
     * Waits until the "My Wishlist" text reflects the expected item count, e.g. "(2 items)".
     */
    public void waitForWishlistItemCount(int expectedCount) {
        String expectedSuffix = "(" + expectedCount + " items)";

        for (int attempt = 1; attempt <= 3; attempt++) {
            // open ACCOUNT dropdown
            openAccountMenu();

            try {
                wait.until(driver -> {
                    WebElement wl = driver.findElement(HomePageElements.MY_WISHLIST_LINK);
                    String text = wl.getText();
                    return text != null && text.contains(expectedSuffix);
                });
                // if we reach here, the expected text was found
                return;
            } catch (org.openqa.selenium.TimeoutException e) {
                System.out.println("My Wishlist s'u be " + expectedSuffix +
                        " ne attempt " + attempt + ", po provoj përsëri...");
                // dropdown-i closes when we click somewhere else, but we
                // just try once again on the other iteration
            }
        }

        throw new AssertionError("My Wishlist NUK arriti vlerën " + expectedSuffix + " brenda kohës se pritjes.");
    }




}
