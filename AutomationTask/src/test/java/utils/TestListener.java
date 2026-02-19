package utils;

import base.DriverFactory;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {
        var driver = DriverFactory.getDriver();
        if (driver != null) {
            String path = ScreenshotUtil.take(driver, result.getMethod().getMethodName());
            System.out.println("SCREENSHOT: " + path);
        }
    }
}
