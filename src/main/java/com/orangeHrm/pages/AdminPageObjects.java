package com.orangeHrm.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.orangeHrm.utils.Driver;
import com.orangeHrm.utils.SeleniumTestHelper;

@SuppressWarnings("null")
public class AdminPageObjects {

    /**
     * Constructor with WebDriver injection - prevents duplicate Driver.getInstance() calls
     */
    public AdminPageObjects(WebDriver driver) {
        this.driver = driver;
        // Ensure PageFactory elements are initialized with the underlying driver
        WebDriver actualDriver = driver;
        if (driver instanceof com.orangeHrm.utils.WebDriverDispatcher) {
            actualDriver = ((com.orangeHrm.utils.WebDriverDispatcher) driver).getUnderlyingDriver();
        }
        PageFactory.initElements(actualDriver, this);
    }

    private final WebDriver driver;

    // Admin Module Navigation - using correct selectors
    @FindBy(id = "admin")
    private WebElement adminDropdown;

    @FindBy(xpath = "//span[contains(text(), 'Company Info')]")
    private WebElement companyInfoLink;

    @FindBy(xpath = "//span[contains(text(), 'General')]")
    private WebElement generalLink;

    @FindBy(xpath = "//span[contains(text(), 'Employee Information')]")
    private WebElement employeeInformationLink;

    // Alternative selectors for Company Info and General
    @FindBy(xpath = "//span[contains(text(),'Company Info')]")
    public WebElement companyInfoSpan;

    @FindBy(xpath = "//span[contains(text(),'General')]")
    private WebElement generalSpan;

    // Deprecated - old selectors kept for backward compatibility
    @FindBy(xpath = "//a[contains(text(), 'Admin')]")
    private WebElement adminMenuLink;

    @FindBy(xpath = "//button[contains(text(), 'Admin')]")
    private WebElement adminMenuButton;

    @FindBy(xpath = "//span[contains(text(), 'Admin')]/..")
    private WebElement adminMenuSpan;

    // Add Employee Button and Form Elements
    @FindBy(xpath = "//button[contains(text(), 'Add')]")
    private WebElement addButton;

    @FindBy(xpath = "//a[contains(text(), 'Add')]")
    private WebElement addLink;

    // Correct field names for employee creation form
    @FindBy(xpath = "//input[@id='txtEmpFirstName']")
    private WebElement firstNameField;

    @FindBy(xpath = "//input[@name='txtEmpFirstName']")
    private WebElement firstNameFieldByName;

    @FindBy(xpath = "//input[@placeholder='First Name']")
    private WebElement firstNameFieldByPlaceholder;

    @FindBy(xpath = "//input[@id='txtEmpLastName']")
    private WebElement lastNameField;

    @FindBy(xpath = "//input[@name='txtEmpLastName']")
    private WebElement lastNameFieldByName;

    @FindBy(xpath = "//input[@placeholder='Last Name']")
    private WebElement lastNameFieldByPlaceholder;

    @FindBy(xpath = "//input[@name='addEmpLastName']")
    private WebElement lastNameFieldByNameAdd;

    @FindBy(xpath = "//button[@id='btnSave']")
    private WebElement saveButton;

    @FindBy(xpath = "//button[contains(text(), 'Save')]")
    private WebElement saveButtonByText;

    // Edit Company info by clicking on edit button
    @FindBy(xpath = "//input[@id='editBtn']")
    private WebElement editButonById;

    // Correct field names for employee creation form
    @FindBy(xpath = "//input[@id='txtCompanyName']")
    private WebElement companyNameField;

    @FindBy(xpath = "//input[@name='txtCompanyName']")
    private WebElement companyNameFieldByName;

    // Tax Id Field
    @FindBy(xpath = "//input[@id='txtTaxID']")
    private WebElement taxIdField;

    @FindBy(xpath = "//input[@name='txtTaxID']")
    private WebElement taxIdFieldByName;

    // NAICS Field
    @FindBy(xpath = "//input[@id='txtNAICS']")
    private WebElement NAICSField;

    @FindBy(xpath = "//input[@name='txtNAICS']")
    private WebElement NAICSFieldByName;

    // Phone Field
    @FindBy(xpath = "//input[@id='txtPhone']")
    private WebElement phoneField;

    @FindBy(xpath = "//input[@name='txtPhone']")
    private WebElement phoneFieldByName;

    // Fax Field
    @FindBy(xpath = "//input[@id='txtFax']")
    private WebElement faxField;

    @FindBy(xpath = "//input[@name='txtFax']")
    private WebElement faxFieldByName;

