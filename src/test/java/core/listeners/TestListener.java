package core.listeners;

import core.utilities.DriverFactory;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * TestNG listener — captures a screenshot when a test fails.
 */

public class TestListener implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {

        File src = ((TakesScreenshot) DriverFactory.getDriver())
                .getScreenshotAs(OutputType.FILE);

        // Generate timestamped file name
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = result.getName() + "_" + timestamp + ".png";

        // Save under /screenshots/
        File dest = new File("screenshots/" + fileName);
        dest.getParentFile().mkdirs();

        try {
            Files.copy(src.toPath(), dest.toPath());
            System.out.println("📸 Screenshot saved: " + dest.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
