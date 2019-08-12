package lv.m8008m.utils;

import lv.m8008m.driver.WDriver;
import lv.m8008m.steps.SharedContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

public class Log {

    public static final Logger systemLogger = LogManager.getLogger("System");
    public static final Logger logger = LogManager.getLogger();

    public static String formatLogMessage(String message) {
        return formatLogMessageInternal(message, null, null);
    }

    public static String formatLogMessage(String message, String location) {
        return formatLogMessageInternal(message, location, null);
    }

    public static String formatLogMessage(String message, WebDriver driver) {
        return formatLogMessageInternal(message, null, driver);
    }

    public static String formatLogMessage(String message, String location, WebDriver driver) {
        return formatLogMessageInternal(message, location, driver);
    }

    private static String formatLogMessageInternal(String message, String location, WebDriver driver) {
        String str = "\n" + lv.m8008m.driver.Utils.getInfo();
        String scenarioId = SharedContext.scenarioID.get();
        String scenarioName = SharedContext.scenarioName.get();

        if (driver != null)
            str = str + "\nSession ID: " + ((driver instanceof RemoteWebDriver) ?
                    ((RemoteWebDriver) driver).getSessionId() :
                    ((RemoteWebDriver) ((WDriver) driver).getWrappedDriver()).getSessionId());
        if (scenarioId != null && !scenarioId.isEmpty()) str = str + "\nScenario ID: " + scenarioId;
        if (scenarioName != null && !scenarioName.isEmpty()) str = str + "\nScenario Name: " + scenarioName;
        if (location != null && !location.isEmpty()) str = str + "\nLocation: " + location;

        str = str + "\n" + message + "\n";

        return str;
    }
}