    // Country dropdown
    @FindBy(xpath = "//select[@id='cmbCountry']")
    private WebElement countryDropdown;

    @FindBy(xpath = "//select[@name='cmbCountry']")
    private WebElement countryDropdownByName;

    @FindBy(xpath = "//input[@class='savebutton']")
    private WebElement companyInfoSaveButton;

    @FindBy(xpath = "//input[contains(@title, 'Save')]")
    private WebElement saveButtonByTitle;

    // Address field 1 by id and name
    @FindBy(xpath = "//input[@id='txtStreet1']")
    private WebElement address1Field;

    @FindBy(xpath = "//input[@name='txtStreet1']")
    private WebElement address1FieldByName;

    // Address field 2 by id and name
    @FindBy(xpath = "//input[@id='txtStreet2']")
    private WebElement address2Field;

    @FindBy(xpath = "//input[@name='txtStreet2']")
    private WebElement address2FieldByName;

    // City field by id and name
    @FindBy(xpath = "//input[@id='cmbCity']")
    private WebElement cityField;

    @FindBy(xpath = "//input[@name='cmbCity']")
    private WebElement cityFieldByName;

    // state field by id and name
    @FindBy(xpath = "//input[@id='txtState']")
    private WebElement stateField;

    @FindBy(xpath = "//input[@name='txtState']")
    private WebElement stateFieldByName;

    // Zipcode field by id and name
    @FindBy(xpath = "//input[@id='txtZIP']")
    private WebElement zipCodeField;

    @FindBy(xpath = "//input[@name='txtZIP']")
    private WebElement zipCodeFieldByName;

    // Comments field by id and name
    @FindBy(xpath = "//textarea[@id='txtComments']")
    private WebElement commentsField;

    @FindBy(xpath = "//textarea[@name='txtComments']")
    private WebElement commentsFieldByName;

    @FindBy(xpath = " (//ul[contains(@class,'l2')][.//a[contains(@class,'companyinfo')]]) /li[contains(@class,'l2')]/a[contains(@class,'l2_link')]/span")
    public List<WebElement> adminMenuItems;

    @FindBy(xpath = "//span[normalize-space()='Company Info']")
    public WebElement adminCompanyInfo;


    /**
     * Deprecated: Use constructor with WebDriver parameter instead
     */
    @Deprecated
    public AdminPageObjects() {
        this(Driver.getInstance());
    }

    /**
     * Navigate to Admin module by hovering on the Admin dropdown (id="admin")
     */
    public void navigateToAdminModule() throws InterruptedException {
        WebDriver driver = Driver.getInstance();
        Thread.sleep(1500);

        System.out.println("Hovering over Admin dropdown...");

        try {
            // Use SeleniumTestHelper.mouseHover to perform a robust hover
            SeleniumTestHelper.mouseHover("id", "admin");
            System.out.println("Successfully hovered over Admin dropdown (via SeleniumTestHelper)");
            Thread.sleep(1500);
        } catch (Exception e) {
            System.out.println("Failed to hover over admin dropdown: " + e.getMessage());
        }

        System.out.println("Navigating to Company Info -> General...");

        try {
            WebDriver actualDriver = driver;
            if (driver instanceof com.orangeHrm.utils.WebDriverDispatcher) {
                actualDriver = ((com.orangeHrm.utils.WebDriverDispatcher) driver).getUnderlyingDriver();
            }

            // Hover over Company Info
            System.out.println("Hovering over Company Info...");
            Actions actions = new Actions(actualDriver);
            actions.moveToElement(companyInfoSpan).perform();
            System.out.println("Successfully hovered over Company Info");
            Thread.sleep(1000);

            // Click on General
            System.out.println("Clicking on General...");
            actions.click(generalSpan).perform();
            System.out.println("Successfully clicked General");
            Thread.sleep(2000);
        } catch (Exception e) {
            System.out.println("Failed in navigation: " + e.getMessage());
        }
    }

    /**
     * Navigate to Company Info -> General section from Admin module
     */
    public void navigateToEmployeeManagement() throws InterruptedException {
        WebDriver driver = Driver.getInstance();
        Thread.sleep(1500);

        System.out.println("Navigating to Company Info -> General...");

        try {
            WebDriver actualDriver = driver;
            if (driver instanceof com.orangeHrm.utils.WebDriverDispatcher) {
                actualDriver = ((com.orangeHrm.utils.WebDriverDispatcher) driver).getUnderlyingDriver();
            }

            // Hover over Company Info
            System.out.println("Hovering over Company Info...");
            Actions actions = new Actions(actualDriver);
            actions.moveToElement(companyInfoSpan).perform();
            System.out.println("Successfully hovered over Company Info");
            Thread.sleep(1000);

            // Click on General
            System.out.println("Clicking on General...");
            actions.click(generalSpan).perform();
            System.out.println("Successfully clicked General");
            Thread.sleep(2000);
        } catch (Exception e) {
            System.out.println("Failed in navigation: " + e.getMessage());
        }
    }

