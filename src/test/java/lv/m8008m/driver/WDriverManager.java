package lv.m8008m.driver;


import lv.m8008m.utils.Log;
import lv.m8008m.utils.Props;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.service.DriverService;
import org.openqa.selenium.support.events.WebDriverEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class WDriverManager {
    private static final String target = Props.getProperty("target", "local").toLowerCase();
    private static final WebDriverEventListener eventListener = new WDriverEventListener();

    private static List<DriverService> threadDriverServices = Collections.synchronizedList(new ArrayList<>());
    private static List<WDriver> threadDrivers = Collections.synchronizedList(new ArrayList<>());
    private static ThreadLocal<DriverService> THREAD_DRIVER_SERVICE = ThreadLocal.withInitial(() -> {
        if (target.equals("local")) {
            DriverService driverService = WDriverFactory.createAndStartDriverService();
            threadDriverServices.add(driverService);
            return driverService;
        } else
            return null;
    });
    private static ThreadLocal<WDriver> THREAD_DRIVER = ThreadLocal.withInitial(() -> {
        WebDriver webDriver = WDriverFactory.createDriver(THREAD_DRIVER_SERVICE.get());
        WDriver driver = new WDriver(webDriver);
        driver.register(eventListener);
        threadDrivers.add(driver);
        return driver;
    });

    private static int browserRestartCount = 0;

    private static synchronized void increaseBrowserRestartCount() {
        browserRestartCount++;
    }

    public static synchronized int getBrowserRestartCount() {
        return browserRestartCount;
    }

    private static final Thread CLOSE_THREAD = new Thread() {
        @Override
        public void run() {
            threadDrivers.forEach(WDriver::quit);

            if (target.equals("local")) {
                threadDriverServices.forEach(driverService -> {
                    Log.systemLogger.info("Stopping driver service!");
                    driverService.stop();
                });
            }
        }
    };

    static {
        Log.systemLogger.info("Adding shutdown hook for closing browsers!");
        Runtime.getRuntime().addShutdownHook(CLOSE_THREAD);
    }

    public static void restart(boolean quitBeforeStart, String location) {
        Log.systemLogger.info(Log.formatLogMessage("Restarting browser! Quit before start: {}", location, THREAD_DRIVER.get()), quitBeforeStart);
        increaseBrowserRestartCount();
        threadDrivers.remove(THREAD_DRIVER.get());
        if (quitBeforeStart) THREAD_DRIVER.get().quit();
        THREAD_DRIVER.remove();
    }

    static WDriver getMainDriver() {
        return THREAD_DRIVER.get();
    }

    public static WDriver getExtraDriver(boolean headless, boolean incognito) {
        WebDriver webDriver = WDriverFactory.createDriver(null, headless, incognito);
        WDriver wDriver = new WDriver(webDriver);
        wDriver.register(eventListener);

        return wDriver;
    }
}