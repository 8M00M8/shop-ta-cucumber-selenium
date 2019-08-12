package lv.m8008m.steps;

import io.cucumber.core.api.Scenario;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import lv.m8008m.driver.WDriver;
import lv.m8008m.utils.Log;
import lv.m8008m.utils.Utils;
import org.openqa.selenium.WebDriverException;


public class Hooks {

    private SharedContext sharedData;
    private WDriver driver;

    public Hooks(SharedContext sharedData) {
        this.sharedData = sharedData;
    }

    @Before(value = "@browser", order = 0)
    public void beforeForBrowserTests() {
        driver = new WDriver();
        driver.closeExtraHandles();
        driver.deleteAllCookies();
    }

    @Before(order = 1)
    public void beforeForAllTests(Scenario scenario) {
        SharedContext.scenarioID.set(scenario.getId());
        SharedContext.scenarioName.set(scenario.getName());

        Log.systemLogger.info(Log.formatLogMessage("Scenario started!", driver));
    }

    @After(value = "@browser", order = 1)
    public void afterForBrowserTests(Scenario scenario) {
        try {
            // To close existing browser alerts
            driver.closeAlerts();

            if (scenario.isFailed()) {
                scenario.embed(Utils.getScreenshotOnFailure(driver), "image/png", "Failure Screenshot");
            }

        } catch (WebDriverException e) {
            lv.m8008m.driver.Utils.handleExceptions(e, driver, "After Hook");
        }

        if (sharedData.extraDriver != null) sharedData.extraDriver.quit();
    }

    @After(order = 0)
    public void afterForAllTests(Scenario scenario) {
        Log.systemLogger.info(Log.formatLogMessage("Scenario finished!", driver));
    }
}