    /**
     * Switch to admin iframe if it exists
     */
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

    /**
     * Add a new employee with required details
     *
     * @param firstName First name of the employee
     * @param lastName  Last name of the employee
     */
    public void addNewEmployee(String firstName, String lastName) throws InterruptedException {
        Thread.sleep(1500);

        // Navigate to Employee Management section
        // This should search for the Employee link in the Admin page navigation (not in the iframe)
        navigateToEmployeeManagement();

        Thread.sleep(1500);

        // Now switch to the rightMenu iframe to interact with the Employee form
        switchToAdminFrame();

        Thread.sleep(1000);

        // Click Add button to open employee creation form
        clickAddButton();

        // Wait for form to appear
        Thread.sleep(1500);

        // Fill First Name
        fillFirstNameField(firstName);

        // Fill Last Name
        fillLastNameField(lastName);

        // Click Save button
        clickSaveButton();

        Thread.sleep(1000);
    }

    /**
     * Add a new employee with default test data
     */
    public void addNewEmployee() throws InterruptedException {
        addNewEmployee("TestEmployee", "TestLast");
    }

    /**
     * Click the Add button
     */
    private void clickAddButton() throws InterruptedException {
        System.out.println("Attempting to click Add button...");
        try {
            System.out.println("Trying addButton (button contains 'Add')");
            SeleniumTestHelper.click(addButton);
            System.out.println("Successfully clicked addButton");
            return;
        } catch (Exception e) {
            System.out.println("Failed with addButton: " + e.getMessage());
        }

        try {
            System.out.println("Trying addLink (a contains 'Add')");
            SeleniumTestHelper.click(addLink);
            System.out.println("Successfully clicked addLink");
            return;
        } catch (Exception e) {
            System.out.println("Failed with addLink: " + e.getMessage());
        }

        // Fallback: Try alternative button locators
        String[] addXpaths = new String[]{"//input[@value='Add']", "//button[@id='btnAdd']", "//a[@id='btnAdd']", "//button[contains(text(), 'New')]", "//a[contains(text(), 'New')]", "//input[@id='btnAdd']"};

        for (String xp : addXpaths) {
            try {
                System.out.println("Trying xpath: " + xp);
                List<WebElement> els = driver.findElements(By.xpath(xp));
                if (els != null && !els.isEmpty()) {
                    els.get(0).click();
                    System.out.println("Successfully clicked Add button with xpath: " + xp);
                    Thread.sleep(500);
                    return;
                }
            } catch (Exception e) {
                System.out.println("Failed with xpath: " + xp);
            }
        }
        System.out.println("Could not find Add button with any selector");
    }

    /**
     * Fill the First Name field
     */
    private void fillFirstNameField(String firstName) throws InterruptedException {
        System.out.println("Attempting to fill firstName field (txtEmpFirstName) with: " + firstName);

        try {
            System.out.println("Trying firstNameField (id='txtEmpFirstName')");
            SeleniumTestHelper.clear(firstNameField);
            SeleniumTestHelper.enterText(firstNameField, firstName);
            System.out.println("Successfully filled firstName field");
            return;
        } catch (Exception e) {
            System.out.println("Failed with firstNameField: " + e.getMessage());
        }

        try {
            System.out.println("Trying firstNameFieldByName (name='txtEmpFirstName')");
            SeleniumTestHelper.clear(firstNameFieldByName);
            SeleniumTestHelper.enterText(firstNameFieldByName, firstName);
            System.out.println("Successfully filled firstName field by name");
            return;
        } catch (Exception e) {
            System.out.println("Failed with firstNameFieldByName: " + e.getMessage());
        }

        try {
            System.out.println("Trying firstNameFieldByPlaceholder");
            SeleniumTestHelper.clear(firstNameFieldByPlaceholder);
            SeleniumTestHelper.enterText(firstNameFieldByPlaceholder, firstName);
            System.out.println("Successfully filled firstName field by placeholder");
            return;
        } catch (Exception e) {
            System.out.println("Failed with firstNameFieldByPlaceholder: " + e.getMessage());
        }

        try {
            System.out.println("Trying xpath: //input[@id='txtEmpFirstName']");
            List<WebElement> els = driver.findElements(By.xpath("//input[@id='txtEmpFirstName']"));
            if (els != null && !els.isEmpty()) {
                SeleniumTestHelper.clear(els.get(0));
                SeleniumTestHelper.enterText(els.get(0), firstName);
                System.out.println("Successfully filled firstName field (xpath)");
                return;
            }
        } catch (Exception e) {
            System.out.println("Failed with xpath search for firstName: " + e.getMessage());
        }
    }

