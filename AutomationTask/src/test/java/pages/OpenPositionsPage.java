package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;
import utils.Cookies;
import utils.Waits;

import java.time.Duration;
import java.util.List;

import static org.testng.Assert.*;

public class OpenPositionsPage {
    private final WebDriver driver;
    private final Waits waits;

    private final By locationSelect = By.id("filter-by-location");
    private final By departmentSelect = By.id("filter-by-department");
    private final By jobCards = By.cssSelector("#jobs-list .position-list-item-wrapper");
    private final By titleInCard = By.cssSelector("h3, h2, .position-title");

    private final By viewRoleInCard = By.xpath(".//*[self::a or self::button][normalize-space()='View Role']");

    public OpenPositionsPage(WebDriver driver) {
        this.driver = driver;
        this.waits = new Waits(driver);
    }

    public OpenPositionsPage applyFiltersIstanbulAndQa() {
        System.out.println("[STEP] Applying filters: Location='Istanbul, Turkiye' and Department='Quality Assurance'.");

        System.out.println("[ACTION] Trying to accept cookies if the banner is present.");
        Cookies.acceptIfPresent(driver);

        System.out.println("[INFO] Waiting for filter dropdowns to be visible.");
        waits.visible(locationSelect);
        waits.visible(departmentSelect);

        System.out.println("[INFO] Waiting until select options are loaded.");
        waitSelectOptionsLoaded(locationSelect);
        waitSelectOptionsLoaded(departmentSelect);

        System.out.println("[ACTION] Selecting location: Istanbul, Turkiye.");
        new Select(driver.findElement(locationSelect)).selectByVisibleText("Istanbul, Turkiye");

        System.out.println("[ACTION] Selecting department: Quality Assurance.");
        new Select(driver.findElement(departmentSelect)).selectByVisibleText("Quality Assurance");

        System.out.println("[INFO] Filter selection completed.");
        return this;
    }

    public OpenPositionsPage assertJobsListPresent() {
        System.out.println("[ASSERT] Verifying job list is present and has at least 1 item.");

        System.out.println("[ACTION] Trying to accept cookies if the banner is present.");
        Cookies.acceptIfPresent(driver);

        System.out.println("[INFO] Waiting until job cards count is at least 1.");
        waits.untilCountAtLeast(jobCards, 1);

        System.out.println("[INFO] Waiting until job cards are clickable.");
        waits.clickable(jobCards);

        System.out.println("[ASSERT] Jobs list is present.");
        return this;
    }

    public OpenPositionsPage assertAllJobsMatchCriteria() {
        System.out.println("[CASE 3][ASSERT] Verifying all job cards match Position/Department/Location criteria.");

        waits.visible(jobCards);
        assertJobsListPresent();

        System.out.println("[INFO] Waiting until each card text contains Istanbul and Turkey/Turkiye/Türkiye.");
        waits.untilTrue(d -> d.findElements(jobCards).stream().allMatch(c -> {
            String t = c.getText().toLowerCase();
            return t.contains("istanbul") && (t.contains("turkey") || t.contains("turkiye") || t.contains("türkiye"));
        }));

        List<WebElement> cards = driver.findElements(jobCards);
        System.out.println("[INFO] Total job cards found: " + cards.size());

        int index = 1;
        for (WebElement card : cards) {
            String text = card.getText().toLowerCase();

            System.out.println("[ASSERT] Card #" + index + " - checking Department contains 'Quality Assurance'.");
            assertTrue(text.contains("quality assurance"),
                    "Department 'Quality Assurance' içermeli. Actual: " + text);

            System.out.println("[ASSERT] Card #" + index + " - checking Location contains Istanbul and Turkey/Turkiye.");
            assertTrue(text.contains("istanbul") &&
                            (text.contains("turkey") || text.contains("turkiye") || text.contains("türkiye")),
                    "Location 'Istanbul, Turkey' içermeli. Actual: " + text);

            String title = safeText(card, titleInCard).toLowerCase();
            System.out.println("[ASSERT] Card #" + index + " - checking Position title contains 'Quality Assurance' or 'QA'. Title: " + title);
            assertTrue(title.contains("quality assurance") || title.contains("qa"),
                    "Position 'Quality Assurance' (veya QA) içermeli. Actual title: " + title);

            index++;
        }

        System.out.println("[CASE 3][ASSERT] All cards matched expected criteria successfully.");
        return this;
    }

    public LeverApplicationPage clickFirstCardViewRoleAndSwitchToNewTab() {
        System.out.println("[STEP] Clicking 'View Role' for the first job card and switching to the new browser tab.");

        assertJobsListPresent();

        WebElement firstCard = driver.findElements(jobCards).get(0);
        System.out.println("[INFO] First card text (preview): " + firstCard.getText().replace("\n", " ").trim());

        WebElement btn = firstCard.findElement(viewRoleInCard);

        String originalWindow = driver.getWindowHandle();
        int windowsBefore = driver.getWindowHandles().size();

        System.out.println("[ACTION] Clicking 'View Role' button.");
        btn.click();

        System.out.println("[INFO] Waiting for a new tab/window to open. Windows before click: " + windowsBefore);
        waits.untilNumberOfWindowsToBe(windowsBefore + 1, driver);

        System.out.println("[ACTION] Switching to the newly opened tab/window.");
        for (String handle : driver.getWindowHandles()) {
            if (!handle.equals(originalWindow)) {
                driver.switchTo().window(handle);
                break;
            }
        }

        System.out.println("[INFO] Switched to new tab. Current URL: " + driver.getCurrentUrl());
        return new LeverApplicationPage(driver);
    }

    private void waitSelectOptionsLoaded(By selectBy) {
        long end = System.currentTimeMillis() + Duration.ofSeconds(15).toMillis();
        while (System.currentTimeMillis() < end) {
            try {
                Select s = new Select(driver.findElement(selectBy));
                if (s.getOptions().size() > 1) {
                    System.out.println("[INFO] Options loaded for select: " + selectBy + " (count=" + s.getOptions().size() + ")");
                    return;
                }
            } catch (StaleElementReferenceException ignored) {}
            sleep(150);
        }
        new Select(driver.findElement(selectBy)).getOptions();
        System.out.println("[WARN] Options loading timeout reached for select: " + selectBy + " (fallback executed)");
    }

    private String safeText(WebElement root, By child) {
        try { return root.findElement(child).getText().trim(); }
        catch (Exception e) { return root.getText().trim(); }
    }

    private void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ignored) {}
    }
}
