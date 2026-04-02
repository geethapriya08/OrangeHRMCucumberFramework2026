package com.orangeHrm.pages;

import com.orangeHrm.utils.Driver;
import com.orangeHrm.utils.SeleniumTestHelper;
import io.cucumber.java.eo.Se;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;
import java.util.Set;

public class LeavePageObjects {
    private WebDriver driver;

    // leave Module Navigation - using correct selectors
    @FindBy(xpath = "//a[contains(@href, 'top=leave')]")
    private WebElement leaveDropdown;

    @FindBy(xpath = "//span[normalize-space()='Leave Summary']")
    private WebElement leaveSummary;

    @FindBy(xpath = "//span[normalize-space()='Employee Leave Summary']")
    private WebElement employeeLeaveSummary;

    @FindBy(xpath = "//select[@name='year']")
    private WebElement fillYearDropdownXpath;

    @FindBy(xpath = "//select[@name='cmbId']")
    private WebElement fillEmployeeDropdownXpath;

    // 3 dots button 1
    @FindBy(xpath = "//input[@value='...']")
    private WebElement moreButton;

    // 3 dots button 2
    @FindBy(xpath = "//input[@onclick='returnEmpDetail();']")
    private WebElement moreButton2;

    // First Employee Record
    @FindBy(xpath = "//a[text()='0001']")
    private WebElement firstEmployee;

    //Select employee Leave Type
    @FindBy(xpath = "//select[@name='leaveTypeId']")
    private WebElement leaveTypeXpath;

    // View button xpath by id
    @FindBy(xpath = "//input[@id='btnView']")
    private WebElement viewButton;


    // View button xpath by class
    @FindBy(xpath = "//input[@class='viewbutton']")
    private WebElement viewButtonByClass;

    // Date Picker Elements
    @FindBy(className = "calendarBtn")
    private WebElement calendarButton;

    @FindBy(id = "txtDate")
    private WebElement dateInputField;

    @FindBy(id = "cal1Container")
    private WebElement calendarContainer;

    @FindBy(id = "cal1")
    private WebElement calendarTable;

    @FindBy(className = "calheader")
    private WebElement calendarHeader;

    @FindBy(className = "calnavright")
    private WebElement nextMonthButton;

    @FindBy(className = "calnavleft")
    private WebElement previousMonthButton;


    public LeavePageObjects(WebDriver driver) {
        this.driver = driver;
        // Ensure PageFactory elements are initialized with the underlying driver
        WebDriver actualDriver = driver;
        if (driver instanceof com.orangeHrm.utils.WebDriverDispatcher) {
            actualDriver = ((com.orangeHrm.utils.WebDriverDispatcher) driver).getUnderlyingDriver();
        }
        PageFactory.initElements(actualDriver, this);
    }

    public void navigateToLeaveModule() throws InterruptedException {
        WebDriver driver = Driver.getInstance();
        Thread.sleep(1500);
        System.out.println("Hovering over Leave dropdown...");
        try {
            // Use SeleniumTestHelper.mouseHover to perform a robust hover
            SeleniumTestHelper.mouseHover("id", "leave");
            System.out.println("Successfully hovered over leave dropdown (via SeleniumTestHelper)");
            Thread.sleep(1500);
        } catch (Exception e) {
            System.out.println("Failed to hover over leave dropdown: " + e.getMessage());
        }
         System.out.println("Navigating to Leave Summary -> Employee leave summary");

        try {
            WebDriver actualDriver = driver;
            if (driver instanceof com.orangeHrm.utils.WebDriverDispatcher) {
                actualDriver = ((com.orangeHrm.utils.WebDriverDispatcher) driver).getUnderlyingDriver();
            }

            // Hover over Company Info
            System.out.println("Hovering over Leave Summary...");
            Actions actions = new Actions(actualDriver);
            actions.moveToElement(leaveSummary).perform();
            System.out.println("Successfully hovered over leave Summary");
            Thread.sleep(1000);

            // Click on General
            System.out.println("Clicking on employee leave summary");
            actions.click(employeeLeaveSummary).perform();
            System.out.println("Successfully clicked employee leave summary");
            Thread.sleep(2000);
        } catch (Exception e) {
            System.out.println("Failed in navigation: " + e.getMessage());
        }
    }