    /**
     * Fill the Last Name field
     */
    private void fillLastNameField(String lastName) throws InterruptedException {
        System.out.println("Attempting to fill lastName field (txtEmpLastName) with: " + lastName);

        try {
            System.out.println("Trying lastNameField (id='txtEmpLastName')");
            SeleniumTestHelper.clear(lastNameField);
            SeleniumTestHelper.enterText(lastNameField, lastName);
            System.out.println("Successfully filled lastName field");
            return;
        } catch (Exception e) {
            System.out.println("Failed with lastNameField: " + e.getMessage());
        }

        try {
            System.out.println("Trying lastNameFieldByName (name='txtEmpLastName')");
            SeleniumTestHelper.clear(lastNameFieldByName);
            SeleniumTestHelper.enterText(lastNameFieldByName, lastName);
            System.out.println("Successfully filled lastName field by name");
            return;
        } catch (Exception e) {
            System.out.println("Failed with lastNameFieldByName: " + e.getMessage());
        }

        try {
            System.out.println("Trying lastNameFieldByPlaceholder");
            SeleniumTestHelper.clear(lastNameFieldByPlaceholder);
            SeleniumTestHelper.enterText(lastNameFieldByPlaceholder, lastName);
            System.out.println("Successfully filled lastName field by placeholder");
            return;
        } catch (Exception e) {
            System.out.println("Failed with lastNameFieldByPlaceholder: " + e.getMessage());
        }

        try {
            System.out.println("Trying xpath: //input[@id='txtEmpLastName']");
            List<WebElement> els = driver.findElements(By.xpath("//input[@id='txtEmpLastName']"));
            if (els != null && !els.isEmpty()) {
                SeleniumTestHelper.clear(els.get(0));
                SeleniumTestHelper.enterText(els.get(0), lastName);
                System.out.println("Successfully filled lastName field (xpath)");
                return;
            }
        } catch (Exception e) {
            System.out.println("Failed with xpath search for lastName: " + e.getMessage());
        }
    }

