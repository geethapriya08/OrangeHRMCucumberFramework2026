package com.orangeHrm.pages;

import com.orangeHrm.utils.Driver;
import com.orangeHrm.utils.SeleniumTestHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class SpecificHolidayPageObjects {
    private WebDriver driver;

    // leave Module Navigation - using correct selectors
    @FindBy(xpath = "//a[contains(@href, 'top=leave')]")
    private WebElement leaveDropdown;

    @FindBy(xpath = "//span[normalize-space()='Define Days Off']")
    private WebElement defineDaysOffDropdown;

    @FindBy(xpath = "//span[normalize-space()='Specific Holidays']")
    private WebElement specificHolidayDropdown;

    @FindBy(xpath = "//input[@id='btnAdd']")
    public WebElement specificHolidayAddButton;

    @FindBy(id = "txtDescription")
    private WebElement nameOfHolidayInputField;

    //Name of holiday field by Name
    @FindBy(xpath = "//input[@name='txtDescription']")
    public WebElement specificHolidayByName;

    @FindBy(id = "txtDate")
    private WebElement date;

    //Name of holiday field by Name
    @FindBy(xpath = "//input[@name='txtDate']")
    public WebElement dateByName;

    @FindBy(id = "chkRecurring")
    private WebElement repeatAnuallyCheckbox;

    @FindBy(id = "sltLeaveLength")
    private WebElement dayDropdown;

    @FindBy(id = "saveBtn")
    private WebElement specificHolidaysaveButton;

    //Save button by value
    @FindBy(xpath = "//input[@value='Save']")
    public WebElement saveButtonByValue;

    public SpecificHolidayPageObjects(WebDriver driver) {
        this.driver = driver;
        // Ensure PageFactory elements are initialized with the underlying driver
        WebDriver actualDriver = driver;
        if (driver instanceof com.orangeHrm.utils.WebDriverDispatcher) {
            actualDriver = ((com.orangeHrm.utils.WebDriverDispatcher) driver).getUnderlyingDriver();
        }
        PageFactory.initElements(actualDriver, this);
    }
    /**
     * Navigate to Admin module by hovering on the Admin dropdown (id="admin")
     */
    public void navigateToSpecificHolidayModule() throws InterruptedException {
        WebDriver driver = Driver.getInstance();
        Thread.sleep(1500);
        System.out.println("Hovering over Leave dropdown...");
        try {
            // Use SeleniumTestHelper.mouseHover to perform a robust hover
            SeleniumTestHelper.mouseHover("id", "leave");
            System.out.println("Successfully hovered over Leave dropdown (via SeleniumTestHelper)");
            Thread.sleep(1500);
        } catch (Exception e) {
            System.out.println("Failed to hover over Leave dropdown: " + e.getMessage());
        }
        System.out.println("Navigating to Define days off -> Specific Holiday...");
        try {
            WebDriver actualDriver = driver;
            if (driver instanceof com.orangeHrm.utils.WebDriverDispatcher) {
                actualDriver = ((com.orangeHrm.utils.WebDriverDispatcher) driver).getUnderlyingDriver();
            }

            // Hover over Define days off
            System.out.println("Hovering over Define days off ...");
            Actions actions = new Actions(actualDriver);
            actions.moveToElement(defineDaysOffDropdown).perform();
            System.out.println("Successfully hovered over Define days off ");
            Thread.sleep(1000);

            // Click on General
            System.out.println("Clicking on specific Holiday ...");
           actions.click(specificHolidayDropdown).perform();
            System.out.println("Successfully clicked specific Holiday");
            Thread.sleep(2000);
        } catch (Exception e) {
            System.out.println("Failed in navigation: " + e.getMessage());
        }
    }

    private void switchToHolidayFrame() {
        WebDriver driver = Driver.getInstance();
        try {
            // Try to find and switch to iframe with id 'rightMenu'
            driver.switchTo().frame("rightMenu");
            System.out.println("Switched to rightMenu iframe successfully");
            dumpIframeInputFields();
        } catch (Exception e) {
            try {
                // Try alternative iframe locators
                List<WebElement> frames = driver.findElements(By.tagName("iframe"));
                System.out.println("Found " + frames.size() + " iframes on the page");
                for (int i = 0; i < frames.size(); i++) {
                    try {
                        driver.switchTo().defaultContent(); // Reset to main content
                        driver.switchTo().frame(i);
                        System.out.println("Successfully switched to iframe index: " + i);
                        dumpIframeInputFields();
                        return;
                    } catch (Exception ex) {
                        // Try next frame
                    }
                }
            } catch (Exception ex) {
                System.out.println("Could not switch to any iframe: " + ex.getMessage());
            }
        }
    }

    /**
     * Debug method to dump all input fields in current iframe context
     */
    private void dumpIframeInputFields() {
        try {
            WebDriver driver = Driver.getInstance();
            List<WebElement> inputs = driver.findElements(By.tagName("input"));
            System.out.println("=== INPUT FIELDS IN CURRENT FRAME ===");
            System.out.println("Total input fields found: " + inputs.size());
            for (int i = 0; i < inputs.size(); i++) {
                try {
                    String id = inputs.get(i).getAttribute("id");
                    String name = inputs.get(i).getAttribute("name");
                    String type = inputs.get(i).getAttribute("type");
                    String placeholder = inputs.get(i).getAttribute("placeholder");
                    System.out.println("Input " + i + ": id='" + id + "', name='" + name + "', type='" + type + "', placeholder='" + placeholder + "'");
                } catch (Exception e) {
                    System.out.println("Input " + i + ": Error reading attributes");
                }
            }
            System.out.println("====================================");

            // Also dump buttons
            List<WebElement> buttons = driver.findElements(By.tagName("button"));
            System.out.println("=== BUTTONS IN CURRENT FRAME ===");
            System.out.println("Total buttons found: " + buttons.size());
            for (int i = 0; i < buttons.size(); i++) {
                try {
                    String id = buttons.get(i).getAttribute("id");
                    String name = buttons.get(i).getAttribute("name");
                    String text = buttons.get(i).getText();
                    System.out.println("Button " + i + ": id='" + id + "', name='" + name + "', text='" + text + "'");
                } catch (Exception e) {
                    System.out.println("Button " + i + ": Error reading attributes");
                }
            }
            System.out.println("====================================");

            // Also check for links with "Add" text
            List<WebElement> links = driver.findElements(By.tagName("a"));
            System.out.println("=== LINKS WITH 'ADD' IN CURRENT FRAME ===");
            for (int i = 0; i < links.size(); i++) {
                try {
                    String text = links.get(i).getText();
                    if (text.contains("Add") || text.contains("add")) {
                        String id = links.get(i).getAttribute("id");
                        String href = links.get(i).getAttribute("href");
                        System.out.println("Link " + i + ": id='" + id + "', text='" + text + "', href='" + href + "'");
                    }
                } catch (Exception e) {
                }
            }
            System.out.println("====================================");
        } catch (Exception e) {
            System.out.println("Error dumping input fields: " + e.getMessage());
        }
    }

    public void enterDefineDaysOfSpecificHoliday(String nameOfHoliday, String date, String dayDropdown) throws InterruptedException {
        Thread.sleep(1500);
        // Now switch to the rightMenu iframe to interact with the Employee form
        switchToHolidayFrame();
        Thread.sleep(1000);

        // Fill Company Name
        fillNameOfHolidayField(nameOfHoliday);
        // Fill Cal Date
        fillCalDateField(date);

        // Fill Country
        fillFullHalfDaydropdown(dayDropdown);

        //Click Save button
        specificDayClickSaveButton();
        Thread.sleep(1000);
    }

    private void fillNameOfHolidayField(String holidayName) throws InterruptedException {
        System.out.println("Attempting to fill holidayName field (txtDescription) with: " + holidayName);

        try {
            System.out.println("Trying holidayName (id='txtDescription')");
            SeleniumTestHelper.clear(nameOfHolidayInputField);
            SeleniumTestHelper.enterText(nameOfHolidayInputField, holidayName);
            System.out.println("Successfully filled companyName field");
            return;
        } catch (Exception e) {
            System.out.println("Failed with nameOfHolidayInputField: " + e.getMessage());
        }

        try {
            System.out.println("Trying nameOfHolidayByName (name='txtDescription')");
            SeleniumTestHelper.clear(specificHolidayByName);
            SeleniumTestHelper.enterText(specificHolidayByName, holidayName);
            System.out.println("Successfully filled holidayName field by name");
            return;
        } catch (Exception e) {
            System.out.println("Failed with holidayNameFieldByName: " + e.getMessage());
        }
    }

    private void  fillCalDateField(String calDate) throws InterruptedException {
        System.out.println("Attempting to fill calendar date field (txtDate) with: " + calDate);

        try {
            System.out.println("Trying calDate (id='txtDate')");
            SeleniumTestHelper.clear(date);
            SeleniumTestHelper.enterText(date, calDate);
            System.out.println("Successfully filled companyName field");
            return;
        } catch (Exception e) {
            System.out.println("Failed with nameOfHolidayInputField: " + e.getMessage());
        }

        try {
            System.out.println("Trying nameOfHolidayByName (name='txtDate')");
            SeleniumTestHelper.clear(dateByName);
            SeleniumTestHelper.enterText(dateByName, calDate);
            System.out.println("Successfully filled holidayName field by name");
            return;
        } catch (Exception e) {
            System.out.println("Failed with holidayNameFieldByName: " + e.getMessage());
        }
    }

    private void fillFullHalfDaydropdown(String country) {
        System.out.println("Attempting to click on day dropdown (sltLeaveLength) with: " + country);

        try {
            System.out.println("Trying to click on day dropdown (id='sltLeaveLength')");
            SeleniumTestHelper.selectFromDropDown(dayDropdown, country, "VALUE");
            System.out.println("Successfully filled day dropdown");

        } catch (Exception e) {
            System.out.println("Failed with xpath search for day dropdown: " + e.getMessage());

        }
    }
    /**
     * Click the Save button
     */
    private void specificDayClickSaveButton() throws InterruptedException {
        try {
            SeleniumTestHelper.click(specificHolidaysaveButton);
            return;
        } catch (Exception e) {
            // Try alternative
        }

        try {
            SeleniumTestHelper.click(saveButtonByValue);
            return;
        } catch (Exception e) {
            // Fallback: Try generic button locators
        }

        // Fallback: Try alternative button locators
        String[] saveXpaths = new String[]{ "//input[@id='saveBtn']", "//input[@value='Save']"};

        for (String xp : saveXpaths) {
            try {
                List<WebElement> els = driver.findElements(By.xpath(xp));
                if (els != null && !els.isEmpty()) {
                    SeleniumTestHelper.click(els.get(0));
                    Thread.sleep(500);
                    return;
                }
            } catch (Exception e) {
                // Continue to next
            }
        }
    }


}