    private void switchToAdminFrame() {
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

    public void selectEmployee() throws InterruptedException {
        Thread.sleep(1500);
        // Now switch to the rightMenu iframe to interact with the Employee form
        switchToAdminFrame();
        Thread.sleep(1000);
        // Fill year
        fillYearDropdown();
        Thread.sleep(1000);

        // Fill employee name
        fillEmployeeNameDropdown();
        Thread.sleep(1000);

        click3dotsButton();
        Thread.sleep(5000);

        selectAnEmployee();
        Thread.sleep(5000);

     //   driver.switchTo().defaultContent();
        switchToAdminFrame();
        Thread.sleep(5000);
        //Leave Type from dropdown
        fillLeaveTypeDropdown();

        clickViewButton();


    }

    private void fillLeaveTypeDropdown() {
        System.out.println("Attempting to click on leave type from dropdown with: " + "Medical");

        try {
            System.out.println("Trying to click on Medical from leave type dropdown");
            SeleniumTestHelper.selectFromDropDown(leaveTypeXpath, "Medical", "visibletext");
            System.out.println("Successfully filled Medical from leave type dropdown");

        } catch (Exception e) {
            System.out.println("Failed with xpath search for leave type : " + e.getMessage());

        }
    }

    private void selectAnEmployee() {
        SeleniumTestHelper.reportLog("Attempting to click on year dropdown with: ");

        try {
            SeleniumTestHelper.switchToOtherWindow(driver);
            //SeleniumTestHelper.switchingTochildtWindowAndCloseIt(driver);

            String currentHandle = driver.getWindowHandle();
            Set<String> handles = driver.getWindowHandles();
            for (String handle : handles) {
                if (!handle.equals(currentHandle)) {
                    SeleniumTestHelper.click(firstEmployee);
                  //  driver.close();
                    driver.switchTo().window(handle);
                }
            }



           // SeleniumTestHelper.switchToOtherWindow(driver).switchTo().defaultContent();


        } catch (Exception e) {
            System.out.println("Failed with xpath search for year : " + e.getMessage());

        }
    }

    private void fillYearDropdown() {
        System.out.println("Attempting to click on year dropdown with: " + "2025");

        try {
            System.out.println("Trying to click on year dropdown");
            SeleniumTestHelper.selectFromDropDown(fillYearDropdownXpath, "2025", "VALUE");
            System.out.println("Successfully filled year in the year dropdown");

        } catch (Exception e) {
            System.out.println("Failed with xpath search for year : " + e.getMessage());

        }
    }

    private void fillEmployeeNameDropdown() {
        System.out.println("Attempting to click on year dropdown with: " + "Select");

        try {
            System.out.println("Trying to click on year dropdown");
            SeleniumTestHelper.selectFromDropDown(fillEmployeeDropdownXpath, "Select", "visibletext");
            System.out.println("Successfully filled year in the year dropdown");

        } catch (Exception e) {
            System.out.println("Failed with xpath search for year : " + e.getMessage());

        }
    }

    private void click3dotsButton() throws InterruptedException {
        System.out.println("Attempting to click 3dots button...");
        try {
            System.out.println("Trying moreButton (button contains 'more')");
            SeleniumTestHelper.click(moreButton);
            System.out.println("Successfully clicked moreButton");
            return;
        } catch (Exception e) {
            System.out.println("Failed with addButton: " + e.getMessage());
        }

        try {
            System.out.println("Trying addLink (a contains 'more2')");
            SeleniumTestHelper.click(moreButton2);
            System.out.println("Successfully clicked more2");
            return;
        } catch (Exception e) {
            System.out.println("Failed with more2: " + e.getMessage());
        }

    }

    private void clickViewButton() throws InterruptedException {
        System.out.println("Attempting to click view button...");
        try {
            System.out.println("Trying viewButton (button contains 'view')");
            SeleniumTestHelper.click(viewButton);
            System.out.println("Successfully clicked viewButton");
            return;
        } catch (Exception e) {
            System.out.println("Failed with viewButton: " + e.getMessage());
        }

        try {
            System.out.println("Trying addLink (a contains 'view')");
            SeleniumTestHelper.click(viewButtonByClass);
            System.out.println("Successfully clicked view button");
            return;
        } catch (Exception e) {
            System.out.println("Failed with view: " + e.getMessage());
        }

    }

    // Date Picker Getter Methods
    public WebElement getCalendarButton() {
        return calendarButton;
    }

    public WebElement getDateInputField() {
        return dateInputField;
    }

    public WebElement getCalendarContainer() {
        return calendarContainer;
    }

    public WebElement getCalendarTable() {
        return calendarTable;
    }

    public WebElement getCalendarHeader() {
        return calendarHeader;
    }

    public WebElement getNextMonthButton() {
        return nextMonthButton;
    }

    public WebElement getPreviousMonthButton() {
        return previousMonthButton;
    }

    /**
     * Dynamically gets a date element from the calendar by date number
     * @param driver WebDriver instance
     * @param dayNumber The day number to select (1-31)
     * @return WebElement representing the date link
     */
    public WebElement getDateElement(WebDriver driver, int dayNumber) {
        return driver.findElement(By.xpath("//table[@id='cal1']//a[text()='" + dayNumber + "']"));
    }

    /**
     * Gets the current month/year text from the calendar header
     * @return String containing "Month Year" (e.g., "March 2026")
     */
    public String getCurrentMonthYearText() {
        return calendarHeader.getText().trim();
    }
}
