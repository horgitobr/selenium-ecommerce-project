package core.tests;

import core.globals.Globals;
import core.pages.HomePage;
import core.pages.RegisterPage;
import core.utilities.BaseTest;
import core.utilities.ConfigurationReader;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Test 1: Verifies that a new user can create an account successfully.
 */
public class Test1_CreateAccount extends BaseTest {

    @Test
    public void createAccountSuccessfully() {

        int timeout = ConfigurationReader.getInt("timeoutSeconds");

        HomePage home = new HomePage(driver, timeout);
        home.goToRegister();

        RegisterPage register = new RegisterPage(driver, timeout);

        // 1) Verify registration page title
        Assert.assertTrue(
                register.getTitleText().equalsIgnoreCase("Create an Account"),
                "Titulli i faqes së regjistrimit nuk është korrekt!"
        );

        // 2) Generate unique email + password
        String email = register.generateUniqueEmail();
        String password = "Test@12345";

        // Store credentials for subsequent tests
        Globals.registeredEmail = email;
        Globals.registeredPassword = password;

        register.fillRegisterForm("Test", "Auto", "User", email, password);

        // 3) Click Register
        register.clickRegister();

        // 4) Verify success message
        String successMsg = register.getSuccessMessage();
        Assert.assertTrue(
                successMsg.toLowerCase().contains("thank you for registering"),
                "Mesazhi i suksesit nuk u shfaq siç pritej!"
        );

        // 5) Logout
        home.logout();
    }
}
