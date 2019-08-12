package lv.m8008m.driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import lv.m8008m.utils.Log;
import lv.m8008m.utils.Props;
import lv.m8008m.utils.Wait;
import org.openqa.selenium.HasCapabilities;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.GeckoDriverService;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.service.DriverService;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

final class WDriverFactory {

    private static final String browser = Props.getProperty("browser", "chrome").toLowerCase();
    private static final String target = Props.getProperty("target", "local").toLowerCase();
    private static final boolean headless = Boolean.valueOf(Props.getProperty("headless", "false").toLowerCase());
    private static final boolean incognito = Boolean.valueOf(Props.getProperty("incognito", "false").toLowerCase());
    private static final String hubUrl = Props.getProperty("selenium.hub.url");

    private WDriverFactory() {

    }

    static DriverService createAndStartDriverService() {

        Log.systemLogger.info(Log.formatLogMessage("Resolving WebDriver and setting local WebDriver path according to browser and OS"));
        Drivers.valueOf(browser.toUpperCase()).resolveWebDriver();

        Log.systemLogger.info(Log.formatLogMessage("Creating driver service instance according to browser"));
        DriverService driverService = Drivers.valueOf(browser.toUpperCase()).newDriverService();

        try {
            Log.systemLogger.info(Log.formatLogMessage("Starting driver service instance"));
            driverService.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return driverService;
    }

    static WebDriver createDriver(DriverService driverService) {
        return createDriver(driverService, headless, incognito);
    }

    static WebDriver createDriver(DriverService driverService, boolean headless, boolean incognito) {
        WebDriver driver;
        URL url;

        Log.systemLogger.info(Log.formatLogMessage("Setting capabilities according to browser"));
        MutableCapabilities capabilities = Drivers.valueOf(browser.toUpperCase()).newCapabilities(headless, incognito);

        switch (target) {
            case "grid":
                try {
                    url = new URL(hubUrl);
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
                break;
            case "local":
            default:
                url = (driverService == null) ? null : driverService.getUrl();
        }

        if (url != null) {
            Log.systemLogger.info(Log.formatLogMessage("Creating RemoteWebDriver instance using capabilities and URL"));
            driver = new RemoteWebDriver(url, capabilities);
        } else {
            Log.systemLogger.info(Log.formatLogMessage("Creating WebDriver instance using capabilities"));
            driver = Drivers.valueOf(browser.toUpperCase()).newDriver(capabilities);
        }

        Log.systemLogger.info(Log.formatLogMessage("Configuring browser", driver));
        driver.manage().timeouts().pageLoadTimeout(Wait.PAGE_LOAD_TIMEOUT_S, TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(Wait.SCRIPT_TIMEOUT_S, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(Wait.IMPLICIT_WAIT_TIMEOUT_S, TimeUnit.SECONDS);
        driver.manage().window().maximize();

        // Getting info
        String osName = System.getProperty("os.name");
        String osArch = System.getProperty("os.arch");
        String osVersion = System.getProperty("os.version");
        String javaVersion = System.getProperty("java.version");
        String env = System.getProperty("env");
        String browserName = ((HasCapabilities) driver).getCapabilities().getBrowserName();
        String browserVersion = ((HasCapabilities) driver).getCapabilities().getVersion();
        String browserWindowSize = driver.manage().window().getSize().width + "x" + driver.manage().window().getSize().height;
        String OSPlatformName = ((HasCapabilities) driver).getCapabilities().getPlatform().toString();
        String OSPlatformVersion = (String) ((HasCapabilities) driver).getCapabilities().getCapability("platformVersion");
        String threadsCount = Props.getProperty("threads", "1");

        // Displaying info in console
        Log.systemLogger.info(Log.formatLogMessage("\nOS name: {}"
                        + "\nOS architecture: {}"
                        + "\nOS version: {}"
                        + "\nJava version: {}"
                        + "\nEnvironment: {}"
                        + "\nTarget: {}"
                        + "\nBrowser name: {}"
                        + "\nBrowser version: {}"
                        + "\nBrowser window size: {}"
                        + "\nOS (Platform) name: {}"
                        + "\nOS (Platform) version: {}"
                        + "\nThreads Count: {}"
                        + "\nPage Load timeout: {}"
                        + "\nScript timeout: {}"
                        + "\nImplicit Wait timeout: {}"
                        + "\nExplicit Wait timeout: {}"
                        + "\nPolling Explicit Wait timeout: {}"
                        + "\nJQuery Explicit Wait timeout: {}"
                        + "\nJQuery Polling Explicit Wait timeout: {}"
                        + "\nAll Capabilities:\n{}\n", driver),
                osName, osArch, osVersion, javaVersion, env, target,
                browserName, browserVersion, browserWindowSize,
                OSPlatformName != null ? OSPlatformName : "-", OSPlatformVersion != null ? OSPlatformVersion : "-",
                threadsCount,
                Wait.PAGE_LOAD_TIMEOUT_S, Wait.SCRIPT_TIMEOUT_S,
                Wait.IMPLICIT_WAIT_TIMEOUT_S,
                Wait.EXPLICIT_WAIT_TIMEOUT_S, Wait.POLLING_EXPLICIT_WAIT_TIMEOUT_MS,
                Wait.JQUERY_EXPLICIT_WAIT_TIMEOUT_S, Wait.JQUERY_POLLING_EXPLICIT_WAIT_TIMEOUT_MS,
                ((HasCapabilities) driver).getCapabilities().asMap());

        Log.systemLogger.info(Log.formatLogMessage("Browser is started in thread '{}' with id {} in JVM '{}'", driver), Utils.getThreadName(), Utils.getThreadId(), Utils.getProcessName());

        return driver;
    }

    private enum Drivers {
        FIREFOX {
            @Override
            public void resolveWebDriver() {
                WebDriverManager.firefoxdriver().setup();
            }

            @Override
            public MutableCapabilities newCapabilities(boolean headless, boolean incognito) {
                return getFirefoxOptions(headless, incognito);
            }

            @Override
            public WebDriver newDriver(MutableCapabilities capabilities) {
                return new FirefoxDriver((FirefoxOptions) capabilities);
            }

            @Override
            public DriverService newDriverService() {
                return GeckoDriverService.createDefaultService();
            }
        }, CHROME {
            @Override
            public void resolveWebDriver() {
                WebDriverManager.chromedriver().setup();
            }

            @Override
            public MutableCapabilities newCapabilities(boolean headless, boolean incognito) {
                return getChromeOptions(headless, incognito);
            }

            @Override
            public WebDriver newDriver(MutableCapabilities capabilities) {
                return new ChromeDriver((ChromeOptions) capabilities);
            }

            @Override
            public DriverService newDriverService() {
                return ChromeDriverService.createDefaultService();
            }
        };

        public abstract void resolveWebDriver();

        public abstract MutableCapabilities newCapabilities(boolean headless, boolean incognito);

        public abstract WebDriver newDriver(MutableCapabilities capabilities);

        public abstract DriverService newDriverService();
    }

    private static ChromeOptions getChromeOptions(boolean headless, boolean incognito) {
        ChromeOptions options = new ChromeOptions();

        options.setHeadless(headless);

        if (incognito)
            options.addArguments("incognito");

        options.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.ACCEPT);

        return options;
    }

    private static FirefoxOptions getFirefoxOptions(boolean headless, boolean incognito) {
        FirefoxOptions options = new FirefoxOptions();

        options.setHeadless(headless);

        if (incognito)
            options.addArguments("-private");

        options.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.ACCEPT);

        return options;
    }
}
