package lv.m8008m.steps;


import io.cucumber.java.en.Then;
import lv.m8008m.driver.WDriver;
import lv.m8008m.po.MainPage;
import lv.m8008m.po.ShoppingCartPage;

public class ShoppingCartSteps {

    private SharedContext sharedData;
    private WDriver driver;

    private MainPage mainPage;
    private ShoppingCartPage shoppingCartPage;

    public ShoppingCartSteps(SharedContext sharedData, WDriver driver) {
        this.sharedData = sharedData;
        this.driver = driver;

        mainPage = new MainPage(driver);
        shoppingCartPage = new ShoppingCartPage(driver);
    }

    @Then("Users sees {int} item(s) in the shopping cart")
    public void usersSeesItemsInTheShoppingCart(int number) {
        mainPage.clickOnShoppingCartIcon();
        shoppingCartPage.waitElementsCountIs(number, ShoppingCartPage.ITEMS, "Shopping Cart Items");
    }
}
