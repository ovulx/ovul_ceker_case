package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.Cookies;
import utils.Waits;

public class QaCareersPage {
    private final WebDriver driver;
    private final Waits waits;

    private final By seeAllQaJobs =
            By.xpath("//*[self::a or self::button][normalize-space()='See all QA jobs']");

    public QaCareersPage(WebDriver driver) {
        this.driver = driver;
        this.waits = new Waits(driver);
    }

    public QaCareersPage open() {
        System.out.println("[ACTION] Opening QA careers page: https://insiderone.com/careers/quality-assurance/");
        driver.get("https://insiderone.com/careers/quality-assurance/");

        System.out.println("[ACTION] Trying to accept cookies if the banner is present.");
        Cookies.acceptIfPresent(driver);

        System.out.println("[INFO] QA careers page loaded.");
        return this;
    }

    public OpenPositionsPage clickSeeAllQaJobs() {
        System.out.println("[ACTION] Clicking 'See all QA jobs' to open positions list.");
        waits.clickable(seeAllQaJobs).click();
        System.out.println("[INFO] Open positions page should be displayed now.");
        return new OpenPositionsPage(driver);
    }
}
