package lv.m8008m.po;

import lv.m8008m.driver.WDriver;
import org.openqa.selenium.By;

public class ItemsGrid extends BasePage {

    private static final By SORT_BY_DESC_PRICE = By.cssSelector("label[for='sort-price-b']");
    private static final By GRID_ITEMS = By.cssSelector("#productList .product-list-item");
    private static final By ITEM_BUY_BUTTON = By.className("btn-buy-product");
    private static final By GRID = By.className("product-list");

    private QuickCartPopup quickCartPopup;

    public ItemsGrid(WDriver driver) {
        super(driver);
        LOCATION = "Items Grid";

        quickCartPopup = new QuickCartPopup(driver);
    }

    public void sortBy(String criteria) {
        switch (criteria) {
            case "descending price":
                driver.findElement(SORT_BY_DESC_PRICE).click();
                break;
        }
        waitAttributeValueNotContain("is-loading", "class", GRID, "Grid");
    }

    public void addItemToTheShoppingCartByIndex(int index) {
        getElementByIndexSafe(index, GRID_ITEMS).findElement(ITEM_BUY_BUTTON).click();
        quickCartPopup.clickOnBackButton();
    }
}
