package com.orangeHrm.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;

@SuppressWarnings("null")
public class SeleniumTestHelper {

// ===================== Frame Helpers =====================

    /**
     * Default explicit wait for frame availability. Adjust if needed.
     */
    private static final Duration FRAME_WAIT_TIMEOUT = Duration.ofSeconds(10);

    // Enum for locator strategies to improve maintainability
    public enum LocatorStrategy {
        XPATH("xpath", By::xpath),
        CSS("css", By::cssSelector),
        ID("id", By::id),
        NAME("name", By::name),
        CLASS("class", By::className),
        LINK_TEXT("link", By::linkText);

        private final String strategyName;
        private final java.util.function.Function<String, By> byFunction;


        LocatorStrategy(String strategyName, java.util.function.Function<String, By> byFunction) {
            this.strategyName = strategyName;
            this.byFunction = byFunction;
        }

        public static LocatorStrategy fromString(String strategy) {
            for (LocatorStrategy loc : LocatorStrategy.values()) {
                if (loc.strategyName.equalsIgnoreCase(strategy)) {
                    return loc;
                }
            }
            throw new IllegalArgumentException("Invalid locator strategy: " + strategy);
        }

        public By getBy(String locator) {
            return byFunction.apply(locator);
        }
    }

    // Lazily initialize driver - will be set by tests via Driver.getInstance()
    // DO NOT initialize as static field to avoid premature browser instantiation
    private static WebDriver driver;

    /**
     * Updates the driver instance - should be called from test hooks
     */
    public static void setDriver(WebDriver driverInstance) {
        driver = driverInstance;
    }

    /**
     * Gets the current driver instance
     */
    public static WebDriver getDriver() {
        if (driver == null) {
            driver = Driver.getInstance();
        }
        return driver;
    }

    public static void clickOnButton(WebElement element) {
        try {
            if (element.isDisplayed()) {
                element.click();
            }
        } catch (NoSuchElementException exe) {
            Assert.fail(exe.getMessage() + " exception occured.");
        }

    }

    public static void enterTextInTextBox(WebElement element, String text) {
        try {
            if (element.isDisplayed()) {
                element.clear();
                element.sendKeys(text);
            }
        } catch (NoSuchElementException exe) {
            Assert.fail(exe.getMessage() + " exception occured.");
        }
    }

    public static void enterText(WebElement element, String text) {
        try {
            if (element.isDisplayed()) {
                element.clear();
                element.sendKeys(text);
            }
        } catch (NoSuchElementException exe) {
            Assert.fail(exe.getMessage() + " exception occured.");
        }
    }

    public static void clear(WebElement element) {
        try {
            if (element.isDisplayed()) {
                element.clear();
            }
        } catch (NoSuchElementException exe) {
            Assert.fail(exe.getMessage() + " exception occured.");
        }
    }

    public enum DropDownMode {
        VISIBLE_TEXT, INDEX, VALUE
    }

