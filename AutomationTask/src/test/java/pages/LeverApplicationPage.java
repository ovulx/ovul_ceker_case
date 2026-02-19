package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.Waits;

import static org.testng.Assert.assertTrue;

public class LeverApplicationPage {
    private final WebDriver driver;
    private final Waits waits;

    private final By leverApplyFormHint =
            By.xpath("//*[contains(.,'Apply for this job') or contains(.,'Submit application') or contains(@class,'application')]");

    public LeverApplicationPage(WebDriver driver) {
        this.driver = driver;
        this.waits = new Waits(driver);
    }

    public void assertRedirectedToLeverApplication() {
        System.out.println("[ASSERT] Waiting until URL contains 'lever'. Current URL: " + driver.getCurrentUrl());
        waits.untilUrlContains(driver, "lever");

        System.out.println("[ASSERT] Verifying Lever form elements are visible (Apply / Submit).");
        waits.visible(By.xpath("//*[contains(.,'Apply for this job') or contains(.,'Submit application')]"));

        System.out.println("[ASSERT] Final URL check includes 'lever'. Current URL: " + driver.getCurrentUrl());
        assertTrue(driver.getCurrentUrl().toLowerCase().contains("lever"));

        System.out.println("[ASSERT] Successfully redirected to Lever application page.");
    }
}
