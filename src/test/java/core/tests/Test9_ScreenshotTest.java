package core.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.annotations.Listeners;
import core.listeners.TestListener;

/**
 * Test 9: Forces a failure to validate screenshot capture via TestListener.
 */
@Listeners({TestListener.class})
public class Test9_ScreenshotTest {

    @Test
    public void verifyScreenshotOnFail() {
        // PURPOSE: This test intentionally fails to confirm that the TestListener's
        // onTestFailure() hook captures and stores a screenshot successfully.
        System.out.println("Ky test pritet te fail-oje per screenshot");

        // Force failure
        Assert.assertTrue(false);
    }
}