    /**
     * Click the Save button
     */
    private void clickSaveButton() throws InterruptedException {
        try {
            SeleniumTestHelper.click(saveButton);
            return;
        } catch (Exception e) {
            // Try alternative
        }

        try {
            SeleniumTestHelper.click(saveButtonByText);
            return;
        } catch (Exception e) {
            // Fallback: Try generic button locators
        }

        // Fallback: Try alternative button locators
        String[] saveXpaths = new String[]{"//input[@id='btnSave']", "//button[@id='btnSave']", "//input[@value='Save']"};

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

    public WebDriver getDriver() {
        return driver;
    }

    /**
     * Add a company info with required details
     *
     */
    public void enterCompanyInformation() throws InterruptedException {
        Thread.sleep(1500);
        // Now switch to the rightMenu iframe to interact with the Employee form
        switchToAdminFrame();
        Thread.sleep(1000);
        // Click Add button to open employee creation form
        clickEditButton();
        // Wait for form to appear
        Thread.sleep(1500);
        // Fill Company Name
        fillCompanyNameField("Wipro");
        //Fill Tax Id
        fillTaxIdField("6563");//
        //Fill NAICS
        fillNAICSField("86866");
        // Fill Phone
        fillPhoneField("8686609500");
//		// Fill Fax
        fillFaxField("12345");
        // Fill Country
        fillCountrydropdown("IN");
//
//		// Fill Address1
		fillAddressField1(" 456 W 100 N or 123 Main St");
//		// Fill Address2
		fillAddressField2("RR 3 Box 9, Canton OH 44730");
//		// Fill City
		fillCityField("Cannada");
//
//		// Fill State
		fillStateField("Phoenix");
//
//		// Fill Zipcode
		fillZipcodeField("85001 ");
//		// Fill Comments
	fillCommentsField("An itemized list of goods and/or services requested by the client or customer.\n" +
            "Prices for each item including labor costs, taxes and discounts." +
            "Disclaimers on the scope of the product or project" +
            "Payment terms and contact information for follow-up.");


        // Click Save button
        companyInfoClickSaveButton();

        Thread.sleep(1000);
    }

    /**
     * Click the Edit button
     */
    private void clickEditButton() {
        System.out.println("Attempting to click Edit button...");
        try {
            System.out.println("Trying editButton (button contains 'Edit')");
            SeleniumTestHelper.click(editButonById);
            System.out.println("Successfully clicked editButton");
            return;
        } catch (Exception e) {
            System.out.println("Failed with editButton: " + e.getMessage());
        }

        // Fallback: Try alternative button locators
        String[] addXpaths = new String[]{"//input[@value='Edit']", "//input[@id='editBtn']", "//input[@class='editbutton']"};

        for (String xp : addXpaths) {
            try {
                System.out.println("Trying xpath: " + xp);
                List<WebElement> els = driver.findElements(By.xpath(xp));
                if (els != null && !els.isEmpty()) {
                    els.get(0).click();
                    System.out.println("Successfully clicked edit button with xpath: " + xp);
                    Thread.sleep(500);
                    return;
                }
            } catch (Exception e) {
                System.out.println("Failed with xpath: " + xp);
            }
        }
        System.out.println("Could not find Edit button with any selector");
    }

    /**
     * Fill the First Name field
     */


    private void fillCompanyNameField(String companyName) throws InterruptedException {
        System.out.println("Attempting to fill companyName field (txtCompanyName) with: " + companyName);

        try {
            System.out.println("Trying companyNameField (id='txtCompanyName')");
            SeleniumTestHelper.clear(companyNameField);
            SeleniumTestHelper.enterText(companyNameField, companyName);
            System.out.println("Successfully filled companyName field");
            return;
        } catch (Exception e) {
            System.out.println("Failed with companyNameField: " + e.getMessage());
        }

        try {
            System.out.println("Trying companyNameFieldByName (name='txtCompanyName')");
            SeleniumTestHelper.clear(companyNameFieldByName);
            SeleniumTestHelper.enterText(companyNameFieldByName, companyName);
            System.out.println("Successfully filled companyName field by name");
            return;
        } catch (Exception e) {
            System.out.println("Failed with companyNameFieldByName: " + e.getMessage());
        }


        try {
            System.out.println("Trying xpath: //input[@id='txtCompanyName']");
            List<WebElement> els = driver.findElements(By.xpath("//input[@id='txtCompanyName']"));
            if (!els.isEmpty()) {
                SeleniumTestHelper.clear(els.get(0));
                SeleniumTestHelper.enterText(els.get(0), companyName);
                System.out.println("Successfully filled companyName field (xpath)");
                return;
            }
        } catch (Exception e) {
            System.out.println("Failed with xpath search for companyName: " + e.getMessage());
        }
    }

    private void fillTaxIdField(String taxID) throws InterruptedException {
        System.out.println("Attempting to fill taxID field (txtTaxID) with: " + taxID);

        try {
            System.out.println("Trying taxIDField (id='txtTaxID')");
            SeleniumTestHelper.clear(taxIdField);
            SeleniumTestHelper.enterText(taxIdField, taxID);
            System.out.println("Successfully filled taxID field");
            return;
        } catch (Exception e) {
            System.out.println("Failed with taxIdField: " + e.getMessage());
        }

        try {
            System.out.println("Trying taxIdFieldFieldByName (name='txtTaxID')");
            SeleniumTestHelper.clear(taxIdFieldByName);
            SeleniumTestHelper.enterText(taxIdFieldByName, taxID);
            System.out.println("Successfully filled taxID field by name");
            return;
        } catch (Exception e) {
            System.out.println("Failed with taxIdFieldByName: " + e.getMessage());
        }


        try {
            System.out.println("Trying xpath: //input[@id='txtTaxID']");
            List<WebElement> els = driver.findElements(By.xpath("//input[@id='txtTaxID']"));
            if (!els.isEmpty()) {
                SeleniumTestHelper.clear(els.get(0));
                SeleniumTestHelper.enterText(els.get(0), taxID);
                System.out.println("Successfully filled taxID field (xpath)");
                return;
            }
        } catch (Exception e) {
            System.out.println("Failed with xpath search for taxID: " + e.getMessage());
        }
    }

    private void fillNAICSField(String NAICS) throws InterruptedException {
        System.out.println("Attempting to fill taxID field (txtNAICS) with: " + NAICS);

        try {
            System.out.println("Trying NAICSField (id='txtNAICS')");
            SeleniumTestHelper.clear(NAICSField);
            SeleniumTestHelper.enterText(NAICSField, NAICS);
            System.out.println("Successfully filled NAICS field");
            return;
        } catch (Exception e) {
            System.out.println("Failed with NAICSField: " + e.getMessage());
        }

        try {
            System.out.println("Trying NAICSFieldByName (name='txtNAICS')");
            SeleniumTestHelper.clear(NAICSFieldByName);
            SeleniumTestHelper.enterText(NAICSFieldByName, NAICS);
            System.out.println("Successfully filled NAICS field by name");
            return;
        } catch (Exception e) {
            System.out.println("Failed with NAICSFieldByName: " + e.getMessage());
        }


        try {
            System.out.println("Trying xpath: //input[@id='txtNAICS']");
            List<WebElement> els = driver.findElements(By.xpath("//input[@id='txtNAICS']"));
            if (!els.isEmpty()) {
                SeleniumTestHelper.clear(els.get(0));
                SeleniumTestHelper.enterText(els.get(0), NAICS);
                System.out.println("Successfully filled NAICS field (xpath)");
                return;
            }
        } catch (Exception e) {
            System.out.println("Failed with xpath search for NAICS: " + e.getMessage());
        }
    }


    private void fillPhoneField(String Phone) throws InterruptedException {
        System.out.println("Attempting to fill taxID field (txtPhone) with: " + Phone);

        try {
            System.out.println("Trying phoneField (id='txtPhone')");
            SeleniumTestHelper.clear(phoneField);
            SeleniumTestHelper.enterText(phoneField, Phone);
            System.out.println("Successfully filled phone field");
            return;
        } catch (Exception e) {
            System.out.println("Failed with phoneField: " + e.getMessage());
        }

        try {
            System.out.println("Trying phoneFieldByName (name='txtPhone')");
            SeleniumTestHelper.clear(phoneFieldByName);
            SeleniumTestHelper.enterText(phoneFieldByName, Phone);
            System.out.println("Successfully filled Phone field by name");
            return;
        } catch (Exception e) {
            System.out.println("Failed with phoneFieldByName: " + e.getMessage());
        }


        try {
            System.out.println("Trying xpath: //input[@id='txtPhone']");
            List<WebElement> els = driver.findElements(By.xpath("//input[@id='txtPhone']"));
            if (!els.isEmpty()) {
                SeleniumTestHelper.clear(els.get(0));
                SeleniumTestHelper.enterText(els.get(0), Phone);
                System.out.println("Successfully filled Phone field (xpath)");
                return;
            }
        } catch (Exception e) {
            System.out.println("Failed with xpath search for Phone: " + e.getMessage());
        }
    }

    private void fillFaxField(String fax) throws InterruptedException {
        System.out.println("Attempting to fill fax field (txtFax) with: " + fax);

        try {
            System.out.println("Trying fax field (id='txtFax')");
            SeleniumTestHelper.clear(faxField);
            SeleniumTestHelper.enterText(faxField, fax);
            System.out.println("Successfully filled fax field");
            return;
        } catch (Exception e) {
            System.out.println("Failed with fax Field: " + e.getMessage());
        }

        try {
            System.out.println("Trying faxFieldByName (name='txtPhone')");
            SeleniumTestHelper.clear(faxFieldByName);
            SeleniumTestHelper.enterText(faxFieldByName, fax);
            System.out.println("Successfully filled fax field by name");
            return;
        } catch (Exception e) {
            System.out.println("Failed with faxFieldByName: " + e.getMessage());
        }


        try {
            System.out.println("Trying xpath: //input[@id='txtFax']");
            List<WebElement> els = driver.findElements(By.xpath("//input[@id='txtFax']"));
            if (!els.isEmpty()) {
                SeleniumTestHelper.clear(els.get(0));
                SeleniumTestHelper.enterText(els.get(0), fax);
                System.out.println("Successfully filled fax field (xpath)");
                return;
            }
        } catch (Exception e) {
            System.out.println("Failed with xpath search for fax: " + e.getMessage());
        }
    }

    private void fillCountrydropdown(String country) {
        System.out.println("Attempting to click pn country dropdown fax field (cmbCountry) with: " + country);

        try {
            System.out.println("Trying to click on country dropdown (id='cmbCountry')");
            SeleniumTestHelper.selectFromDropDown(countryDropdown, country, "VALUE");
            System.out.println("Successfully filled fax field");

        } catch (Exception e) {
            System.out.println("Failed with xpath search for fax: " + e.getMessage());

        }
    }
    private void fillAddressField1(String address1) throws InterruptedException {
        System.out.println("Attempting to fill address1 field (txtStreet1) with: " + address1);

        try {
            System.out.println("Trying address1 (id='txtStreet1')");
            SeleniumTestHelper.clear(address1Field);
            SeleniumTestHelper.enterText(address1Field, address1);
            System.out.println("Successfully filled address1 field");
            return;
        } catch (Exception e) {
            System.out.println("Failed with address1Field: " + e.getMessage());
        }

        try {
            System.out.println("Trying address1FieldByName (name='txtStreet1')");
            SeleniumTestHelper.clear(address1FieldByName);
            SeleniumTestHelper.enterText(address1FieldByName, address1);
            System.out.println("Successfully filled address1 field by name");
            return;
        } catch (Exception e) {
            System.out.println("Failed with address1FieldByName: " + e.getMessage());
        }


        try {
            System.out.println("Trying xpath: //input[@id='txtStreet1']");
            List<WebElement> els = driver.findElements(By.xpath("//input[@id='txtStreet1']"));
            if (!els.isEmpty()) {
                SeleniumTestHelper.clear(els.get(0));
                SeleniumTestHelper.enterText(els.get(0), address1);
                System.out.println("Successfully filled address1 field (xpath)");
                return;
            }
        } catch (Exception e) {
            System.out.println("Failed with xpath search for address1: " + e.getMessage());
        }
    }

    private void fillAddressField2(String address2) throws InterruptedException {
        System.out.println("Attempting to fill address2 field (txtStreet2) with: " + address2);

        try {
            System.out.println("Trying address2Field (id='txtStreet2')");
            SeleniumTestHelper.clear(address2Field);
            SeleniumTestHelper.enterText(address2Field, address2);
            System.out.println("Successfully filled address2 field");
            return;
        } catch (Exception e) {
            System.out.println("Failed with address2 Field: " + e.getMessage());
        }

        try {
            System.out.println("Trying address2FieldByName (name='txtStreet2')");
            SeleniumTestHelper.clear(address2FieldByName);
            SeleniumTestHelper.enterText(address2FieldByName, address2);
            System.out.println("Successfully filled address2 field by name");
            return;
        } catch (Exception e) {
            System.out.println("Failed with address2FieldByName: " + e.getMessage());
        }


        try {
            System.out.println("Trying xpath: //input[@id='txtStreet2']");
            List<WebElement> els = driver.findElements(By.xpath("//input[@id='txtStreet2']"));
            if (!els.isEmpty()) {
                SeleniumTestHelper.clear(els.get(0));
                SeleniumTestHelper.enterText(els.get(0), address2);
                System.out.println("Successfully filled address2 field (xpath)");
                return;
            }
        } catch (Exception e) {
            System.out.println("Failed with xpath search for address2: " + e.getMessage());
        }
    }

    private void fillCityField(String city) throws InterruptedException {
        System.out.println("Attempting to fill city field (cmbCity) with: " + city);

        try {
            System.out.println("Trying cityField (id='cmbCity')");
            SeleniumTestHelper.clear(cityField);
            SeleniumTestHelper.enterText(cityField, city);
            System.out.println("Successfully filled city field");
            return;
        } catch (Exception e) {
            System.out.println("Failed with cityField: " + e.getMessage());
        }

        try {
            System.out.println("Trying cityFieldByName (name='cmbCity')");
            SeleniumTestHelper.clear(cityFieldByName);
            SeleniumTestHelper.enterText(cityFieldByName, city);
            System.out.println("Successfully filled city field by name");
            return;
        } catch (Exception e) {
            System.out.println("Failed with cityFieldByName: " + e.getMessage());
        }


        try {
            System.out.println("Trying xpath: //input[@id='cmbCity']");
            List<WebElement> els = driver.findElements(By.xpath("//input[@id='cmbCity']"));
            if (!els.isEmpty()) {
                SeleniumTestHelper.clear(els.get(0));
                SeleniumTestHelper.enterText(els.get(0), city);
                System.out.println("Successfully filled city field (xpath)");
                return;
            }
        } catch (Exception e) {
            System.out.println("Failed with xpath search for city: " + e.getMessage());
        }
    }

    private void fillStateField(String state) throws InterruptedException {
        System.out.println("Attempting to fill state field (txtState) with: " + state);

        try {
            System.out.println("Trying stateField (id='txtState')");
            SeleniumTestHelper.clear(stateField);
            SeleniumTestHelper.enterText(stateField, state);
            System.out.println("Successfully filled state field");
            return;
        } catch (Exception e) {
            System.out.println("Failed with stateField: " + e.getMessage());
        }

        try {
            System.out.println("Trying stateFieldByName (name='txtState')");
            SeleniumTestHelper.clear(stateFieldByName);
            SeleniumTestHelper.enterText(stateFieldByName, state);
            System.out.println("Successfully filled state field by name");
            return;
        } catch (Exception e) {
            System.out.println("Failed with stateFieldByName: " + e.getMessage());
        }


        try {
            System.out.println("Trying xpath: //input[@id='txtState']");
            List<WebElement> els = driver.findElements(By.xpath("//input[@id='txtState']"));
            if (!els.isEmpty()) {
                SeleniumTestHelper.clear(els.get(0));
                SeleniumTestHelper.enterText(els.get(0), state);
                System.out.println("Successfully filled state field (xpath)");
                return;
            }
        } catch (Exception e) {
            System.out.println("Failed with xpath search for state: " + e.getMessage());
        }
    }

    private void fillZipcodeField(String zipCode) throws InterruptedException {
        System.out.println("Attempting to fill zipCode field (txtZIP) with: " + zipCode);

        try {
            System.out.println("Trying zipCodeField (id='txtZIP')");
            SeleniumTestHelper.clear(zipCodeField);
            SeleniumTestHelper.enterText(zipCodeField, zipCode);
            System.out.println("Successfully filled zipCode field");
            return;
        } catch (Exception e) {
            System.out.println("Failed with zipCodeField: " + e.getMessage());
        }

        try {
            System.out.println("Trying zipCodeFieldByName (name='txtZIP')");
            SeleniumTestHelper.clear(zipCodeFieldByName);
            SeleniumTestHelper.enterText(zipCodeFieldByName, zipCode);
            System.out.println("Successfully filled zipCode field by name");
            return;
        } catch (Exception e) {
            System.out.println("Failed with zipCodeFieldByName: " + e.getMessage());
        }


        try {
            System.out.println("Trying xpath: //input[@id='txtZIP']");
            List<WebElement> els = driver.findElements(By.xpath("//input[@id='txtZIP']"));
            if (!els.isEmpty()) {
                SeleniumTestHelper.clear(els.get(0));
                SeleniumTestHelper.enterText(els.get(0), zipCode);
                System.out.println("Successfully filled zipCode field (xpath)");
                return;
            }
        } catch (Exception e) {
            System.out.println("Failed with xpath search for zipCode: " + e.getMessage());
        }
    }

    private void fillCommentsField(String comments) throws InterruptedException {
        System.out.println("Attempting to fill comments field (txtComments) with: " + comments);

        try {
            System.out.println("Trying commentsField (id='txtComments')");
            SeleniumTestHelper.clear(commentsField);
            SeleniumTestHelper.enterText(commentsField, comments);
            System.out.println("Successfully filled comments field");
            return;
        } catch (Exception e) {
            System.out.println("Failed with commentsField: " + e.getMessage());
        }

        try {
            System.out.println("Trying commentsFieldByName (name='txtComments')");
            SeleniumTestHelper.clear(commentsFieldByName);
            SeleniumTestHelper.enterText(commentsFieldByName, comments);
            System.out.println("Successfully filled comments field by name");
            return;
        } catch (Exception e) {
            System.out.println("Failed with commentsFieldByName: " + e.getMessage());
        }


        try {
            System.out.println("Trying xpath: //input[@id='txtComments']");
            List<WebElement> els = driver.findElements(By.xpath("//input[@id='txtComments']"));
            if (!els.isEmpty()) {
                SeleniumTestHelper.clear(els.get(0));
                SeleniumTestHelper.enterText(els.get(0), comments);
                System.out.println("Successfully filled comments field (xpath)");
                return;
            }
        } catch (Exception e) {
            System.out.println("Failed with xpath search for comments: " + e.getMessage());
        }
    }

    /**
     * Click the Save button
     */
    private void companyInfoClickSaveButton() throws InterruptedException {
        try {
            SeleniumTestHelper.click(companyInfoSaveButton);
            return;
        } catch (Exception e) {
            // Try alternative
        }

        try {
            SeleniumTestHelper.click(saveButtonByTitle);
            return;
        } catch (Exception e) {
            // Fallback: Try generic button locators
        }

        // Fallback: Try alternative button locators
        String[] saveXpaths = new String[]{"//input[contains(@class, 'savebutton')]", "//input[@id='editBtn']", "//input[@value='Save']"};

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
