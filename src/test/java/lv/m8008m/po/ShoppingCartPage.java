package lv.m8008m.po;

import lv.m8008m.driver.WDriver;
import org.openqa.selenium.By;

public class ShoppingCartPage extends BasePage {

    public static final By ITEMS = By.cssSelector(".cart-product-list .cart-product-item");

    public ShoppingCartPage(WDriver driver) {
        super(driver);
        LOCATION = "Shopping Cart Page";
    }
}
