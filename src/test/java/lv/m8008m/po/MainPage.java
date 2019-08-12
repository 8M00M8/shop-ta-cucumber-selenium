package lv.m8008m.po;

import lv.m8008m.driver.WDriver;
import org.openqa.selenium.By;

import java.util.List;

public class MainPage extends BasePage {

    public static final By LOGO = By.className("logo");
    private static final By LEVEL_1_CATEGORIES = By.cssSelector("#site-menu-main .site-menu-popup ul > li > .menu-item .menu-item-link");
    private static final By GRID_CATEGORIES = By.cssSelector(".main .category-children ul > li a span");
    private static final By GRID_TITLE = By.cssSelector(".main h1");
    private static final By BANNER_CLOSE_ICON = By.cssSelector(".banner .popup-close");
    private static final By SHOPPING_CART_ICON = By.cssSelector(".header-cart a");

    public MainPage(WDriver driver) {
        super(driver);
        LOCATION = "Main Page";
    }

    public void open() {
        driver.get(SHOP_BASE_URL);
        if (isElementPresent(BANNER_CLOSE_ICON))
            driver.findElement(BANNER_CLOSE_ICON).click();
    }

    public void navigateToTheCategory(List<String> categoriesTree) {
        if (!categoriesTree.isEmpty()) {
            getElementByExactTextSafe(categoriesTree.get(0), LEVEL_1_CATEGORIES, "\n", " ").click();
            waitElementTextIs(categoriesTree.get(0), GRID_TITLE, "Grid (Current View) Title");
            for (int i = 1; i < categoriesTree.size(); i++) {
                getElementByExactTextSafe(categoriesTree.get(i), GRID_CATEGORIES, "\n", " ").click();
                waitElementTextIs(categoriesTree.get(i), GRID_TITLE, "Grid (Current View) Title");
            }
        }
    }

    public void clickOnShoppingCartIcon() {
        driver.findElement(SHOPPING_CART_ICON).click();
    }
}
