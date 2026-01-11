package core.tests;

import core.globals.Globals;
import core.pages.HomePage;
import core.pages.LoginPage;
import core.utilities.BaseTest;
import core.utilities.ConfigurationReader;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test 2: Validates successful sign-in using credentials created in Test1.
 */
public class Test2_SignIn extends BaseTest {

    @Test
    public void signInSuccessfully() {

        // Ensure Test1 has already produced valid credentials
        Assert.assertNotNull(Globals.registeredEmail, "Nuk ka email nga Test1!");
        Assert.assertNotNull(Globals.registeredPassword, "Nuk ka password nga Test1!");

        // Load configurable timeout for waits and interactions
        int timeout = ConfigurationReader.getInt("timeoutSeconds");

        // Navigate to login page from home
        HomePage home = new HomePage(driver, timeout);
        home.goToSignIn();


        // Perform login using previously registered credentials
        LoginPage login = new LoginPage(driver, timeout);
        login.login(Globals.registeredEmail, Globals.registeredPassword);

        // Retrieve welcome banner text
        String welcome = home.getWelcomeMessage();
        String expectedName = "Test Auto User";

        // Validate UI displays logged-in user's full name
        Assert.assertTrue(
                welcome.toLowerCase().contains(expectedName.toLowerCase()),
                "Username (Test Auto User) nuk u shfaq ne kendin e djathte! Mesazhi: " + welcome
        );

        // Logout to reset session state for subsequent tests
        home.logout();
    }
}
