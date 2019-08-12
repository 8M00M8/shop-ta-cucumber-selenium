package lv.m8008m.driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.AbstractWebDriverEventListener;
import org.openqa.selenium.support.events.WebDriverEventListener;

public class WDriverEventListener extends AbstractWebDriverEventListener implements WebDriverEventListener {

    @Override
    public void onException(Throwable throwable, WebDriver driver) {
        Utils.handleExceptions(throwable, driver, "Event Listener");
    }
}
