package core.tests;

import core.globals.Globals;
import core.pages.HomePage;
import core.pages.LoginPage;
import core.pages.WomenPage;
import core.utilities.BaseTest;
import core.utilities.ConfigurationReader;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Test 6: Validates sorting by price on Women page and wishlist counter update
 * after adding two products.
 */
public class Test6_Sorting extends BaseTest {

    @Test
    public void productsSortedByPriceAndTwoItemsInWishlist() {

        // Precondition: valid account must exist from Test1_CreateAccount
        Assert.assertNotNull(Globals.registeredEmail, "Nuk ka email nga Test1_CreateAccount!");
        Assert.assertNotNull(Globals.registeredPassword, "Nuk ka password nga Test1_CreateAccount!");

        int timeout = ConfigurationReader.getInt("timeoutSeconds");

        HomePage home = new HomePage(driver, timeout);

        // 1) Sign In
        home.goToSignIn();
        LoginPage login = new LoginPage(driver, timeout);
        login.login(Globals.registeredEmail, Globals.registeredPassword);

        Assert.assertTrue(
                home.isUserLoggedIn(),
                "Sign In dështoi – nuk u gjet mesazhi i mirëseardhjes."
        );

        // 2) Navigate to WOMEN → View All Women
        home.goToAllWomenPage();

        WomenPage womenPage = new WomenPage(driver, timeout);

        // 3) Apply sorting: Sort By = Price, direction ASC
        womenPage.sortByPriceAscending();

        // 4) Fetch products and validate they are (almost) sorted in ascending order by price
        List<WebElement> products = womenPage.getAllProducts();

        Assert.assertTrue(
                womenPage.arePricesSortedAscending(products),
                "Produktet NUK janë (pothuajse) të sortuara në rritje sipas çmimit."
        );

        // 5) Add first two products from sorted list to wishlist
        womenPage.addProductToWishlistByIndex(0);
        womenPage.addProductToWishlistByIndex(1);

        // 6) Wait until Account dropdown → "My Wishlist" shows (2 items)
        home.waitForWishlistItemCount(2);

        // 7) Read My Wishlist text from Account menu and assert content
        String wishlistText = home.getMyWishlistTextFromAccountMenu();
        System.out.println("My Wishlist menu text: " + wishlistText);

        Assert.assertTrue(
                wishlistText.contains("My Wishlist") && wishlistText.contains("(2 items)"),
                "Teksti i wishlist nuk është i saktë. U gjet: " + wishlistText
        );

    }
}
