package org.example;


import WebAutomationBase.helper.ElementHelper;
import WebAutomationBase.helper.StoreHelper;
import WebAutomationBase.model.ElementInfo;
import com.thoughtworks.gauge.Step;
import driver.Driver;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;


public class BaseSteps extends Driver {


    private final Logger logger = LoggerFactory.getLogger(BaseSteps.class);
    private final Actions action;

    public static int DEFAULT_MAX_ITERATION_COUNT = 150;
    public static int DEFAULT_MILLISECOND_WAIT_AMOUNT = 100;


    public BaseSteps() {
        action = new Actions(driver);
    }

    private WebElement findElement(String key) {
        ElementInfo elementInfo = StoreHelper.INSTANCE.findElementInfoByKey(key);
        By infoParam = ElementHelper.getElementInfoToBy(elementInfo);
        WebDriverWait webDriverWait = new WebDriverWait(driver, 20);
        WebElement webElement = webDriverWait
                .until(ExpectedConditions.presenceOfElementLocated(infoParam));
        webDriverWait.until((ExpectedCondition<Boolean>) wd ->
                ((JavascriptExecutor) wd).executeScript("return document.readyState").equals("complete"));
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center', inline: 'center'})",
                webElement);
        highlightElement(webElement);
        return webElement;
    }

    private List<WebElement> findElements(String key) {
        ElementInfo elementInfo = StoreHelper.INSTANCE.findElementInfoByKey(key);
        By infoParam = ElementHelper.getElementInfoToBy(elementInfo);

        return driver.findElements(infoParam);
    }

    public void saveValue(String key, String value) {
        StoreHelper.INSTANCE.saveValue(key, value);
    }

    public String getValue(String saveKey) {
        return StoreHelper.INSTANCE.getValue(saveKey);
    }

    @Step({"<elementKey> li elementin text degerini <saveKey> olarak sakla"})
    public void getTextAndSaveKey(String elementKey, String saveKey) {
        saveValue(saveKey, getText(elementKey));
    }

    @Step({"<value> degerini <saveKey> olarak sakla"})
    public void saveKey(String value, String saveKey) {
        saveValue(saveKey, value);
    }


    @Step({"<saveKey> olarak saklanan degeri konsola yazdir"})
    public void writeToConsoleSavedValue(String saveKey) {
        writeToConsole(getValue(saveKey));
    }

    @Step({"<key> degerini console ekranina yaz"})
    public void writeToConsole(String key) {
        System.out.println(key);
    }

    private void clickElement(WebElement element) {
        element.click();
    }

    private void clickElementBy(String key) {
        findElement(key).click();
    }


    private void sendKeys(String text, String key) {
        if (!key.equals("")) {
            findElement(key).sendKeys(text);
            logger.info(key + " elementine " + text + " texti yazıldı.");
        }
    }

    private void sendKeys(CharSequence[] charSequence, String key) {
        if (!key.equals("")) {
            findElement(key).sendKeys(charSequence);
        }
    }

    private void clearTextArea(String key) {
        findElement(key).clear();
        logger.info("Text area cleared ");
    }

    @Step("<key> li alanı temizle, <value> değerini yaz")
    public void clearTypeAndEnter(String key, String value) {
        clearTextArea(key);
        sendKeys(value, key);

    }

    @Step({"Focus on last tab",
            "Son sekmeye odaklan"})
    public void chromeFocusLastTab() {
        ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(tabs.size() - 1));
    }

    private String getText(String key) {
        logger.info("Getting text from given locator ");
        return findElement(key).getText();
    }


    private String getTitle() {
        return driver.getTitle();
    }


    private JavascriptExecutor getJSExecutor() {
        return (JavascriptExecutor) driver;
    }


    private Object executeJS(String script, boolean wait) {
        return wait ? getJSExecutor().executeScript(script, "") : getJSExecutor().executeAsyncScript(script, "");
    }


    private void scrollTo(int x, int y) {
        String script = String.format("window.scrollTo(%d, %d);", x, y);
        executeJS(script, true);
    }


    private WebElement scrollToElementToBeVisible(String key) {
        ElementInfo elementInfo = StoreHelper.INSTANCE.findElementInfoByKey(key);
        WebElement webElement = driver.findElement(ElementHelper.getElementInfoToBy(elementInfo));
        if (webElement != null) {
            scrollTo(webElement.getLocation().getX(), webElement.getLocation().getY() - 100);
        }
        return webElement;
    }

    private WebElement scrollToElementToBeVisibleWithElement(WebElement element) {
        if (element != null) {
            scrollTo(element.getLocation().getX(), element.getLocation().getY() - 100);
        }
        return element;
    }


    //For highlighting the element to be located after scroll
    private static void highlightElement(WebElement ele) {
        try {
            for (int i = 0; i < 3; i++) {
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("arguments[0].setAttribute('style', arguments[1]);", ele, "color: red; border: 2px solid red;");
            }
        } catch (Throwable t) {
            System.err.println("Error came : " + t.getMessage());
        }
    }

    private void hoverElementBy(String key) {
        WebElement webElement = findElement(key);
        action.moveToElement(webElement).build().perform();
    }

    @Step("Hover by given element <key>")
    public void hoverByGivenElement(String key) {
        hoverElementBy(key);
    }

    private WebElement findElementWithKey(String key) {
        return findElement(key);
    }

    private Boolean isEnabled(String key) {
        logger.info("Checking is element enable on the page ");

        return findElement(key).isEnabled();
    }

    private boolean isDisplayedBy(By by) {
        return driver.findElement(by).isDisplayed();
    }


    private void selectByVisibleText(String key, String text) {

        Select select = new Select(findElement(key));
        select.selectByVisibleText(text);
        logger.info("Selected from dropdown");
    }

    @Step("Select from <key> list with <text>")
    public void select(String key, String text) {
        selectByVisibleText(key, text);

    }

    @Step("Select <key> list with <option>")
    public void s(String key, String option) {
        List<WebElement> list = findElements(key);
        for (WebElement l : list) {
            if (l.getText().equals(option)) {
                l.click();
                break;
            }
        }

    }

    private void sleepSeconds(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
            logger.info("Waited " + seconds + " seconds ");
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());

        }
    }

    @Step("Navigate to home page")
    public void navigateToHomePage() {
        driver.navigate().to(System.getenv("APP_URL"));
        logger.info("Navigated to homepage ");
    }


    @Step("Navigate to given page <string>")
    public void navigateToGivenPage(String string) {
        driver.navigate().to(string);
        logger.info("Navigated to given url :" + string);
    }


    @Step({"<key> alanına kaydır"})
    public void scrollToElement(String key) {
        scrollToElementToBeVisible(key);
        logger.info(key + " elementinin olduğu alana kaydırıldı");


    }

    private String getAttributeValue(String key, String value) {
        String attributeValueOfGivenElement = "";
        if (findElement(key) != null) {
            attributeValueOfGivenElement = findElement(key).getAttribute(value);
        }
        return attributeValueOfGivenElement;
    }

    @Step({"<key> elementinin <value> attribute degerini al ve <saveKey> olarak sakla"})
    public void asdf(String key, String value, String saveKey) {
        saveKey(getAttributeValue(key, value), saveKey);
        System.out.println("teststostosotst");
        writeToConsoleSavedValue(saveKey);
    }


    @Step({"Select address from auto suggestive dropdown with <key>"})
    public void selectFromAutoSuggestiveDropDown(String key) {
        String optionToSelect = "Mexico City, CDMX, Mexico";
        List<WebElement> contentList = findElements(key);
        WebElement tempElement = null;
        for (WebElement e : contentList) {
            String currentOption = e.getAttribute("textContent");
            if (currentOption.equals(optionToSelect)) {
                tempElement = e.findElement(By.xpath("//parent::div"));
                // Actions ac = new Actions(driver);
                //ac.moveToElement(tempElement).click().build().perform();
                clickElement(tempElement);
                break;
            }


        }

    }


    @Step({"Check if element <key> contains text <expectedText>",
            "<key> elementi <text> değerini içeriyor mu kontrol et"})
    public void checkElementContainsText(String key, String expectedText) {
        boolean containsText = findElement(key).getText().contains(expectedText);
        assertTrue("Expected text is not contained", containsText);
        logger.info(key + " elementi" + expectedText + "değerini içeriyor.");
    }


    @Step({"Check if title contains text <expectedText>",
            "Title <text> değerini içeriyor mu kontrol et"})
    public void checkElementContainsText(String expectedText) {
        boolean containsText = getTitle().contains(expectedText);
        assertTrue("Expected text is not contained", containsText);
        logger.info(" Title" + expectedText + "değerini içeriyor.");
    }


    @Step({"Click with javascript to xpath <xpath>",
            "Javascript ile xpath tıkla <xpath>"})
    public void javascriptClickerWithXpath(String xpath) {
        assertTrue("Element bulunamadı", isDisplayedBy(By.xpath(xpath)));
        javaScriptClicker(driver.findElement(By.xpath(xpath)));
        logger.info("Javascript ile " + xpath + " tıklandı.");
    }

    @Step({"Click with javascript to css <css>",
            "Javascript ile css tıkla <css>"})
    public void javascriptClickerWithCss(String css) {
        assertTrue("Element bulunamadı", isDisplayedBy(By.cssSelector(css)));
        javaScriptClicker(driver.findElement(By.cssSelector(css)));
        logger.info("Javascript ile " + css + " tıklandı.");
    }


    @Step({"Element with <key> is displayed on website",
            "<key> li elementin ekranda goruntulendigini dogrula"})
    public void isDisplayedWithKey(String key) {
        WebElement element = findElementWithKey(key);
        assertTrue("Section bulunamadı", element.isDisplayed());
    }


    @Step({"Element with <key> is enable on website",
            "<key> li element sayfada enable mı kontrol et "})
    public void isEnableSection(String key) {
        WebElement element = findElementWithKey(key);
        assertTrue("Section bulunamadı", element.isEnabled());
        logger.info(key + "li section sayfada bulunuyor.");
    }


    @Step("<key> menu listesinden rasgele sec")
    public void chooseRandomElementFromList(String key) {
        WebElement element = randomPick(key);
        isDisplayedWithKey(key);
        clickElement(element);
    }


    public void javaScriptClicker(WebElement element) {

        JavascriptExecutor jse = ((JavascriptExecutor) driver);
        jse.executeScript("var evt = document.createEvent('MouseEvents');"
                + "evt.initMouseEvent('click',true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0,null);"
                + "arguments[0].dispatchEvent(evt);", element);
    }

    @Step("Click with js method <key>")
    public void jsClicker(String key) {
        WebElement element = findElement(key);
        JavascriptExecutor jse = ((JavascriptExecutor) driver);
        jse.executeScript("var evt = document.createEvent('MouseEvents');"
                + "evt.initMouseEvent('click',true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0,null);"
                + "arguments[0].dispatchEvent(evt);", element);
    }


    @Step("Check if new tab has verified url <key>")
    public void switchTabsUsingPartOfUrl(String key) {
        String currentHandle = null;
        try {
            final Set<String> handles = driver.getWindowHandles();
            if (handles.size() > 1) {
                currentHandle = driver.getWindowHandle();
            }
            if (currentHandle != null) {
                for (final String handle : handles) {
                    driver.switchTo().window(handle);
                    if (driver.getCurrentUrl().contains(key) && !currentHandle.equals(handle)) {
                        break;
                    }
                }
            } else {
                for (final String handle : handles) {
                    driver.switchTo().window(handle);
                    if (driver.getCurrentUrl().contains(key)) {
                        break;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Switching tabs failed");
        }
    }


    @Step({"Scroll to <key> field",
            "<key> alanına js ile kaydır"})
    public void scrollToElementWithJs(String key) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", findElement(key));
        waitByMilliSeconds(3000);
    }


    @Step({"Check if element <key> exists else print message <message>",
            "Element <key> var mı kontrol et yoksa hata mesajı ver <message>"})
    public void getElementWithKeyIfExistsWithMessage(String key, String message) {
        ElementInfo elementInfo = StoreHelper.INSTANCE.findElementInfoByKey(key);
        By by = ElementHelper.getElementInfoToBy(elementInfo);

        int loopCount = 0;
        while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
            if (driver.findElements(by).size() > 0) {
                logger.info(key + " elementi bulundu.");
                return;
            }
            loopCount++;
            waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
        }
        Assert.fail(message);
    }

    public void findTextAndClick(String key, String text) {

        List<WebElement> searchText = findElements(key);
        for (int i = 0; i < searchText.size(); i++) {
            if (searchText.get(i).getText().trim().contains(text)) {
                searchText.get(i).click();
                logger.info("Bulunan elemente tıklandı.");
                break;
            }
        }
    }


    private void hoverElement(WebElement element) {
        action.moveToElement(element).build().perform();
    }

    @Step({"Click to element <key>",
            "Elementine tıkla <key>"})
    public void clickElement(String key) {
        if (!key.equals("")) {
            WebElement element = findElement(key);
            hoverElement(element);
            waitByMilliSeconds(500);
            clickElement(element);
            logger.info(key + " elementine tıklandı.");
        }
    }

    @Step({"<saveKey1> ve <saveKey2> degerlerini dogrula"})
    public void verifySaveKeyValues(String saveKey1, String saveKey2) {
        String firstValue = getValue(saveKey1);
        String secondValue = getValue(saveKey2);
        assertTrue("SaveKey degerleri dogrulanamadi." + firstValue + "-" + secondValue, firstValue.equals(secondValue));

    }

    @Step({"Wait <value> milliseconds",
            "<long> milisaniye bekle"})
    public void waitByMilliSeconds(long milliseconds) {
        try {
            logger.info(milliseconds + " milisaniye bekleniyor.");
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public WebElement randomPick(String key) {
        List<WebElement> elements = findElements(key);
        Random random = new Random();
        int index = random.nextInt(elements.size());
        return elements.get(index);
    }

    @Step("Check if <key> element's attribute value <active>")
    public void asdf(String key, String active) {
        WebElement element = findElementWithKey(key);
        String a = element.getAttribute(active);
        assertEquals("active", a);
    }


    @Step("Check position <key1> relative to <key2>")
    public Boolean checkRelativePosition(String key1, String key2) {
        WebElement parent = findElementWithKey(key1);
        WebElement child = findElementWithKey(key2);
        boolean isAbove = false;
        if (!(parent.getLocation() == null && child.getLocation() == null)) {
            if (parent.getLocation().getY() - child.getLocation().getY() < 0) {
                isAbove = true;
                logger.info(key1 + " element is above the" + key2);
            }

            if (parent.getLocation().getY() - child.getLocation().getY() > 0) {
                isAbove = false;
                logger.info(key1 + " element is under the " + key2);
            }
        }
        return isAbove;
    }


    @Step({"Check if element <key> has attribute <attribute>",
            "<key> elementi <attribute> niteliğine sahip mi"})
    public void checkElementAttributeExists(String key, String attribute) {
        WebElement element = findElement(key);
        int loopCount = 0;
        while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
            if (element.getAttribute(attribute) != null) {
                logger.info(key + " elementi " + attribute + " niteliğine sahip.");
                return;
            }
            loopCount++;
            waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
        }
        Assert.fail("Element DOESN't have the attribute: '" + attribute + "'");
    }

    @Step("Wait until image uploaded and click checkbox <key>")
    public void waitUntilImageUploadedAndClickCheckBox(String key) {
        WebDriverWait wait = new WebDriverWait(driver, 15);
        boolean elm = wait.until(ExpectedConditions.attributeContains(By.cssSelector("img[class='custom-image__uploaded-image']"), "src", "image/"));
        if (elm) findElement(key).click();
    }

    @Step("Upload file to given <key>")
    public void uploadImage(String key) throws AWTException, InterruptedException {
        Robot robot = new Robot();
        String userDirectory = System.getProperty("user.dir");
        String path = userDirectory + "\\ppberkay.png";

        // disable the internal click by overriding the method in dom
        executeJS("HTMLInputElement.prototype.click = function () {" +
                "    if (this.type !== 'file') {" +
                "        HTMLElement.prototype.click.call(this);" +
                "    }" +
                "    else if (!this.parentNode) {" +
                "        this.style.display = 'none';" +
                "        this.ownerDocument.documentElement.appendChild(this);" +
                "        this.addEventListener('change', () => this.remove());" +
                "    }" +
                "}", true);
        javaScriptClicker(findElement(key));
        Thread.sleep(2000);
        robot.keyPress(KeyEvent.VK_ESCAPE);
        robot.keyRelease(KeyEvent.VK_ESCAPE);
        findElement(key).sendKeys(path);
    }

    @Step("Wait until elements loaded <key> and verify sorting")
    public void waitUntilElementsLoadedAndCheckSortingSuccessful(String key) {
        ElementInfo elementInfo = StoreHelper.INSTANCE.findElementInfoByKey(key);
        By infoParam = ElementHelper.getElementInfoToBy(elementInfo);
        WebDriverWait wait = new WebDriverWait(driver, 15);
        List<WebElement> elements = wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(infoParam, 59));
        Integer[] actual = new Integer[elements.size()];
        Integer[] sorted = new Integer[elements.size()];
        int i = 0;
        for (WebElement e : elements) {
            actual[i] = sorted[i] = Integer.parseInt((e.getText()).replace(",", ""));
            i++;
        }
        Arrays.sort(sorted, Collections.reverseOrder());
        for (int k = 0; k < elements.size(); k++) {
            assertEquals(actual[k], sorted[k]);
        }


    }

    @Step("<key> product is exist on cart page")
    public void isExistOnCartPage(String key) {
        assertTrue(findElements(key).size() > 0);
    }


    @Step("Hover all elements in <key> with js")
    public void hoverAllMenuLinksWithJs(String key) {
        List<WebElement> elements = findElements(key);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        for (WebElement element : elements) {
            js.executeScript("var element = arguments[0];"
                    + "var mouseEventObj = document.createEvent('MouseEvents');"
                    + "mouseEventObj.initEvent( 'mouseover', true, true );"
                    + "element.dispatchEvent(mouseEventObj);", element);

        }

    }

    @Step("Check broken links with <key> element")
    public void brokenLinkCheck(String key) {
        String url;
        int respCode;
        HttpURLConnection huc;
        Iterator<WebElement> it = findElements(key).iterator();
        while (it.hasNext()) {
            url = it.next().getAttribute("href");
            if (!url.startsWith(System.getenv("APP_URL"))) continue;
            if (url.isEmpty()) continue;

            try {
                huc = (HttpURLConnection) (new URL(url).openConnection());
                huc.setRequestMethod("HEAD");
                huc.connect();
                respCode = huc.getResponseCode();
                assertFalse(respCode >= 400);
                logger.info(url + " is a valid link");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Step("Wait until element visible <key>")
    public void waitUntilElementVisible(String key) {
        WebDriverWait wait = new WebDriverWait(driver, 15);
        wait.until(ExpectedConditions.visibilityOf(findElement(key)));

    }
}
