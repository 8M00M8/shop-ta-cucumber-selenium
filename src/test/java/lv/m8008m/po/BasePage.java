package lv.m8008m.po;

import lv.m8008m.driver.WDriver;
import lv.m8008m.utils.Log;
import lv.m8008m.utils.Props;
import lv.m8008m.utils.Wait;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

import static lv.m8008m.utils.Utils.formatErrorMessage;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;

public abstract class BasePage {

    public static final String SHOP_BASE_URL = Props.getProperty("url.base.shop");
    public WDriver driver;
    protected WebDriverWait wait;
    public String LOCATION = "Base Page";

    public BasePage(WDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Wait.EXPLICIT_WAIT_TIMEOUT_S);
    }

    // AEM = Assertion error message
    public static final String ACTUAL_AEM_PART = "Actual ";
    public static final String IS_VISIBLE_AEM_PART = " is visible (displayed)!";
    public static final String NOT_VISIBLE_AEM_PART = " is NOT visible (displayed)!";
    public static final String NOT_CLICKABLE_AEM_PART = " is NOT clickable (enabled)!";
    public static final String NOT_EQUAL_AEM_PART = " is NOT equal to expected text!";
    public static final String CONTAIN_AEM_PART = " contains text!";
    public static final String IS_NOT_AEM_PART = " count is NOT ";

    public String isVisibleAEM(String title) {
        return title + IS_VISIBLE_AEM_PART;
    }

    public String notVisibleAEM(String title) {
        return title + NOT_VISIBLE_AEM_PART;
    }

    public String notClickableAEM(String title) {
        return title + NOT_CLICKABLE_AEM_PART;
    }

    public String textNotEqualAEM(String title) {
        return ACTUAL_AEM_PART + title + " text" + NOT_EQUAL_AEM_PART;
    }

    public String attributeValueContainAEM(String title, String attribute) {
        return ACTUAL_AEM_PART + title + " attribute '" + attribute + "' value" + CONTAIN_AEM_PART;
    }

    public String isNotAEM(String title, int count) {
        return ACTUAL_AEM_PART + title + IS_NOT_AEM_PART + count + "!";
    }

    /*
     * Waits. ExpectedConditions
     */

    public ExpectedCondition<Boolean> textToBe(final WebElement element, final String value) {
        return new ExpectedCondition<Boolean>() {
            private String currentValue = null;

            public Boolean apply(WebDriver driver) {
                try {
                    currentValue = element.getText();
                    return currentValue.equals(value);
                } catch (Exception var3) {
                    return false;
                }
            }

            public String toString() {
                return String.format("element text to be \"%s\". Current text: \"%s\". Element %s", value, currentValue, element);
            }
        };
    }

    /*
     * Waits. Helpers
     */

    public <T> T waitSafe(ExpectedCondition<T> expCond, String errorMessage) {
        try {
            return wait.until(expCond);
        } catch (TimeoutException e) {
            fail(errorMessage, e);
        }
        return null;
    }

    public <T> T waitSafe(WebDriverWait wait, ExpectedCondition<T> expCond, String errorMessage) {
        try {
            return wait.until(expCond);
        } catch (TimeoutException e) {
            fail(errorMessage, e);
        }
        return null;
    }

    public WebDriverWait getWebDriverWait(long timeoutInSeconds, long pollingInMillis) {
        return new WebDriverWait(driver, timeoutInSeconds, pollingInMillis);
    }

    /*
     * Waits. Wrappers
     */

    public WebElement waitElementIsVisible(By element, String elemName) {
        return waitSafe(ExpectedConditions.visibilityOfElementLocated(element),
                formatErrorMessage(notVisibleAEM(elemName), LOCATION));
    }

    public WebElement waitElementIsVisible(WebElement element, String elemName) {
        return waitSafe(ExpectedConditions.visibilityOf(element),
                formatErrorMessage(notVisibleAEM(elemName), LOCATION));
    }

    public boolean waitElementIsInvisible(By element, String elemName) {
        return waitSafe(ExpectedConditions.invisibilityOfElementLocated(element),
                formatErrorMessage(isVisibleAEM(elemName), LOCATION));
    }

    public boolean waitElementIsInvisible(WebElement element, String elemName) {
        return waitSafe(ExpectedConditions.invisibilityOf(element),
                formatErrorMessage(isVisibleAEM(elemName), LOCATION));
    }

    public WebElement waitElementIsClickable(By element, String elemName) {
        return waitSafe(ExpectedConditions.elementToBeClickable(element),
                formatErrorMessage(notClickableAEM(elemName), LOCATION));
    }

    public WebElement waitElementIsClickable(WebElement element, String elemName) {
        return waitSafe(ExpectedConditions.elementToBeClickable(element),
                formatErrorMessage(notClickableAEM(elemName), LOCATION));
    }

    public boolean waitElementTextIs(String value, By element, String elemName) {
        return waitSafe(ExpectedConditions.textToBe(element, value),
                formatErrorMessage(textNotEqualAEM(elemName), LOCATION, value));
    }

    public boolean waitElementTextIs(String value, WebElement element, String elemName) {
        return waitSafe(textToBe(element, value),
                formatErrorMessage(textNotEqualAEM(elemName), LOCATION, value));
    }

    public boolean waitAttributeValueNotContain(String value, String attribute, By element, String elemName) {
        return waitSafe(ExpectedConditions.not(ExpectedConditions.attributeContains(element, attribute, value)),
                formatErrorMessage(attributeValueContainAEM(elemName, attribute), LOCATION, value));
    }

    public boolean waitAttributeValueNotContain(String value, String attribute, WebElement element, String elemName) {
        return waitSafe(ExpectedConditions.not(ExpectedConditions.attributeContains(element, attribute, value)),
                formatErrorMessage(attributeValueContainAEM(elemName, attribute), LOCATION, value));
    }

    public List<WebElement> waitElementsCountIs(int size, By element, String elemName) {
        return waitSafe(ExpectedConditions.numberOfElementsToBe(element, size),
                formatErrorMessage(isNotAEM(elemName, size), LOCATION));
    }

    /*
     * Getters
     */

    public String getElementTextContent(By element) {
        return getElementTextContent(driver.findElement(element));
    }

    public String getElementTextContent(WebElement element) {
        String content = element.getAttribute("textContent").trim();
        Log.logger.debug("TextContent: {}", content);
        return content;
    }

    public String getElementTextContent(By element, String notAllowed, String replacement) {
        return getElementTextContent(driver.findElement(element), notAllowed, replacement);
    }

    public String getElementTextContent(WebElement element, String notAllowed, String replacement) {
        String content = element.getAttribute("textContent").replaceAll(notAllowed, replacement).trim();
        Log.logger.debug("TextContent: {}", content);
        return content;
    }

    public boolean isElementPresent(By by) {
        return !getElementsList(by).isEmpty();
    }

    public List<WebElement> getElementsList(By element) {
        return driver.findElements(element);
    }

    /**
     * Get element by exact text.
     *
     * @param text text
     * @param by   locator
     * @return element, else null
     */
    public WebElement getElementByExactText(String text, By by) {
        return getElementByExactText(text, getElementsList(by));
    }

    /**
     * Get element by exact text.
     *
     * @param text text
     * @param by   locator
     * @return element, else null
     */
    public WebElement getElementByExactTextSafe(String text, By by) {
        WebElement element = getElementByExactText(text, by);
        assertThat(element)
                .as(formatErrorMessage("Element with exact text '" + text + "' does NOT exist in the list!", LOCATION))
                .isNotNull();
        return element;
    }

    /**
     * Get element by exact text.
     *
     * @param text     text
     * @param elements elements
     * @return element, else null
     */
    public WebElement getElementByExactText(String text, List<WebElement> elements) {
        return elements
                .stream()
                .filter(elem -> getElementTextContent(elem).equals(text))
                .findFirst()
                .orElse(null);
    }

    /**
     * Get element by exact text.
     *
     * @param text     text
     * @param elements elements
     * @return element, else null
     */
    public WebElement getElementByExactTextSafe(String text, List<WebElement> elements) {
        WebElement element = getElementByExactText(text, elements);
        assertThat(element)
                .as(formatErrorMessage("Element with exact text '" + text + "' does NOT exist in the list!", LOCATION))
                .isNotNull();
        return element;
    }

    /**
     * Get element by exact text.
     *
     * @param text        text
     * @param by          locator
     * @param notAllowed  not allowed regex
     * @param replacement replacement
     * @return element, else null
     */
    public WebElement getElementByExactText(String text, By by, String notAllowed, String replacement) {
        return getElementByExactText(text, getElementsList(by), notAllowed, replacement);
    }

    /**
     * Get element by exact text.
     *
     * @param text        text
     * @param by          locator
     * @param notAllowed  not allowed regex
     * @param replacement replacement
     * @return element, else null
     */
    public WebElement getElementByExactTextSafe(String text, By by, String notAllowed, String replacement) {
        WebElement element = getElementByExactText(text, by, notAllowed, replacement);
        assertThat(element)
                .as(formatErrorMessage("Element with exact text '" + text + "' does NOT exist in the list!", LOCATION))
                .isNotNull();
        return element;
    }

    /**
     * Get element by exact text.
     *
     * @param text        text
     * @param elements    elements
     * @param notAllowed  not allowed regex
     * @param replacement replacement
     * @return element, else null
     */
    public WebElement getElementByExactText(String text, List<WebElement> elements, String notAllowed, String replacement) {
        return elements
                .stream()
                .filter(elem -> getElementTextContent(elem, notAllowed, replacement).equals(text))
                .findFirst()
                .orElse(null);
    }

    /**
     * Get element by exact text.
     *
     * @param text        text
     * @param elements    elements
     * @param notAllowed  not allowed regex
     * @param replacement replacement
     * @return element, else null
     */
    public WebElement getElementByExactTextSafe(String text, List<WebElement> elements, String notAllowed, String replacement) {
        WebElement element = getElementByExactText(text, elements, notAllowed, replacement);
        assertThat(element)
                .as(formatErrorMessage("Element with exact text '" + text + "' does NOT exist in the list!", LOCATION))
                .isNotNull();
        return element;
    }

    /**
     * Get element by index.
     *
     * @param index
     * @param by
     * @return element, else null
     */
    public WebElement getElementByIndex(int index, By by) {
        List<WebElement> elements = getElementsList(by);
        return (index < elements.size() && index > -1) ? elements.get(index) : null;
    }

    /**
     * Get element by index.
     *
     * @param index
     * @param by
     * @return element, else null
     */
    public WebElement getElementByIndexSafe(int index, By by) {
        WebElement element = getElementByIndex(index, by);
        assertThat(element)
                .as(formatErrorMessage("Element by index '" + index + "' does NOT exist in the list!", LOCATION))
                .isNotNull();
        return element;
    }

    /**
     * Get element by index.
     *
     * @param index
     * @param elements
     * @return element, else null
     */
    public WebElement getElementByIndex(int index, List<WebElement> elements) {
        return (index < elements.size() && index > -1) ? elements.get(index) : null;
    }

    /**
     * Get element by index.
     *
     * @param index
     * @param elements
     * @return element, else null
     */
    public WebElement getElementByIndexSafe(int index, List<WebElement> elements) {
        WebElement element = getElementByIndex(index, elements);
        assertThat(element)
                .as(formatErrorMessage("Element by index '" + index + "' does NOT exist in the list!", LOCATION))
                .isNotNull();
        return element;
    }
}
