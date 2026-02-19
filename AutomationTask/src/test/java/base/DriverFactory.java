package base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;

public final class DriverFactory {
    private static final ThreadLocal<WebDriver> TL = new ThreadLocal<>();

    private DriverFactory() {}

    public static WebDriver getDriver() {
        return TL.get();
    }

    public static void initDriver(String browser) {
        if (browser == null || browser.isBlank()) browser = "chrome";
        browser = browser.toLowerCase();

        WebDriver driver;
        switch (browser) {
            case "firefox" -> {
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions fo = new FirefoxOptions();
                driver = new FirefoxDriver(fo);
            }
            default -> {
                WebDriverManager.chromedriver().setup();
                ChromeOptions co = new ChromeOptions();
                co.addArguments("--disable-notifications");
                driver = new ChromeDriver(co);
            }
        }

        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(45));
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(30));
        driver.manage().window().maximize();
        TL.set(driver);
    }

    public static void quitDriver() {
        WebDriver driver = TL.get();
        if (driver != null) {
            driver.quit();
            TL.remove();
        }
    }
}
