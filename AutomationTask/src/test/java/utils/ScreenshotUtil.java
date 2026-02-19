package utils;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;

import java.io.File;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class ScreenshotUtil {

    private ScreenshotUtil() {}

    public static String take(WebDriver driver, String name) {
        try {
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            Path dest = Path.of("target", "screenshots", name + "_" + ts + ".png");
            FileUtils.copyFile(src, dest.toFile());
            return dest.toString();
        } catch (Exception e) {
            return "screenshot_failed: " + e.getMessage();
        }
    }
}
