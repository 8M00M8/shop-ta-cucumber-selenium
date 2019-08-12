package lv.m8008m.po;

import lv.m8008m.driver.WDriver;
import org.openqa.selenium.By;

public class QuickCartPopup extends BasePage {

    private static final By POPUP = By.id("quick-cart");
    private static final By BACK_BUTTON = By.cssSelector("#quick-cart .quick-cart-navigation .qc-back");

    public QuickCartPopup(WDriver driver) {
        super(driver);
        LOCATION = "Quick Cart Popup";
    }

    public void clickOnBackButton() {
        waitElementIsClickable(BACK_BUTTON, "Quick Cart Popup Back Button").click();
        waitElementIsInvisible(POPUP, "Quick Cart Popup");
    }
}
