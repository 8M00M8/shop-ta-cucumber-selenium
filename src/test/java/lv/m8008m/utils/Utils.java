package lv.m8008m.utils;

import lv.m8008m.driver.WDriver;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriverException;

public class Utils {

    public static byte[] getScreenshotOnFailure(WDriver driver) {
        try {
            byte[] screenshot = driver.getScreenshotAs(OutputType.BYTES);
            Log.systemLogger.info(Log.formatLogMessage("Failure screenshot is created.", driver));
            return screenshot;
        } catch (WebDriverException e) {
            Log.systemLogger.error(Log.formatLogMessage("Failure screenshot creation is failed!", driver), e);
            return null;
        }
    }

    public static String formatErrorMessage(String message, String location) {
        return formatErrorMessage(message, location, null, null, null);
    }

    public static <T> String formatErrorMessage(String message, String location, T expected) {
        return formatErrorMessage(message, location, null, expected, "Expected");
    }

    public static <T> String formatErrorMessage(String message, String location, T actual, T expected) {
        return formatErrorMessage(message, location, actual, expected, "Expected");
    }

    public static <T> String formatErrorMessage(String message, String location, T actual, T expected, String expectedLabel) {
        String str = message + "\n";

        if (location != null && !location.isEmpty()) str = str + "Location: " + location + "\n";
        if (actual != null) str = str + "Actual:\n" + actual + "\n";
        if (expected != null) str = str + expectedLabel + ":\n" + expected + "\n";

        return str;
    }
}