    public static void selectFromDropDown(WebElement element, String value,
                                          DropDownMode mode) {
        try {
            Select select = new Select(element);

            switch (mode) {
                case VISIBLE_TEXT:
                    select.selectByVisibleText(value);
                    break;
                case INDEX:
                    select.selectByIndex(Integer.parseInt(value));
                    break;
                case VALUE:
                    select.selectByValue(value);

            }
            retryCount = 0;

        } catch (Exception exe) {

            if (retryCount > 3) {
                throw exe;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            retryCount++;
            selectFromDropDown(element, value, mode);

        }
    }

    private static int retryCount = 0;

    public static void selectFromDropDown(WebElement element, String value,
                                          String mode) {
        try {
            Select select = new Select(element);
            if (mode.equalsIgnoreCase("value")) {
                select.selectByValue(value);
            } else if (mode.equalsIgnoreCase("index")) {
                select.selectByIndex(Integer.parseInt(value));
            } else if (mode.equalsIgnoreCase("visibletext")) {
                select.selectByVisibleText(value);
            } else {
                Assert.fail("Not a valid mode to select from a drop down.");
            }

            retryCount = 0;

        } catch (Exception exe) {

            if (retryCount > 3) {
                throw exe;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            retryCount++;
            selectFromDropDown(element, value, mode);

        }
    }

    public static void selectServiceTypeDropDown(String prodName,
                                                 String serviceType) {
        // This method references non-existent RetailerNewInboundShipmentPageObject
        // Use selectAdvancedDropDown instead
        throw new UnsupportedOperationException("Use selectAdvancedDropDown method instead");
    }

    public static void selectAdvancedDropDown(WebElement dropDown,
                                              String dropDownItemName) {
        WebDriver driver = Driver.getInstance();
        WebDriverWait wd = new WebDriverWait(driver, Duration.ofSeconds(20));
        wd.until(ExpectedConditions.elementToBeClickable(dropDown));
        dropDown.click();
        // Generic XPath for advanced dropdown items
        driver.findElement(By.xpath("//li[contains(@class,'select2-results__option') and contains(text(),'" + dropDownItemName + "')]")).click();
    }

    public static void selectFromAdvancedDropDown(String prodName,
                                                  String serviceType) {
        // This method references non-existent RetailerNewInboundShipmentPageObject
        // Use selectAdvancedDropDown instead
        throw new UnsupportedOperationException("Use selectAdvancedDropDown method instead");
    }

    public static List<String> getAllOptionsInAdvancedDropdown(WebElement dropDown) {

        WebDriver driver = Driver.getInstance();
        WebDriverWait wd = new WebDriverWait(driver, Duration.ofSeconds(20));
        wd.until(ExpectedConditions.elementToBeClickable(dropDown));
        dropDown.click();
        List<WebElement> elements = driver.findElements(By.xpath("//li[contains(@class,'select2-results__option')]"));
        List<String> allOptions = new ArrayList<String>();
        for (WebElement element : elements) {
            allOptions.add(element.getText());
        }
        dropDown.click();
        return allOptions;
    }

    public static void selectFromAutosuggestion(WebElement dropdown, String Option) throws InterruptedException {
        dropdown.click();
        Thread.sleep(1000);
        // Generic XPath for autosuggestion selection
        driver.findElement(By.xpath("//li[contains(@class,'select2-results__option') and contains(text(),'" + Option + "')]")).click();
    }


    public static boolean isElementDisplayed(WebElement element) {
        boolean displayed = false;
        try {
            if (element.isDisplayed()) {
                displayed = true;
            }
        } catch (NoSuchElementException exe) {
            displayed = false;
        }
        return displayed;
    }

    public static void verifyElementDisplayed(WebElement element, String message) {
        assertTrue(element.isDisplayed(), message);
    }


    public static WebElement WaitForElement(WebElement element, int seconds) {

        long start = System.currentTimeMillis();

        while (true) {

            try {
                element.isDisplayed();
                return element;
            } catch (WebDriverException e) {

                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                }

                continue;

            } finally {

                long end = System.currentTimeMillis();
                if ((end - start) / 1000 > seconds) {
                    throw new NoSuchElementException(
                            "Timeout exceeded and element couldn't be found");
                }
            }
        }
    }

    public static void click(WebElement element) {

        try {
            element.click();
        } catch (WebDriverException e) {

            if (e instanceof NoSuchElementException) {
                WaitForElement(element, 60);
                element.click();
                return;
            } else if (e instanceof StaleElementReferenceException) {
                Driver.getInstance().navigate().refresh();
                element.click();
                return;
            }

            // Some UIs keep menu items present but not natively interactable (hover menus, overlays, etc.)
            // Fall back to JS click in these cases.
            String msg = e.getMessage();
            if (msg != null && (msg.startsWith("unknown error: Element")
                    || msg.toLowerCase().contains("element not interactable")
                    || msg.toLowerCase().contains("not interactable"))) {
                WebDriver execDriver = Driver.getInstance();
                if (execDriver instanceof com.orangeHrm.utils.WebDriverDispatcher) {
                    execDriver = ((com.orangeHrm.utils.WebDriverDispatcher) execDriver).getUnderlyingDriver();
                }
                WebElement target = element;
                try {
                    By by = getElementByInDefaultElementLocator(element);
                    if (by != null) {
                        target = execDriver.findElement(by);
                    }
                } catch (Exception ignored) {
                    // ignore and use original element
                }
                ((JavascriptExecutor) execDriver).executeScript("arguments[0].click();", target);
                return;
            }

            throw e;
        }
    }

    public static void waitForElementToBeDisplayed(WebDriver driver,
                                                   WebElement element, int timeOutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOutInSeconds));
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    public static void waitForListOfElementsToBeDisplayed(WebDriver driver, List<WebElement> element, int timeOutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOutInSeconds));
        ;
        wait.until(ExpectedConditions.visibilityOfAllElements(element));
    }

    public static void waitForElementToBeClickable(WebDriver driver,
                                                   WebElement element, int timeOutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOutInSeconds));
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    public static void waitForElementToBePresent(WebDriver driver, By element, int timeOutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOutInSeconds));
        wait.until(ExpectedConditions.presenceOfElementLocated(element));
    }

    public static void scrollToElement(WebDriver driver, WebElement element) {
        WebDriver actualDriver = driver;
        if (driver instanceof com.orangeHrm.utils.WebDriverDispatcher) {
            actualDriver = ((com.orangeHrm.utils.WebDriverDispatcher) driver).getUnderlyingDriver();
        }

        WebElement target = element;
        try {
            By by = getElementByInDefaultElementLocator(element);
            if (by != null) {
                target = actualDriver.findElement(by);
            }
        } catch (Exception e) {
            // ignore and use original element
        }

        Actions actions = new Actions(actualDriver);
        actions.moveToElement(target).perform();
    }

    /**
     * this function returns true if the element is enabled else false
     *
     * @param element
     * @return boolean
     */
    public static boolean isElementEnabled(WebElement element) {
        boolean enabled = false;
        try {
            if (element.isDisplayed()) {
                if (element.isEnabled())
                    enabled = true;
            }
        } catch (NoSuchElementException exe) {

            Assert.fail(exe.getMessage() + " exception occured.");
        }
        return enabled;
    }

    public static void switchingToParentWindow(WebDriver driver) {

        Set<String> childWindow = driver.getWindowHandles();
        Iterator<String> itrs = childWindow.iterator();
        String pWindows = itrs.next();
        String cWindows = itrs.next();
        driver.switchTo().window(cWindows);

        driver.switchTo().window(pWindows);

    }


    public static By getElementByInDefaultElementLocator(WebElement ele) {
        By by = null;
        String eleStr = ele.toString();
        System.out.println(eleStr);
        String byString = ele.toString().split("By.", 2)[1];
        String[] byDetail = byString.split(": ", 2);
        String locator = byDetail[1].substring(0, byDetail[1].length() - 1);
        switch (byDetail[0].trim().toLowerCase()) {
            case "id":
                by = By.id(locator);
                break;
            case "name":
                by = By.name(locator);
                break;
            case "class name":
            case "classname":
                by = By.className(locator);
                break;
            case "linktext":
            case "link text":
                by = By.linkText(locator);
                break;
            case "partiallinktext":
            case "partial link text":
                by = By.partialLinkText(locator);
                break;
            case "tagname":
            case "tag name":
                by = By.tagName(locator);
                break;
            case "xpath":
                by = By.xpath(locator);
                break;
            case "cssselector":
            case "css selector":
                by = By.cssSelector(locator);
                break;
            default:
                System.out.println("None of the locator type matched. Locator type found is " + byDetail[0] + " and locator found is " + locator);
        }

        return by;
    }
	
	/*public static boolean verifyElement(WebElement element) {
		String eleStr = element.toString();
		if(eleStr.contains("By")){
			By locator = getElementByInDefaultElementLocator(element);
			return verifyElement(locator);
		}else{
			WebDriverDispatcher wpatch = (WebDriverDispatcher)element;
			return wpatch.
		}
		
	}
	public static boolean verifyElement(WebElement element, int maxWaitTime) {
		By locator = getElementByInDefaultElementLocator(element);
		return verifyElement(locator, maxWaitTime);
	}*/

    public static boolean verifyElement(By locator) {
        boolean isFound = true;
        WebDriverDispatcher wpatch = (WebDriverDispatcher) Driver.getInstance();
        try {
            isFound = wpatch.verifyElement(locator, 20);
        } catch (Exception e) {
            isFound = false;
        }
        return isFound;
    }

    public static boolean verifyElement(By locator, int maxWaitTime) {
        boolean isFound = true;
        WebDriverDispatcher wpatch = (WebDriverDispatcher) Driver.getInstance();
        try {
            isFound = wpatch.verifyElement(locator, maxWaitTime);
        } catch (Exception e) {
            isFound = false;
        }
        return isFound;
    }


    public static WebElement locateRow(String description) {

        SeleniumTestHelper.waitForElementToBeDisplayed(driver, Driver.getInstance().findElement(By.partialLinkText(description)), 60);
        return Driver.getInstance().findElement(By.partialLinkText(description));
    }

    public static WebElement dropDownPartialItem(String partialItemName) {
        return Driver.getInstance().findElement(By.xpath("//li[contains(@class,'select2-results__option') and contains(text(),'" + partialItemName + "')]"));
    }

    public static WebElement dropDownItem(String itemName) {
        return Driver.getInstance().findElement(By.xpath("//li[contains(@class,'select2-results__option') and text()='" + itemName + "']"));
    }

    public static void uploadFile(String filePath) {
        try {
            // Use system default behavior to upload file
            Runtime.getRuntime().exec(new String[]{filePath});
        } catch (IOException e) {
            throw new RuntimeException("File operation failed: " + e.getMessage());
        }

    }

    public static WebDriver switchToOtherWindow(WebDriver driver) {
        String currentHandle = driver.getWindowHandle();
        Set<String> handles = driver.getWindowHandles();
        for (String handle : handles) {
            if (!handle.equals(currentHandle)) {
                driver.switchTo().window(handle);
            }
        }
        return driver;
    }

    public static WebDriver switchToOtherWindowAndCloseCurrentWindow(
            WebDriver driver) {
        String currentHandle = driver.getWindowHandle();
        Set<String> handles = driver.getWindowHandles();
        for (String handle : handles) {
            if (!handle.equals(currentHandle)) {
                driver.close();
                driver.switchTo().window(handle);
            }
        }
        return driver;
    }

    public static WebDriver switchToOtherWindowAndCloseItAndComeBack(
            WebDriver driver) {
        String currentHandle = driver.getWindowHandle();
        Set<String> handles = driver.getWindowHandles();
        for (String handle : handles) {
            if (!handle.equals(currentHandle)) {
                driver.switchTo().window(handle);
                driver.close();
                break;
            }
        }
        driver.switchTo().window(currentHandle);
        return driver;
    }

    public static void switchingTochildtWindow(WebDriver driver) {

        Set<String> childWindow = driver.getWindowHandles();
        Iterator<String> itrs = childWindow.iterator();
        String cWindows = itrs.next();
        if (itrs.hasNext()) {
            cWindows = itrs.next();
        }
        driver.switchTo().window(cWindows);

    }

    public static void waitForElementToAppear(By element, long minWaitTime,
                                              long maxWaitTime) {
        boolean isElementFound = false;
        try {
            Thread.sleep(minWaitTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long maxDeltaTime = maxWaitTime - minWaitTime;
        long startTime = System.currentTimeMillis();
        long currentTime = startTime;

        while ((maxDeltaTime > (currentTime - startTime)) && !isElementFound) {
            isElementFound = true;
            try {
                Driver.getInstance().findElement(element);
            } catch (Exception e) {
                try {
                    Thread.sleep(500);
                    Driver.getInstance().navigate().refresh();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                isElementFound = false;
            }
            currentTime = System.currentTimeMillis();
        }

    }

    public static void assertEquals(Object actual, Object expected, String message) {

        if (null != message) {
            System.out.println(message + "actual is - " + actual + "expected is - " + expected);
            Assert.assertEquals(actual, expected, message);
        } else {
            Assert.assertEquals(actual, expected);
        }
    }

    public static void assertNotEquals(Object actual, Object expected, String message) {

        if (null != message) {
            System.out.println(message + " actual is - " + actual + " expected is - " + expected);
            Assert.assertNotEquals(actual, expected, message);
        } else {
            Assert.assertNotEquals(actual, expected);
        }
    }

    public static void assertEquals(Object actual, Object expected) {
        assertEquals(actual, expected, null);
    }


    public static void assertNotEquals(Object actual, Object expected) {
        assertNotEquals(actual, expected, null);
    }

    public static void assertTrue(boolean bool, String message) {
        assertEquals(bool, true, message);
    }

    public static void assertTrue(boolean bool) {
        assertTrue(bool, null);
    }

    public static void assertFalse(boolean bool, String message) {
        assertEquals(bool, false, message);
    }

    public static void assertFalse(boolean bool) {
        assertFalse(bool, null);
    }

    public static void fail(String message) {
        assertEquals(true, false, message);
    }

    public static void assertNotNull(Object obj) {
        assertNotNull(obj, null);
    }

    public static void assertNotNull(Object obj, String message) {
        boolean truthness = true;
        if (null == obj) {
            truthness = false;
        }
        assertEquals(truthness, true, message);
    }

    public static void fail() {
        fail(null);
    }

    public static String getScreenshot() throws IOException {
        String dateName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
        WebDriver driver = Driver.getInstance();
        TakesScreenshot ts = (TakesScreenshot) driver;
        File source = ts.getScreenshotAs(OutputType.FILE);
        String destination = System.getProperty("user.dir") + "/PassedScreenshots/" + dateName + ".png";
        File finalDestination = new File(destination);
        try {
            FileUtils.copyFile(source, finalDestination);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileInputStream is = new FileInputStream(source);
        byte[] imageBytes = IOUtils.toByteArray(is);
        String Base64 = java.util.Base64.getEncoder().encodeToString(imageBytes);
        //	return destination;
        return "data:image/png;base64," + Base64;

    }


    public static int generateRandomInt(int min, int max) {
        Random foo = new Random();
        return foo.nextInt((max + 1) - min) + min;
    }

    public static void jsClick(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) Driver.getInstance();
        js.executeScript("arguments[0].click();", element);
    }

    /**
     *
     * @param element
     * @author nurmamet.hemrayev
     */
    public static void actionsClick(WebElement element) {
        WebDriver driver = Driver.getInstance();
        WebDriver actualDriver = driver;
        if (driver instanceof com.orangeHrm.utils.WebDriverDispatcher) {
            actualDriver = ((com.orangeHrm.utils.WebDriverDispatcher) driver).getUnderlyingDriver();
        }

        WebElement target = element;
        try {
            By by = getElementByInDefaultElementLocator(element);
            if (by != null) {
                target = actualDriver.findElement(by);
            }
        } catch (Exception e) {
            // ignore and use original element
        }

        try {
            Actions actions = new Actions(actualDriver);
            actions.moveToElement(target).perform();
            actions.click().perform();
        } catch (Exception e) {
            // fallback: attempt JS click then direct click
            try {
                ((JavascriptExecutor) actualDriver).executeScript(
                        "var evObj = document.createEvent('MouseEvents'); evObj.initMouseEvent('click',true,true,window,0,0,0,0,0,false,false,false,false,0,null); arguments[0].dispatchEvent(evObj);",
                        target);
                return;
            } catch (Exception ex) {
                try {
                    target.click();
                    return;
                } catch (Exception ex2) {
                    throw new RuntimeException("Failed to perform actions click", e);
                }
            }
        }
    }

    /**
     * This method is used for selecting date from calendar modal dialog
     *
     * @param days   - required days from current date
     * @param months - required months from current date
     * @param years  - required years from current date
     * @throws InterruptedException
     * @author nurmamet.hemrayev
     */
    public static void setCalendarFromCurrentDate(int days, int months, int years) throws InterruptedException {
        Calendar cal = Calendar.getInstance();

        if (0 != days) {
            Thread.sleep(1000);
            cal.add(Calendar.DATE, days);
        }
        if (0 != months) {
            Thread.sleep(1000);
            cal.add(Calendar.MONTH, months);
        }
        if (0 != years) {
            Thread.sleep(1000);
            cal.add(Calendar.YEAR, years);
        }

        Date reqDate = cal.getTime();
        SimpleDateFormat fullDateFormat = new SimpleDateFormat("yyyy-MMMM-dd");
        SimpleDateFormat shortMonthFormat = new SimpleDateFormat("MMM");

        String reqDateStr = fullDateFormat.format(reqDate);
        String shortMonth = shortMonthFormat.format(reqDate);
        String reqYear = reqDateStr.split("-")[0];
        String reqMonth = reqDateStr.split("-")[1];
        String reqDay = reqDateStr.split("-")[2];
        reqDay = reqDay.replaceFirst("^0+(?!$)", "");
        WebDriver driver = Driver.getInstance();
        By dispYearMonthBy = By.xpath("//div[@class='datepicker-days']//th[@class='datepicker-switch']");
        By currentYearBy = By.xpath("//div[@class='datepicker-months']//th[@class='datepicker-switch']");
        By yearRangeBy = By.xpath("//div[@class='datepicker-years']//th[@class='datepicker-switch']");
        String dispYearMonth = driver.findElement(dispYearMonthBy).getText();
        String dispMonth = dispYearMonth.split(" ")[0];
        String dispYear = dispYearMonth.split(" ")[1];
        By previousBtnBy = By.xpath("//th[@class='prev'][not(./ancestor::*[contains(@style,'display: none')])]");
        if (!dispYear.equals(reqYear)) {
            driver.findElement(dispYearMonthBy).click();
            driver.findElement(currentYearBy).click();
            String currentyearRange = driver.findElement(yearRangeBy).getText();
            int startYear = Integer.parseInt(currentyearRange.split("-")[0]);
            int endYear = Integer.parseInt(currentyearRange.split("-")[1]);
            int reqYearInt = Integer.parseInt(reqYear);
//			while(!(reqYearInt>=startYear && reqYearInt<=endYear)){
//				driver.findElement(nextBtnBy).click();
//				currentyearRange = driver.findElement(yearRangeBy).getText();
//				startYear = Integer.parseInt(currentyearRange.split("-")[0]);
//				endYear = Integer.parseInt(currentyearRange.split("-")[1]);

//			}
            while (!(reqYearInt >= startYear && reqYearInt <= endYear)) {
                driver.findElement(previousBtnBy).click();
                currentyearRange = driver.findElement(yearRangeBy).getText();
                startYear = Integer.parseInt(currentyearRange.split("-")[0]);
                endYear = Integer.parseInt(currentyearRange.split("-")[1]);
            }
            driver.findElement(By.xpath("//span[contains(@class,'year') and text()='" + reqYear + "']")).click();
            ;
            driver.findElement(By.xpath("//span[contains(@class,'month') and text()='" + shortMonth + "']")).click();
        }
        dispYearMonth = driver.findElement(dispYearMonthBy).getText();
        dispMonth = dispYearMonth.split(" ")[0];

        if (!(dispMonth.equals(reqMonth))) {
            driver.findElement(dispYearMonthBy).click();
            driver.findElement(By.xpath("//span[contains(@class,'month') and text()='" + shortMonth + "']")).click();
        }

        driver.findElement(
                By.xpath("//div[@class='datepicker-days']//tbody//td[@class='day' and text()='"
                        + reqDay + "']")).click();
    }


    public static String getAbsolutePath(String fileName) {

        String relativePath = Configurations.getProperty("attachmentsPath") + Configurations.getProperty(fileName);
        Path path = Paths.get(relativePath);
        Path absolutePath = path.toAbsolutePath();

        return absolutePath.toString();
    }

    public static File getLastDownloadedFile() {
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        File requiredFile = null;
        String requiredFileName = "";
        long lastModified = 0;
        String downloadDir = Configurations.getDownloadLocation();
        do {
            File f = new File(downloadDir);
            if (f.isDirectory())
                for (File currFile : f.listFiles()) {
                    if (!currFile.isDirectory()) {
                        if (lastModified < currFile.lastModified()) {
                            requiredFile = currFile;
                            lastModified = currFile.lastModified();
                        }
                    }
                }
            requiredFileName = requiredFile.getName();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //lastModified = 0;
        } while (requiredFileName.contains(".crdownload") || requiredFileName.contains(".download"));
        return requiredFile;
    }

    public static File getLastDownloadedFile(String matchingFileName) {
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        File requiredFile = null;
        String requiredFileName = "";
        long lastModified = 0;
        String downloadDir = Configurations.getDownloadLocation();
        do {
            File f = new File(downloadDir);
            if (f.isDirectory())
                for (File currFile : f.listFiles()) {
                    if (!currFile.isDirectory()) {
                        if (currFile.getName().contains(matchingFileName) && lastModified < currFile.lastModified()) {
                            requiredFile = currFile;
                            lastModified = currFile.lastModified();
                        }
                    }
                }
            if (requiredFile != null) {
                requiredFileName = requiredFile.getName();
            } else {
                requiredFileName = "";
            }
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //lastModified = 0;
        } while (requiredFileName.contains(".crdownload") || requiredFileName.contains(".download"));
        return requiredFile;
    }

    public static void waitForTextToBePresentInElement(WebDriver driver,
                                                       WebElement element, int timeOutInSeconds, String text) {
        @SuppressWarnings("null")
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOutInSeconds));
        wait.until(ExpectedConditions.textToBePresentInElement(element, text));
    }


    public static void switchingTochildtWindowAndCloseIt(WebDriver driver) {

        Set<String> childWindow = driver.getWindowHandles();
        Iterator<String> itrs = childWindow.iterator();
        String cWindows = itrs.next();
        if (itrs.hasNext()) {
            cWindows = itrs.next();
        }
        @SuppressWarnings("null")
        String windowToClose = cWindows;
        driver.switchTo().window(windowToClose);
        driver.switchTo().window(windowToClose).close();

    }

    /**
     * @param days add desired days in current date
     * @author Geetha
     */
    public static String addDaysToCurrentDate(int days) {

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


        Date currentDate = new Date();
        System.out.println(dateFormat.format(currentDate));

        // convert date to calendar
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);

        c.add(Calendar.DATE, days);

        // convert calendar to date
        String currentDatePlusOne = dateFormat.format(c.getTime());
        return currentDatePlusOne;

    }

    /**
     * @author Geetha
     */

    public static String generateRandomNumericalString() {
        String saltChars = "1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) {
            int index = (int) (rnd.nextFloat() * saltChars.length());
            salt.append(saltChars.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }

    public static String generateRandomNumericalStringWithSpecialCharacters() {
        String SALTCHARS = "1234567890/-";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) {
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }

    public static void reportLog(String message) {
        Reporter.log("<I style=\"color:blue\">" + message + "</I>");
        System.out.println(message);
    }

    public static String generateRandomNumericalStringWithLetters(int stringLength) {
        String SALTCHARS = "1234567890abcdefg";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < stringLength) {
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }

    public static String generateRandomNumericalString(int stringLength) {
        String SALTCHARS = "1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < stringLength) {
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }

    //sushant
    // this method is used when we are getting stale element exception
    public static void waitForElementToBerefreshed(WebDriver driver, WebElement element, int timeOutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeOutInSeconds));
        wait.until(ExpectedConditions.refreshed(ExpectedConditions.stalenessOf(element)));
    }

    public static String generateRandomNumberString(int stringLength) {
        String SALTCHARS = "123456789";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < stringLength) {
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }


    public static String getFailedScreenshot() {
        return captureScreenshot("failed");
    }

    public static String getPassedScreenshot() {
        return captureScreenshot("passed");
    }

    private static String captureScreenshot(String prefix) {
        try {
            WebDriver driver = Driver.getInstance();
            if (!(driver instanceof TakesScreenshot)) {
                return "";
            }

            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String dirPath = System.getProperty("user.dir") + File.separator + "TestResult" + File.separator + "screenshots";
            File dir = new File(dirPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String fileName = prefix + "_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".png";
            File dest = new File(dir, fileName);
            Files.copy(src.toPath(), dest.toPath());
            return dest.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    /**
     * Moves the mouse over an element identified by locator strategy and locator string.
     * Supports: xpath, css, id, name, class, link.
     *
     * @param strategy the locator strategy (xpath, css, id, name, class, link)
     * @param locator  the locator value
     * @throws IllegalArgumentException if strategy is invalid
     */
    public static void mouseHover(String strategy, String locator) {
        try {
            LocatorStrategy locatorStrategy = LocatorStrategy.fromString(strategy);
            WebElement element = findElement(locatorStrategy, locator);
            mouseHover(element);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid locator strategy: " + strategy, e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to perform mouse hover with strategy '" + strategy +
                    "' and locator '" + locator + "': " + e.getMessage(), e);
        }
    }

    /**
     * Moves the mouse over a WebElement.
     * Attempts to use Actions API first, with JavaScript fallback if needed.
     *
     * @param element the WebElement to hover over
     */
    public static void mouseHover(WebElement element) {
        if (element == null) {
            throw new IllegalArgumentException("Element cannot be null");
        }

        WebDriver driver = getDriver();
        WebDriver actualDriver = driver;

        if (driver instanceof WebDriverDispatcher) {
            actualDriver = ((WebDriverDispatcher) driver).getUnderlyingDriver();
        }

        try {
            // Try using Actions API
            new Actions(actualDriver).moveToElement(element).perform();
        } catch (Exception actionsFailed) {
            // Fallback to JavaScript mouseover event
            try {
                executeMouseoverScript(actualDriver, element);
            } catch (Exception jsException) {
                throw new RuntimeException("Failed to perform mouse hover - both Actions and JavaScript methods failed: " +
                        actionsFailed.getMessage(), actionsFailed);
            }
        }
    }

    /**
     * Helper method to find an element using a locator strategy
     *
     * @param strategy the LocatorStrategy enum value
     * @param locator  the locator value
     * @return the found WebElement
     */
    private static WebElement findElement(LocatorStrategy strategy, String locator) {
        WebDriver driver = getDriver();
        WebDriver actualDriver = driver;

        if (driver instanceof WebDriverDispatcher) {
            actualDriver = ((WebDriverDispatcher) driver).getUnderlyingDriver();
        }

        return actualDriver.findElement(strategy.getBy(locator));
    }

    /**
     * Helper method to execute mouseover event using JavaScript
     *
     * @param driver  the WebDriver instance
     * @param element the WebElement to trigger mouseover on
     */
    private static void executeMouseoverScript(WebDriver driver, WebElement element) {
        ((JavascriptExecutor) driver).executeScript(
                "var evObj = document.createEvent('MouseEvents');" +
                        "evObj.initMouseEvent('mouseover',true,true,window,0,0,0,0,0,false,false,false,false,0,null);" +
                        "arguments[0].dispatchEvent(evObj);",
                element);
    }

/*    //Switch to Frame..!!

    public static void switchToFrame(String identify, String locator) {
        WebDriver driver = getDriver();
        if (identify.equalsIgnoreCase("name")) {
            driver.switchTo().frame(locator);
        } else if (identify.equalsIgnoreCase("index")) {
            int index = Integer.parseInt(locator);
            driver.switchTo().frame(index);
        } else if (identify.equalsIgnoreCase("id")) {
            WebElement iframeElement = driver.findElement(By.id(locator));
            driver.switchTo().frame(iframeElement);
        } else if (identify.equalsIgnoreCase("xpath")) {
            WebElement iframeElement = driver.findElement(By.xpath(locator));
            driver.switchTo().frame(iframeElement);
        } else if (identify.equalsIgnoreCase("css")) {
            WebElement iframeElement = driver.findElement(By.cssSelector(locator));
            driver.switchTo().frame(iframeElement);
        } else {
            System.out.println("Invalid frame locator: " + identify);
        }
    }*/

    /**
     * Marks the current thread as interrupted. Use this from catch blocks
     * handling InterruptedException to consistently restore the interrupt flag.
     */
    public static void markCurrentThreadInterrupted() {
        Thread.currentThread().interrupt();
    }


    /**
     * Switch to a frame using a flexible "identify + locator" pair.
     * Supported identifiers: "id", "name", "css", "xpath", "index".
     * <p>
     * Examples:
     * switchToFrame("id", "payment-iframe");
     * switchToFrame("name", "card-frame");
     * switchToFrame("css", "iframe[src*='checkout']");
     * switchToFrame("xpath", "//iframe[@title='Secure']");
     * switchToFrame("index", "0");
     */
    public static void switchToFrame(String identify, String locator) {
        switchToFrame(identify, locator, FRAME_WAIT_TIMEOUT);
    }

    public static void switchToFrame(String identify, String locator, Duration timeout) {
        WebDriver driver = getDriver();
        switchToFrame(driver, identify, locator, timeout);
    }

    /**
     * Variant that accepts driver explicitly (optional use).
     */
    public static void switchToFrame(WebDriver driver, String identify, String locator, Duration timeout) {
        Objects.requireNonNull(driver, "driver is required");
        Objects.requireNonNull(identify, "identify is required");
        Objects.requireNonNull(locator, "locator is required");

        String key = identify.trim().toLowerCase();
        WebDriverWait wait = new WebDriverWait(driver, timeout);

        try {
            switch (key) {
                case "id": {
                    By by = By.id(locator);
                    wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(by));
                    break;
                }
                case "name": {
                    // nameOrId is supported by driver.switchTo().frame(String)
                    wait.until(d -> {
                        try {
                            d.switchTo().frame(locator);
                            return true;
                        } catch (NoSuchFrameException e) {
                            return false;
                        }
                    });
                    break;
                }
                case "css": {
                    By by = By.cssSelector(locator);
                    wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(by));
                    break;
                }
                case "xpath": {
                    By by = By.xpath(locator);
                    wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(by));
                    break;
                }
                case "index": {
                    int index = parseFrameIndex(locator);
                    wait.until(d -> {
                        try {
                            d.switchTo().frame(index);
                            return true;
                        } catch (NoSuchFrameException e) {
                            return false;
                        }
                    });
                    break;
                }
                default:
                    throw new IllegalArgumentException(
                            "Unsupported frame identifier: " + identify + " (use id|name|css|xpath|index)");
            }
        } catch (org.openqa.selenium.TimeoutException e) {
            throw new org.openqa.selenium.TimeoutException(
                    String.format("Timed out after %ds waiting for frame by %s='%s'",
                            timeout.getSeconds(), identify, locator), e);
        }
    }

    /**
     * Switch using a By locator (css/xpath/etc.).
     */
    public static void switchToFrame(By by) {
        switchToFrame(by, FRAME_WAIT_TIMEOUT);
    }

    public static void switchToFrame(By by, Duration timeout) {
        WebDriver driver = getDriver();
        switchToFrame(driver, by, timeout);
    }

    public static void switchToFrame(WebDriver driver, By by, Duration timeout) {
        Objects.requireNonNull(driver, "driver is required");
        Objects.requireNonNull(by, "by is required");
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        try {
            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(by));
        } catch (org.openqa.selenium.TimeoutException e) {
            throw new org.openqa.selenium.TimeoutException(
                    String.format("Timed out after %ds waiting for frame located by: %s",
                            timeout.getSeconds(), by), e);
        }
    }

    /**
     * Switch using a WebElement that references the iframe element.
     */
    public static void switchToFrame(WebElement frameElement) {
        switchToFrame(frameElement, FRAME_WAIT_TIMEOUT);
    }

    public static void switchToFrame(WebElement frameElement, Duration timeout) {
        WebDriver driver = getDriver();
        switchToFrame(driver, frameElement, timeout);
    }

    public static void switchToFrame(WebDriver driver, WebElement frameElement, Duration timeout) {
        Objects.requireNonNull(driver, "driver is required");
        Objects.requireNonNull(frameElement, "frameElement is required");
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        try {
            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(frameElement));
        } catch (org.openqa.selenium.TimeoutException e) {
            throw new org.openqa.selenium.TimeoutException(
                    String.format("Timed out after %ds waiting for provided frame element to be available",
                            timeout.getSeconds()), e);
        }
    }

    /**
     * Switch by zero-based index.
     */
    public static void switchToFrame(int index) {
        switchToFrame(index, FRAME_WAIT_TIMEOUT);
    }

    public static void switchToFrame(int index, Duration timeout) {
        WebDriver driver = getDriver();
        switchToFrame(driver, index, timeout);
    }

    public static void switchToFrame(WebDriver driver, int index, Duration timeout) {
        Objects.requireNonNull(driver, "driver is required");
        if (index < 0) throw new IllegalArgumentException("Frame index must be >= 0");
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        try {
            wait.until(d -> {
                try {
                    d.switchTo().frame(index);
                    return true;
                } catch (NoSuchFrameException e) {
                    return false;
                }
            });
        } catch (org.openqa.selenium.TimeoutException e) {
            throw new org.openqa.selenium.TimeoutException(
                    String.format("Timed out after %ds waiting for frame index: %d",
                            timeout.getSeconds(), index), e);
        }
    }

    /**
     * Return to the top-level document.
     */
    public static void switchToDefaultContent() {
        getDriver().switchTo().defaultContent();
    }

    /**
     * Switch to the immediate parent frame.
     */
    public static void switchToParentFrame() {
        getDriver().switchTo().parentFrame();
    }

// ---------------- Helpers ----------------

    private static int parseFrameIndex(String s) {
        try {
            return Integer.parseInt(s.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid frame index: " + s, e);
        }
    }

}
