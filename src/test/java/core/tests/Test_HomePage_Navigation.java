package core.tests;

import core.pages.HomePage;
import core.utilities.BaseTest;
import core.utilities.ConfigurationReader;
import org.testng.annotations.Test;

public class Test_HomePage_Navigation extends BaseTest {

    @Test
    public void shouldOpenRegisterFromAccountMenu() {
        HomePage home = new HomePage(driver, ConfigurationReader.getInt("timeoutSeconds"));
        home.goToRegister();
    }
}
