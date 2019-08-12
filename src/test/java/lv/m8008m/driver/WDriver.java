package lv.m8008m.driver;

import lv.m8008m.utils.Log;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import java.util.ArrayList;
import java.util.List;


public class WDriver extends EventFiringWebDriver {

    /**
     * Used by DI and Hooks. Returns always main instance.
     */
    public WDriver() {
        super(WDriverManager.getMainDriver().getWrappedDriver());
    }

    /**
     * Extra instance creation
     */
    public WDriver(boolean headless, boolean incognito) {
        super(WDriverManager.getExtraDriver(headless, incognito).getWrappedDriver());
    }

    /**
     * Used by WDriverManager
     */
    WDriver(WebDriver driver) {
        super(driver);
    }

    @Override
    public void quit() {
        Log.systemLogger.info("Closing browser completely!");
        super.quit();
    }

    @Override
    public void close() {
        String handle = this.getWindowHandle();
        Log.systemLogger.info(Log.formatLogMessage("Closing window/tab: {}", this), handle);
        super.close();
        Log.systemLogger.info(Log.formatLogMessage("Closed window/tab: {}"), handle);
    }

    public void closeExtraHandles() {
        List<String> handles = new ArrayList<>(this.getWindowHandles());
        int handlesCount = handles.size();
        if (handlesCount > 1) {
            for (int i = handlesCount - 1; i > 0; i--) {
                this.switchTo().window(handles.get(i));
                this.close();
            }
            this.switchTo().window(handles.get(0));
            Log.systemLogger.info(Log.formatLogMessage("Closing extra {} windows/tabs", this), handlesCount - 1);
        }
    }

    private static int browserAlertCount = 0;

    public static synchronized void increaseBrowserAlertCount() {
        browserAlertCount++;
    }

    public static synchronized int getBrowserAlertCount() {
        return browserAlertCount;
    }

    public void closeAlerts() {
        int counter = 0;

        while (counter < 10) {
            counter++;
            try {
                this.switchTo().alert().accept();
                increaseBrowserAlertCount();
                Log.systemLogger.warn(Log.formatLogMessage("Browser alert N{} is closed!", this), counter);
            } catch (NoAlertPresentException e) {
                Log.systemLogger.info(Log.formatLogMessage("Browser alert N{} is not present.", this), counter);
                break;
            }
        }
    }

    public void deleteAllCookies() {
        this.manage().deleteAllCookies();
        Log.systemLogger.info(Log.formatLogMessage("All cookies are deleted.", this));
    }
}