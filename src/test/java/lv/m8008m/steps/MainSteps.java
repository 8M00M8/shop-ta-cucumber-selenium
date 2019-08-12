package lv.m8008m.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import lv.m8008m.driver.WDriver;
import lv.m8008m.po.MainPage;

import java.util.List;

public class MainSteps {

    private SharedContext sharedData;
    private WDriver driver;

    private MainPage mainPage;

    public MainSteps(SharedContext sharedData, WDriver driver) {
        this.sharedData = sharedData;
        this.driver = driver;

        mainPage = new MainPage(driver);
    }

    @Given("User opens Main page")
    public void userNavigatesToMainPage() {
        mainPage.open();
        mainPage.waitElementIsVisible(MainPage.LOGO, "Logo");
    }

    @When("User navigates to the category:")
    public void userNavigatesToTheCategory(List<String> categoriesTree) {
        mainPage.navigateToTheCategory(categoriesTree);
    }
}
