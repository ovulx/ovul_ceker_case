package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.Cookies;
import utils.Waits;

import java.util.List;

import static org.testng.Assert.assertTrue;

public class HomePage {
    private final WebDriver driver;
    private final Waits waits;

    private final By headerNav = By.cssSelector("header");
    private final List<By> mainBlocks = List.of(
            By.xpath("//*[contains(.,'Platform') or contains(.,'Capabilities')]"),
            By.xpath("//*[contains(.,'Customers') or contains(.,'Resources')]"),
            By.xpath("//*[contains(.,'Get a demo') or contains(.,'Platform Tour')]")
    );

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.waits = new Waits(driver);
    }

    public HomePage open() {
        System.out.println("[ACTION] Opening Insider home page: https://insiderone.com/");
        driver.get("https://insiderone.com/");

        System.out.println("[INFO] Waiting for document ready state.");
        waits.untilDocumentReady(driver);

        System.out.println("[ACTION] Trying to accept cookies if the banner is present.");
        Cookies.acceptIfPresent(driver);

        System.out.println("[INFO] Home page navigation completed.");
        return this;
    }

    public HomePage assertOpenedAndMainBlocksLoaded() {
        System.out.println("[ASSERT] Verifying header is visible.");
        waits.visible(headerNav);

        System.out.println("[ASSERT] Verifying main blocks are visible.");
        for (By block : mainBlocks) {
            System.out.println("[ASSERT] Checking block visible: " + block);
            waits.visible(block);
        }

        System.out.println("[ASSERT] Verifying page title contains 'insider'. Current title: " + driver.getTitle());
        assertTrue(driver.getTitle().toLowerCase().contains("insider"), "Title Insider içermeli");

        System.out.println("[ASSERT] Home page opened and main blocks loaded successfully.");
        return this;
    }
}
