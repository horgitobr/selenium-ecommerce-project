package core.tests;

import core.globals.Globals;
import core.pages.HomePage;
import core.pages.LoginPage;
import core.pages.WishlistPage;
import core.pages.ShoppingCartPage;
import core.utilities.BaseTest;
import core.utilities.ConfigurationReader;
import org.testng.Assert;
import org.testng.annotations.Test;

public class Test7_ShoppingCartTest extends BaseTest {

    @Test
    public void shoppingCartTotalsAreCorrect() {

        // PRECONDITION: User must already exist (created in Test1_CreateAccount)
        Assert.assertNotNull(Globals.registeredEmail, "Nuk ka email nga Test1_CreateAccount!");
        Assert.assertNotNull(Globals.registeredPassword, "Nuk ka password nga Test1_CreateAccount!");

        int timeout = ConfigurationReader.getInt("timeoutSeconds");

        HomePage homePage   = new HomePage(driver, timeout);
        LoginPage loginPage = new LoginPage(driver, timeout);

        // 1) Sign In using the same registered user
        homePage.goToSignIn();
        loginPage.login(Globals.registeredEmail, Globals.registeredPassword);

        Assert.assertTrue(
                homePage.isUserLoggedIn(),
                "Sign In dështoi në Test7 – nuk u gjet mesazhi i mirëseardhjes."
        );

        // 2) Open My Wishlist via ACCOUNT menu
        homePage.goToMyWishlist();

        // 3) Add 2 products from wishlist → cart (handled internally by WishlistPage logic)
        WishlistPage wishlistPage = new WishlistPage(driver, timeout);
        wishlistPage.addFirstNProductsToCart(2);

        // NOTE: After adding the 2 items, Tealium typically redirects to /checkout/cart,
        // but we enforce navigation to ensure test is stable.
        driver.get("https://ecommerce.tealiumdemo.com/checkout/cart/");

        // 4) Ensure Shopping Cart page has loaded
        ShoppingCartPage cartPage = new ShoppingCartPage(driver, timeout);
        cartPage.waitForCartToLoad();

        // 5) Update quantity of the first product to 2 and click Update
        // This triggers a recalculation of line subtotal + grand total
        cartPage.setQuantityForFirstItemTo2AndUpdate();

        // 6) Retrieve sum of all item-level subtotals + cart grand total
        double sumOfItems = cartPage.getSumOfAllSubtotals();
        double grandTotal = cartPage.getGrandTotal();

        System.out.println("SUM OF ITEMS: " + sumOfItems);
        System.out.println("GRAND TOTAL : " + grandTotal);

        // 7) ASSERT (Business rule): Grand Total = Σ (item subtotals)
        Assert.assertEquals(
                sumOfItems,
                grandTotal,
                "Sum of all item subtotals must be equal to Grand Total price"
        );
    }
}

