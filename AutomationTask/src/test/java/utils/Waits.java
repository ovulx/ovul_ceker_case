package utils;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public final class Waits {
    private final WebDriverWait wait;

    public Waits(WebDriver driver) {
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    public WebElement visible(By by) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    public WebElement clickable(By by) {
        return wait.until(ExpectedConditions.elementToBeClickable(by));
    }

    public void untilUrlContains(WebDriver driver, String partial) {
        wait.until(d -> d.getCurrentUrl().contains(partial));
    }

    public void untilDocumentReady(WebDriver driver) {
        wait.until(d -> ((JavascriptExecutor) d).executeScript("return document.readyState").equals("complete"));
    }

    public void untilCountMoreThan(By by, int n) {
        wait.until(d -> d.findElements(by).size() > n);
    }

    public void untilCountAtLeast(By by, int n) {
        wait.until(d -> d.findElements(by).size() >= n);
    }

    public void untilNumberOfWindowsToBe(int n, WebDriver driver) {
        wait.until(d -> driver.getWindowHandles().size() == n);
    }

    public void untilTrue(java.util.function.Function<WebDriver, Boolean> condition) {
        wait.until(d -> condition.apply(d));
    }
}
