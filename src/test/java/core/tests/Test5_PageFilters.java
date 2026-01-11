package core.tests;

import core.globals.Globals;
import core.pages.HomePage;
import core.pages.LoginPage;
import core.pages.MenPage;
import core.utilities.BaseTest;
import core.utilities.ConfigurationReader;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Test 5: Validates product filtering behavior on Men category page:
 * - Black color filter must visually select "Black" swatch with border
 * - Price filter $0–$99.99 must reduce results to 3 products within range
 */
public class Test5_PageFilters extends BaseTest {

    @Test
    public void menPageFiltersWorkCorrectly() {

        // Precondition: Test1 must have created and stored credentials
        Assert.assertNotNull(Globals.registeredEmail, "Nuk ka email nga Test1_CreateAccount!");
        Assert.assertNotNull(Globals.registeredPassword, "Nuk ka password nga Test1_CreateAccount!");

        int timeout = ConfigurationReader.getInt("timeoutSeconds");

        // Browser + BASE_URL are handled by BaseTest.setUp()
        HomePage home = new HomePage(driver, timeout);

        // === STEP 1: Sign In ===
        home.goToSignIn();
        LoginPage login = new LoginPage(driver, timeout);
        login.login(Globals.registeredEmail, Globals.registeredPassword);

        Assert.assertTrue(
                home.isUserLoggedIn(),
                "Sign In dështoi – nuk u gjet mesazhi i mirëseardhjes."
        );

        // 2) Hover MEN -> View All Men
        home.goToAllMenPage();

        MenPage menPage = new MenPage(driver, timeout);

        // === STEP 3: Apply color filter: BLACK ===
        menPage.applyBlackColorFilter();
        List<WebElement> productsAfterColor = menPage.getAllProducts();

        Assert.assertFalse(
                productsAfterColor.isEmpty(),
                "Pas filtrit të ngjyrës Black nuk u gjet asnjë produkt."
        );

        // Style check: every product must have Black swatch selected + bordered (blue)
        for (WebElement product : productsAfterColor) {
            Assert.assertTrue(
                    menPage.hasBlackColorSelectedWithBlueBorder(product),
                    "Produkti nuk ka ngjyrën Black të selektuar me border vizual (blu)."
            );
        }

        // === STEP 4: Apply price filter: $0.00 – $99.99 ===
        menPage.applyPriceFilter0To99();
        List<WebElement> productsAfterPrice = menPage.getAllProducts();

        // Requirement: must reduce to 3 products
        Assert.assertEquals(
                productsAfterPrice.size(),
                3,
                "Numri i produkteve pas filtrit të çmimit nuk është 3."
        );

        // === STEP 5: Validate price range ===
        Assert.assertTrue(
                menPage.allProductsPriceBetween(productsAfterPrice, 0.00, 99.99),
                "Jo të gjithë produktet kanë çmim brenda intervalit $0.00 - $99.99."
        );
    }
}
