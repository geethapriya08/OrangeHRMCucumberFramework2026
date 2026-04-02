package com.orangeHrm.pages;

import com.orangeHrm.utils.Driver;
import com.orangeHrm.utils.SeleniumTestHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class CompanyPageObjects {
    private WebDriver driver;

    // Admin Module Navigation - using correct selectors
    @FindBy(xpath = "//span[normalize-space()='Company Info']")
    private WebElement companyInfo;

    @FindBy(xpath = "//span[normalize-space()='General']")
    private WebElement general;

     @FindBy(xpath = "//h2[normalize-space()='Company Info : General']")
     public WebElement companyHeader;

     @FindBy(id = "rightMenu")
     public WebElement companyInfoFrame;

     @FindBy(id ="editBtn")
     public WebElement editButton;

     @FindBy(xpath = "//input[@id='txtCompanyName']")
     public WebElement companyNameFieldByxpath;

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
    public WebElement companyInfoSaveButton;

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


    public CompanyPageObjects(WebDriver driver) {
        this.driver = driver;
        // Ensure PageFactory elements are initialized with the underlying driver
        WebDriver actualDriver = driver;
        if (driver instanceof com.orangeHrm.utils.WebDriverDispatcher) {
            actualDriver = ((com.orangeHrm.utils.WebDriverDispatcher) driver).getUnderlyingDriver();
        }
        PageFactory.initElements(actualDriver, this);
    }

    public void enterCompanyDetails(String companyName, String taxId, String naics, String phone, String fax, String countryDropdown, String address1, String address2, String city, String state, String zipcode, String comments) throws InterruptedException {

        // Fill Company Name
        if (isNotEmpty(companyName)) fillCompanyName(companyName);
        // Fill taxId
        if (isNotEmpty(taxId)) filltaxIdField(taxId);
        // Fill naics
        if (isNotEmpty(naics)) fillNAICSField(naics);
        // Fill phone
        if (isNotEmpty(phone)) fillPhoneField(phone);
        // Fill fax
        if (isNotEmpty(fax)) fillFaxField(fax);
        // Fill Country dropdown
        if (isNotEmpty(countryDropdown)) fillCountrydropdown(countryDropdown);
        // Fill Address 1
        if (isNotEmpty(address1)) fillAddressField1(address1);
        // Fill address2
        if (isNotEmpty(address2)) fillAddressField2(address2);
        // Fill city
        if (isNotEmpty(city)) fillCityField(city);
        // Fill state (skip if null/empty to avoid Selenium errors)
        if (isNotEmpty(state)) {
            fillStateField(state);
        } else {
            System.out.println("State field is null or empty - skipping");
        }
        // Fill zipcode
        if (isNotEmpty(zipcode)) fillZipcodeField(zipcode);
        // Fill comments
        if (isNotEmpty(comments)) fillCommentsField(comments);
        // Note: Save button click is now handled separately in the step definition, not here
    }

    private boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }


    public void navigateToAdminGeneralModule() throws InterruptedException {
        WebDriver driver = Driver.getInstance();
        Thread.sleep(1500);
        System.out.println("Hovering over admin dropdown...");
        try {
            // Use SeleniumTestHelper.mouseHover to perform a robust hover
            SeleniumTestHelper.mouseHover("id", "admin");
            System.out.println("Successfully hovered over admin dropdown (via SeleniumTestHelper)");
            Thread.sleep(1500);
        } catch (Exception e) {
            System.out.println("Failed to hover over admin dropdown: " + e.getMessage());
        }
        System.out.println("Navigating to Company Info -> General");

        try {
            WebDriver actualDriver = driver;
            if (driver instanceof com.orangeHrm.utils.WebDriverDispatcher) {
                actualDriver = ((com.orangeHrm.utils.WebDriverDispatcher) driver).getUnderlyingDriver();
            }

            // Hover over Company Info
            System.out.println("Hovering over Company Info...");
            Actions actions = new Actions(actualDriver);
            actions.moveToElement(companyInfo).perform();
            System.out.println("Successfully hovered over Company Onfo");
            Thread.sleep(1000);

            // Click on General
            System.out.println("Clicking on General");
            actions.click(general).perform();
            System.out.println("Successfully clicked eGeneral");
            Thread.sleep(2000);
        } catch (Exception e) {
            System.out.println("Failed in navigation: " + e.getMessage());
        }
    }

    public class CompanyInfoPage {
        private final WebDriver driver;
        private final WebDriverWait wait;

        private static final By HEADER = By.xpath(
                "//h1[normalize-space()='Company Info : General' or normalize-space()='Company Info: General']"
        );

        public CompanyInfoPage(WebDriver driver) {
            this.driver = driver;
            this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        }

        public String getHeaderText() {
            WebElement el = wait.until(ExpectedConditions.visibilityOfElementLocated(HEADER));
            String txt = el.getText().trim();
            if (txt.isEmpty()) {
                txt = el.getAttribute("textContent").trim();
            }
            return txt.replace("\u00A0"," ").replaceAll("\\s*:\\s*", " : ").trim();
        }

        public boolean isLoaded() {
            try {
                return "Company Info : General".equals(getHeaderText());
            } catch (TimeoutException e) {
                return false;
            }
        }
    }

    public void clickOnEditButton() throws InterruptedException {
        try {
            WebDriver actualDriver = driver;
            if (driver instanceof com.orangeHrm.utils.WebDriverDispatcher) {
                actualDriver = ((com.orangeHrm.utils.WebDriverDispatcher) driver).getUnderlyingDriver();
            }
            // Click on Edit button
            System.out.println("clicking on editButton...");
            Actions actions = new Actions(actualDriver);
            actions.click(editButton).perform();
            System.out.println("Successfully clicked on Edit button");
            Thread.sleep(2000);
        } catch (Exception e) {
            System.out.println("Failed in click on edit button: " + e.getMessage());
        }
}

    private void fillCompanyName(String companyName) throws InterruptedException {
        System.out.println("Attempting to fill companyName field (txtCompanyName) with: " + companyName);
        try {
            System.out.println("Trying companyName (id='txtCompanyName')");
            SeleniumTestHelper.clear(companyNameFieldByxpath);
            SeleniumTestHelper.enterText(companyNameFieldByxpath, companyName);
            System.out.println("Successfully filled companyName field by id");
            return;
        } catch (Exception e) {
            System.out.println("Failed with companyNameFieldByxpath: " + e.getMessage());
        }
    }

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

    private void filltaxIdField(String taxID) throws InterruptedException {
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
            SeleniumTestHelper.selectFromDropDown(countryDropdown, country, "visibletext");
            System.out.println("Successfully filled country field");

        } catch (Exception e) {
            System.out.println("Failed with xpath search for country: " + e.getMessage());

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
    public void companyInfoClickSaveButton() throws InterruptedException {
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

