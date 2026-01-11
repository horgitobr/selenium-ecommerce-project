package core.tests;

import core.globals.Globals;
import core.pages.HomePage;
import core.pages.LoginPage;
import core.pages.ShoppingCartPage;
import core.utilities.BaseTest;
import core.utilities.ConfigurationReader;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test 8: Validates that removing all items from the Shopping Cart
 * results in an empty cart message being displayed.
 */
public class Test8_EmptyShoppingCartTest extends BaseTest {

    @Test
    public void emptyShoppingCartTest() {

        // PRECONDITION: valid credentials must exist from Test1_CreateAccount
        Assert.assertNotNull(Globals.registeredEmail,  "Nuk ka email nga Test1_CreateAccount!");
        Assert.assertNotNull(Globals.registeredPassword,"Nuk ka password nga Test1_CreateAccount!");

        int timeout = ConfigurationReader.getInt("timeoutSeconds");

        HomePage homePage   = new HomePage(driver, timeout);
        LoginPage loginPage = new LoginPage(driver, timeout);

        // 1) Sign In with the existing user
        homePage.goToSignIn();
        loginPage.login(Globals.registeredEmail, Globals.registeredPassword);

        Assert.assertTrue(
                homePage.isUserLoggedIn(),
                "Sign In dështoi – nuk u gjet mesazhi i mirëseardhjes në Test8."
        );

        // 2) Open Shopping Cart page
        driver.get("https://ecommerce.tealiumdemo.com/checkout/cart/");
        ShoppingCartPage cartPage = new ShoppingCartPage(driver, timeout);
        cartPage.waitForCartToLoad();

        // Cart must contain at least one item before starting removal flow
        int itemCount = cartPage.getCartItemCount();
        Assert.assertTrue(
                itemCount > 0,
                "Precondition FAILED: Shopping cart duhet të ketë të paktën 1 produkt para se ta boshatisim."
        );

        // 3) Remove items one by one and verify the count decreases until cart is empty
        while (itemCount > 0) {
            int previousCount = itemCount;

            cartPage.deleteFirstItemAndWait(previousCount);

            if (previousCount > 1) {
                int newCount = cartPage.getCartItemCount();
                Assert.assertEquals(
                        newCount,
                        previousCount - 1,
                        "Numri i elementëve në cart nuk u ul me 1 pas fshirjes."
                );
                itemCount = newCount;
            } else {
                // If it was the last product, exit the loop (cart should now be empty)

                itemCount = 0;
            }
        }

        // 4) Verify that "You have no items in your shopping cart." message is displayed
        Assert.assertTrue(
                cartPage.isCartEmptyMessageVisible(),
                "Mesazhi i cart-it bosh nuk u shfaq siç pritej."
        );

        // 5) Browser closing is handled by BaseTest.tearDown()
    }
}
