package core.tests;

import core.globals.Globals;
import core.pages.HomePage;
import core.pages.LoginPage;
import core.pages.SalePage;
import core.utilities.BaseTest;
import core.utilities.ConfigurationReader;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;


/**
 * Test 4: Validates visual styling for discounted products on the Sale page:
 * - Old price should be grey + strikethrough
 * - Final price should be blue + normal text
 */
public class Test4_SaleProductsStyle extends BaseTest {

    @Test
    public void saleProductsHaveCorrectStyle() {

        // Dependency: Test1 must have stored credentials for login
        Assert.assertNotNull(Globals.registeredEmail, "Nuk ka email nga Test1!");
        Assert.assertNotNull(Globals.registeredPassword, "Nuk ka password nga Test1!");

        // Configurable timeout
        int timeout = ConfigurationReader.getInt("timeoutSeconds");

        HomePage home = new HomePage(driver, timeout);

        // === STEP 1: Sign In ===
        home.goToSignIn();
        LoginPage login = new LoginPage(driver, timeout);
        login.login(Globals.registeredEmail, Globals.registeredPassword);

        // Verify login succeeded via welcome banner
        Assert.assertTrue(
                home.isUserLoggedIn(),
                "Sign In dështoi – nuk u gjet mesazhi i mirëseardhjes."
        );

        // === STEP 2: Navigate directly to SALE page ===
        home.goToAllSalePage();

        // === STEP 3: Retrieve product cards from Sale page ===
        SalePage salePage = new SalePage(driver, timeout);
        List<WebElement> saleProducts = salePage.getSaleProducts();

        Assert.assertFalse(
                saleProducts.isEmpty(),
                "Nuk u gjet asnjë produkt në faqen e Sale."
        );

        // Color expectations based on Tealium UI CSS:
        // Old price:   rgba(160, 160, 160, 1) → grey
        // Final price: rgba(51, 153, 204, 1) → blue
        String expectedGreySnippet = "160, 160, 160";  // rgba(160, 160, 160, 1)
        String expectedBlueSnippet = "51, 153, 204";   // rgba(51, 153, 204, 1)

        // === STEP 4: Validate visual styling for each discounted product ===
        for (WebElement product : saleProducts) {

            WebElement originalPrice = salePage.getOriginalPriceElement(product);
            WebElement finalPrice    = salePage.getFinalPriceElement(product);

            // Ensure both price elements exist
            Assert.assertNotNull(
                    originalPrice,
                    "Produkti nuk ka çmim origjinal (old-price)."
            );

            Assert.assertNotNull(
                    finalPrice,
                    "Produkti nuk ka çmim final (special-price)."
            );

            // === COLOR CHECKS ===
            String originalColor = originalPrice.getCssValue("color");
            String finalColor    = finalPrice.getCssValue("color");

            Assert.assertTrue(
                    originalColor.contains(expectedGreySnippet),
                    "Original price nuk duket gri. CSS: " + originalColor
            );

            // Allow both rgba and hex as fallback for blue
            Assert.assertTrue(
                    finalColor.contains(expectedBlueSnippet)
                            || finalColor.toLowerCase().contains("1e7ec8"),
                    "Final price nuk duket blu. CSS: " + finalColor
            );

            // === STRIKETHROUGH CHECKS ===
            String originalDecoration = salePage.getTextDecoration(originalPrice);
            String finalDecoration    = salePage.getTextDecoration(finalPrice);

            Assert.assertTrue(
                    originalDecoration.toLowerCase().contains("line-through"),
                    "Original price DUHET të jetë strikethrough. CSS: " + originalDecoration
            );

            Assert.assertFalse(
                    finalDecoration.toLowerCase().contains("line-through"),
                    "Final price NUK duhet të jetë strikethrough. CSS: " + finalDecoration
            );
        }
    }
}
