package utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public final class Cookies {
    private Cookies() {}

    private static final By ACCEPT_ALL =
            By.xpath("//*[self::button or self::a][normalize-space()='Accept All']");

    public static void acceptIfPresent(WebDriver driver) {
        try { driver.findElement(ACCEPT_ALL).click(); }
        catch (Exception ignored) {}
    }
}