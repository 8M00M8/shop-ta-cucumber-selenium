package lv.m8008m.steps;

import io.cucumber.java.en.When;
import lv.m8008m.driver.WDriver;
import lv.m8008m.po.ItemsGrid;

public class ItemsGridSteps {

    private SharedContext sharedData;
    private WDriver driver;

    private ItemsGrid itemsGrid;

    public ItemsGridSteps(SharedContext sharedData, WDriver driver) {
        this.sharedData = sharedData;
        this.driver = driver;

        itemsGrid = new ItemsGrid(driver);
    }

    @When("User sorts items by {string}")
    public void userSortsItemsBy(String criteria) {
        itemsGrid.sortBy(criteria);
    }

    @When("Users adds {int} first item(s) to the shopping cart")
    public void usersAddsFirstItemsToTheShoppingCart(int number) {
        for (int i = 0; i < number; i++)
            itemsGrid.addItemToTheShoppingCartByIndex(i);
    }
}
