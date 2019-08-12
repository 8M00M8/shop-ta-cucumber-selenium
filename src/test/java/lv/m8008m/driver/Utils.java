package lv.m8008m.driver;


import lv.m8008m.utils.Log;
import org.openqa.selenium.WebDriver;

import java.lang.management.ManagementFactory;


public class Utils {

    public static String getProcessName() {
        return ManagementFactory.getRuntimeMXBean().getName();
    }

    public static String getThreadName() {
        return Thread.currentThread().getName();
    }

    public static long getThreadId() {
        return Thread.currentThread().getId();
    }

    public static String getInfo() {
        return ManagementFactory.getRuntimeMXBean().getName() + "." + Thread.currentThread().getName() + "(" + Thread.currentThread().getId() + ")";
    }

    public static void handleExceptions(Throwable throwable, WebDriver driver, String location) {
        String throwableName = throwable.getClass().getSimpleName();

        if (throwableName.equals("NoSuchSessionException")) {
            Log.systemLogger.error(Log.formatLogMessage("{} is caught! Browser will be restarted!\n{}", location, driver), throwableName, throwable);
            WDriverManager.restart(false, location);
        }

        // org.openqa.selenium.WebDriverException: Session [] was terminated due to BROWSER_TIMEOUT
        if (throwableName.equals("WebDriverException") && throwable.getMessage().startsWith("Session")) {
            Log.systemLogger.error(Log.formatLogMessage("{} is caught! Browser will be restarted!\n{}", location, driver), throwableName, throwable);
            WDriverManager.restart(false, location);
        }

        if (throwableName.equals("TimeoutException") && throwable.getMessage().startsWith("timeout")) {
            Log.systemLogger.error(Log.formatLogMessage("{} is caught! Browser will be restarted!\n{}", location, driver), throwableName, throwable);
            WDriverManager.restart(true, location);
        }
    }
}