package core.tests;

import core.globals.Globals;
import core.pages.HomePage;
import core.pages.LoginPage;
import core.pages.WomenPage;
import core.utilities.BaseTest;
import core.utilities.ConfigurationReader;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test 3: Validates that hovering over a product in Women page reveals UI actions
 * (e.g. Add to Wishlist/View Details), confirming hover-based style behavior.
 */
public class Test3_HoverStyle extends BaseTest {

    @Test
    public void hoverShowsActionsOnProduct() {

        // Verify credentials exist (produced in Test1)
        Assert.assertNotNull(
                Globals.registeredEmail,
                "Globals.registeredEmail është null. Ekzekuto Test1_CreateAccount i pari."
        );
        Assert.assertNotNull(
                Globals.registeredPassword,
                "Globals.registeredPassword është null. Ekzekuto Test1_CreateAccount i pari."
        );

        // Configurable timeout for waits/interactions
        int timeout = ConfigurationReader.getInt("timeoutSeconds");

        // Browser + base URL already handled in BaseTest.setUp()
        HomePage homePage = new HomePage(driver, timeout);

        // === PRECONDITION: Login into application ===
        homePage.goToSignIn();

        LoginPage loginPage = new LoginPage(driver, timeout);
        loginPage.login(Globals.registeredEmail, Globals.registeredPassword);

        // Verify login was successful via welcome banner
        String welcome = homePage.getWelcomeMessage();
        Assert.assertTrue(
                welcome != null && welcome.toLowerCase().contains("test auto user"),
                "Login failed in precondition for Test 3! Mesazhi i welcome: " + welcome
        );

        // === STEP 1: Navigate to Women page via top menu hover ===
        homePage.goToAllWomenPage();

        WomenPage womenPage = new WomenPage(driver, timeout);

        // === STEP 2: Check hover-based style before interaction ===
        boolean visibleBefore = womenPage.areLastProductActionsVisible();
        System.out.println("Actions visible BEFORE hover? " + visibleBefore);

        // Trigger hover on last product in the grid
        womenPage.hoverLastProduct();


        // === STEP 3: Wait until hover UI actions become visible ===
        womenPage.waitForLastProductActionsVisible();

        boolean visibleAfter = womenPage.areLastProductActionsVisible();
        System.out.println("Actions visible AFTER hover? " + visibleAfter);

        // === ASSERTIONS ===
        // Before hover: actions should be hidden
        Assert.assertFalse(
                visibleBefore,
                "Actions nuk duhet të jenë të dukshme PARA hover!"
        );

        // After hover: actions should be visible
        Assert.assertTrue(
                visibleAfter,
                "Actions duhet të jenë të dukshme PAS hover, por nuk janë!"
        );
    }
}
