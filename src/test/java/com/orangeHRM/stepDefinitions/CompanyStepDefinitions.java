package com.orangeHrm.stepDefinitions;

import com.orangeHrm.pages.CompanyPageObjects;
import com.orangeHrm.utils.Driver;
import com.orangeHrm.utils.ExcelReader;
import com.orangeHrm.utils.SeleniumTestHelper;
import com.orangeHrm.utils.DatabaseVerificationUtils;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.After;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompanyStepDefinitions extends BaseStepDefinition {
    private CompanyPageObjects companyPageObjects;
    private TestData lastLoadedTestData;  // Store test data for verification in later steps


    private CompanyPageObjects getCompanyPageObjects() {
        if (companyPageObjects == null) {
            // Pass the driver to constructor to avoid duplicate getInstance() calls
            WebDriver driver = Driver.getInstance();
            companyPageObjects = new CompanyPageObjects(driver);
        }
        return companyPageObjects;
    }

    @When("I navigate to Admin → Company Info → General")
    public void i_navigate_to_admin_company_info_general() {
        try {
            getCompanyPageObjects().navigateToAdminGeneralModule();
            logReportMessage("Successfully navigated to admin general module");
        } catch (InterruptedException e) {
            logReportMessage("Failed to navigate to admin general module: " + e.getMessage());
            SeleniumTestHelper.markCurrentThreadInterrupted();
            throw new RuntimeException("Failed to navigate to admin general module", e);
        }
    }

    @And("I should see the following company information:")
    public void i_should_see_the_following_company_information(DataTable dataTable) {
        SeleniumTestHelper.switchToDefaultContent();
        SeleniumTestHelper.switchToFrame(getCompanyPageObjects().companyInfoFrame);

        Map<String, String> expectedFieldIds = dataTable.asMap(String.class, String.class);

        for (Map.Entry<String, String> entry : expectedFieldIds.entrySet()) {
            String field = entry.getKey().trim();
            String expectedId = entry.getValue().trim();

            // Header row may be parsed as data by some datatable variants
            if ("Field".
                    equalsIgnoreCase(field) && "FieldId".
                    equalsIgnoreCase(expectedId)) {
                continue;
            }

            String actualId = getCompanyInformationFieldId(field);
            SeleniumTestHelper.assertEquals(actualId, expectedId,
                    "Verify company field id for '" + field + "'");

            WebDriver driver = Driver.getInstance();
            WebElement element = driver.findElement(By.id(expectedId));
            SeleniumTestHelper.assertTrue(element.isDisplayed(),
                    "Element with id '" + expectedId + "' should be visible");
        }
    }

    private String getCompanyInformationFieldId(String fieldName) {
        return switch (fieldName) {
            case "Company Name" -> "txtCompanyName";
            case "Tax ID" -> "txtTaxID";
            case "NAICS" -> "txtNAICS";
            case "Phone" -> "txtPhone";
            case "Country" -> "cmbCountry";
            case "Address1" -> "txtStreet1";
            case "Address2" -> "txtStreet2";
            case "City" -> "cmbCity";
            case "State/Province" -> "txtState";
            case "ZIP Code" -> "txtZIP";
            case "Comments" -> "txtComments";
            default -> throw new IllegalArgumentException("Unsupported company information field: " + fieldName);
        };
    }

    @Then("the General company information page {string} should load")
    public void theGeneralCompanyInformationPageShouldLoad(String title) {
        SeleniumTestHelper.switchToFrame(getCompanyPageObjects().companyInfoFrame);
        SeleniumTestHelper.assertEquals(getCompanyPageObjects().companyHeader.getText(), title, " Page title is successfully verified");

    }

    @Then("I click the Edit button")
    public void i_click_the_edit_button() {
        try {
            getCompanyPageObjects().clickOnEditButton();
            logReportMessage("Successfully clicked the Edit button");
        } catch (InterruptedException e) {
            logReportMessage("Failed to click on edit button: " + e.getMessage());
            SeleniumTestHelper.markCurrentThreadInterrupted();
            throw new RuntimeException("Failed to click on edit button", e);
        }
    }
    @Then("I edit the company information details in the general section")
    public void editCompanyInformationDetailsInGeneralSection() {
        try {
            TestData testData = loadCompanyDataFromExcel("TC_General_001");
            validateLoadedData(testData);
            this.lastLoadedTestData = testData;  // Store for verification
            fillCompanyDetailsForm(testData);
            logReportMessage("Company information details edited successfully");
        } catch (IllegalArgumentException e) {
            logReportMessage("Invalid test data: " + e.getMessage());
            throw new RuntimeException("Failed to edit company information: " + e.getMessage(), e);
        } catch (Exception e) {
            logReportMessage("Failed to edit company information details: " + e.getMessage());
            throw new RuntimeException("Failed to edit company information details", e);
        }
    }

    private void fillCompanyDetailsForm(TestData testData) throws InterruptedException {
        getCompanyPageObjects().enterCompanyDetails(
            testData.companyName,
            testData.taxId,
            testData.naics,
            testData.phone,
            testData.fax,
            testData.country,
            testData.address1,
            testData.address2,
            testData.city,
            testData.state,
            testData.zipcode,
            testData.comments
        );
    }

    private void validateLoadedData(TestData testData) {
        if (testData == null) {
            throw new IllegalArgumentException("Test data could not be loaded from Excel");
        }
        if (testData.companyName == null || testData.companyName.trim().isEmpty()) {
            throw new IllegalArgumentException("Company Name is required and cannot be empty");
        }
        
        // Log any null/empty fields for debugging
        logNullOrEmptyFields(testData);
    }

    private void logNullOrEmptyFields(TestData testData) {
        if (testData.taxId == null || testData.taxId.trim().isEmpty()) {
            System.out.println("WARNING: Tax ID is null or empty");
        }
        if (testData.state == null || testData.state.trim().isEmpty()) {
            System.out.println("WARNING: State field is null or empty - it will be skipped");
        }
        if (testData.comments == null || testData.comments.trim().isEmpty()) {
            System.out.println("WARNING: Comments field is null or empty");
        }
    }

    private TestData loadCompanyDataFromExcel(String testCaseId) {
        try {
            if (!testCaseExists(testCaseId)) {
                System.out.println("Test case " + testCaseId + " not found in Excel");
                return null;
            }

            ExcelReader excelReader = new ExcelReader();
            excelReader.setReadingSheet("Admin_General");

            TestData data = new TestData();
            data.testCaseId = testCaseId;
            data.companyName = excelReader.getData(testCaseId, "Company_Name");
            data.taxId = excelReader.getData(testCaseId, "Tax_ID");
            data.naics = excelReader.getData(testCaseId, "NAICS");
            data.phone = excelReader.getData(testCaseId, "Phone");
            data.fax = excelReader.getData(testCaseId, "Fax");
            data.country = excelReader.getData(testCaseId, "Country");
            data.address1 = excelReader.getData(testCaseId, "Address1");
            data.address2 = excelReader.getData(testCaseId, "Address2");
            data.city = excelReader.getData(testCaseId, "City");
            data.state = loadStateFromExcel(excelReader, testCaseId);
            data.zipcode = excelReader.getData(testCaseId, "ZIP_Code");
            data.comments = excelReader.getData(testCaseId, "Comments");
            data.expectedResult = excelReader.getData(testCaseId, "ExpectedResult");

            System.out.println("Loaded test data for " + testCaseId + ": companyName=" + data.companyName);
            return data;

        } catch (Exception e) {
            System.out.println("Error loading test data from Excel: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Load state field with fallback options for different column name variations.
     * Tries: "State ", "State", "State/Province", "StateProvince" in order.
     * Note: Excel column has trailing space "State "
     */
    private String loadStateFromExcel(ExcelReader excelReader, String testCaseId) {
        String[] stateColumnNames = {"State ", "State", "State/Province", "StateProvince", "State_Province"};
        
        // Log all available columns for debugging
        java.util.List<String> allHeaders = excelReader.getAllColumnHeaders();
        System.out.println("Available Excel columns: " + allHeaders);
        
        for (String columnName : stateColumnNames) {
            try {
                String value = excelReader.getData(testCaseId, columnName);
                if (value != null && !value.trim().isEmpty()) {
                    System.out.println("Loaded State from column: '" + columnName + "' = " + value);
                    return value;
                }
            } catch (Exception e) {
                // Try next column name
            }
        }
        System.out.println("WARNING: State field not found in Excel under any expected column name");
        return null;
    }

    @Then("I click the Save button")
    public void i_click_the_save_button() {
        try {
            // Use the page object's method which has fallback logic for multiple locators
            getCompanyPageObjects().companyInfoClickSaveButton();
            logReportMessage("Successfully clicked the Save button");
        } catch (Exception e) {
            logReportMessage("Failed to click Save button: " + e.getMessage());
            throw new RuntimeException("Failed to click the Save button", e);
        }
    }

    @Then("the updated information should be reflected in the form")
    public void the_updated_information_should_be_reflected_in_the_form() {
        if (lastLoadedTestData == null) {
            throw new IllegalStateException("No test data available for verification. Ensure 'I edit the company information details' step was executed first.");
        }

        try {
            // No frame switching needed - rightMenu is a div, not an iframe

            // Verify each field against the stored test data
            verifyFormField("Company Name", lastLoadedTestData.companyName, "txtCompanyName");
            verifyFormField("Tax ID", lastLoadedTestData.taxId, "txtTaxID");
            verifyFormField("NAICS", lastLoadedTestData.naics, "txtNAICS");
            verifyFormField("Phone", lastLoadedTestData.phone, "txtPhone");
            verifyFormField("Fax", lastLoadedTestData.fax, "txtFax");
            verifyCountryField("Country", lastLoadedTestData.country, "cmbCountry");
            verifyFormField("Address1", lastLoadedTestData.address1, "txtStreet1");
            verifyFormField("Address2", lastLoadedTestData.address2, "txtStreet2");
            verifyFormField("City", lastLoadedTestData.city, "cmbCity");
            verifyFormField("State/Province", lastLoadedTestData.state, "txtState");
            verifyFormField("ZIP Code", lastLoadedTestData.zipcode, "txtZIP");
            verifyFormField("Comments", lastLoadedTestData.comments, "txtComments");

            logReportMessage("All company information fields verified successfully");
        } catch (Exception e) {
            logReportMessage("Failed to verify updated information: " + e.getMessage());
            throw new RuntimeException("Verification of updated information failed", e);
        }
    }

    private void verifyFormField(String fieldName, String expectedValue, String fieldId) {
        // Skip verification for null/empty fields (they were skipped during fill)
        if (expectedValue == null || expectedValue.trim().isEmpty()) {
            System.out.println("Skipping verification for " + fieldName + " (value is null/empty)");
            return;
        }

        WebElement element = driver.findElement(By.id(fieldId));
        String actualValue = element.getAttribute("value").trim();
        String expectedValueTrimmed = expectedValue.trim();  // Trim expected value too for consistent comparison

        SeleniumTestHelper.assertEquals(actualValue, expectedValueTrimmed,
                "Verify " + fieldName + " field is reflected correctly in the form");
        System.out.println("✓ " + fieldName + " verified: " + actualValue);
    }

    private void verifyCountryField(String fieldName, String expectedValue, String fieldId) {
        // Skip verification for null/empty country
        if (expectedValue == null || expectedValue.trim().isEmpty()) {
            System.out.println("Skipping verification for " + fieldName + " (value is null/empty)");
            return;
        }

        try {
            driver = Driver.getInstance();
            WebElement countryDropdown = driver.findElement(By.id(fieldId));
            
            String actualValue = null;
            try {
                // Try using Select to get the selected option
                Select select = new Select(countryDropdown);
                actualValue = select.getFirstSelectedOption().getText().trim();
            } catch (UnsupportedOperationException e) {
                // If Select fails (getDomAttribute not supported), get selected option text directly
                List<WebElement> options = countryDropdown.findElements(By.tagName("option"));
                for (WebElement option : options) {
                    if ("selected".equals(option.getAttribute("selected")) || option.isSelected()) {
                        actualValue = option.getText().trim();
                        break;
                    }
                }
            }

            if (actualValue == null) {
                actualValue = countryDropdown.getAttribute("value");
            }

            SeleniumTestHelper.assertEquals(actualValue, expectedValue,
                    "Verify " + fieldName + " dropdown is reflected correctly in the form");
            System.out.println("✓ " + fieldName + " verified: " + actualValue);
        } catch (Exception e) {
            throw new RuntimeException("Failed to verify country field: " + e.getMessage(), e);
        }
    }

    private boolean testCaseExists(String testCaseId) {
        try {
            ExcelReader excelReader = new ExcelReader();
            excelReader.setReadingSheet("Admin_General");
            String companyName = excelReader.getData(testCaseId, "Company_Name");
            return companyName != null && !companyName.trim().isEmpty();
        } catch (Exception e) {
            System.out.println("Error checking test case existence: " + e.getMessage());
            return false;
        }
    }

    /**
     * Inner class to encapsulate company test data loaded from Excel.
     * Follows the Data Transfer Object (DTO) pattern for clean data handling.
     */
    /**
     * Database verification step definitions
     */
    private DatabaseVerificationUtils dbVerification;

    /**
     * Initialize database connection before verification
     */
    @When("I connect to the OrangeHRM database")
    public void i_connect_to_the_orangehrm_database() {
        try {
            if (dbVerification == null) {
                dbVerification = new DatabaseVerificationUtils();
            }
            dbVerification.connect();
            logReportMessage("Successfully connected to OrangeHRM database");
        } catch (Exception e) {
            logReportMessage("Failed to connect to database: " + e.getMessage());
            throw new RuntimeException("Database connection failed", e);
        }
    }

    /**
     * Verify company information exists in database
     */
    @Then("the company information should be saved in the database")
    public void the_company_information_should_be_saved_in_the_database() {
        if (dbVerification == null || !dbVerification.isConnected()) {
            throw new RuntimeException("Database connection not established. Execute 'I connect to the OrangeHRM database' step first.");
        }

        if (lastLoadedTestData == null) {
            throw new IllegalStateException("No test data available for verification");
        }

        try {
            boolean companyExists = dbVerification.companyExists(lastLoadedTestData.companyName);
            if (!companyExists) {
                throw new RuntimeException("Company '" + lastLoadedTestData.companyName + "' not found in database");
            }
            logReportMessage("✓ Company '" + lastLoadedTestData.companyName + "' found in database");
        } catch (Exception e) {
            logReportMessage("Failed to verify company in database: " + e.getMessage());
            throw new RuntimeException("Database verification failed", e);
        }
    }

    /**
     * Verify all company fields in database
     */
    @Then("the company details should match the database records")
    public void the_company_details_should_match_the_database_records() {
        if (dbVerification == null || !dbVerification.isConnected()) {
            throw new RuntimeException("Database connection not established. Execute 'I connect to the OrangeHRM database' step first.");
        }

        if (lastLoadedTestData == null) {
            throw new IllegalStateException("No test data available for verification");
        }

        try {
            // Map UI field names to database column names
            Map<String, String> expectedFields = new HashMap<>();
            if (lastLoadedTestData.companyName != null && !lastLoadedTestData.companyName.trim().isEmpty()) {
                expectedFields.put("company_name", lastLoadedTestData.companyName);
            }
            if (lastLoadedTestData.taxId != null && !lastLoadedTestData.taxId.trim().isEmpty()) {
                expectedFields.put("company_tax_num", lastLoadedTestData.taxId);
            }
            if (lastLoadedTestData.phone != null && !lastLoadedTestData.phone.trim().isEmpty()) {
                expectedFields.put("phone", lastLoadedTestData.phone);
            }
            if (lastLoadedTestData.fax != null && !lastLoadedTestData.fax.trim().isEmpty()) {
                expectedFields.put("fax", lastLoadedTestData.fax);
            }
            if (lastLoadedTestData.address1 != null && !lastLoadedTestData.address1.trim().isEmpty()) {
                expectedFields.put("address1", lastLoadedTestData.address1.trim());
            }
            if (lastLoadedTestData.address2 != null && !lastLoadedTestData.address2.trim().isEmpty()) {
                expectedFields.put("address2", lastLoadedTestData.address2);
            }
            if (lastLoadedTestData.city != null && !lastLoadedTestData.city.trim().isEmpty()) {
                expectedFields.put("city", lastLoadedTestData.city);
            }
            if (lastLoadedTestData.zipcode != null && !lastLoadedTestData.zipcode.trim().isEmpty()) {
                expectedFields.put("zipcode", lastLoadedTestData.zipcode);
            }
            if (lastLoadedTestData.comments != null && !lastLoadedTestData.comments.trim().isEmpty()) {
                expectedFields.put("notes", lastLoadedTestData.comments);
            }

            // Verify all fields
            Map<String, Boolean> results = dbVerification.verifyCompanyFields(
                    lastLoadedTestData.companyName,
                    expectedFields
            );

            // Check if all verifications passed
            boolean allPassed = results.values().stream().allMatch(v -> v);
            if (!allPassed) {
                long failedCount = results.values().stream().filter(v -> !v).count();
                throw new RuntimeException(failedCount + " field(s) did not match database records");
            }

            logReportMessage("✓ All company details verified successfully in database");
        } catch (Exception e) {
            logReportMessage("Failed to verify company details in database: " + e.getMessage());
            throw new RuntimeException("Database field verification failed", e);
        }
    }

    @Then("verify the company information details in database")
    public void verifyTheCompanyInformationDetailsInDatabase() {
        // Ensure database connection is available
        try {
            if (dbVerification == null) {
                dbVerification = new DatabaseVerificationUtils();
            }
            if (!dbVerification.isConnected()) {
                dbVerification.connect();
            }
        } catch (Exception e) {
            throw new RuntimeException("Database connection failed during verification: " + e.getMessage(), e);
        }

        if (lastLoadedTestData == null) {
            throw new IllegalStateException("No test data available for verification. Execute the edit step before database verification.");
        }

        the_company_information_should_be_saved_in_the_database();
        the_company_details_should_match_the_database_records();
        logReportMessage("✓ Company information and details verified in database");
    }

    /**
     * Verify specific company field in database
     */
    @Then("the {string} field should contain {string} in the database")
    public void the_field_should_contain_in_the_database(String fieldName, String expectedValue) {
        if (dbVerification == null || !dbVerification.isConnected()) {
            throw new RuntimeException("Database connection not established. Execute 'I connect to the OrangeHRM database' step first.");
        }

        if (lastLoadedTestData == null) {
            throw new IllegalStateException("No test data available for verification");
        }

        try {
            // Map UI field names to database column names
            String dbFieldName = mapUIFieldToDBField(fieldName);

            boolean verified = dbVerification.verifyCompanyField(
                    lastLoadedTestData.companyName,
                    dbFieldName,
                    expectedValue
            );

            if (!verified) {
                throw new RuntimeException("Field '" + fieldName + "' verification failed in database");
            }

            logReportMessage("✓ Field '" + fieldName + "' verified: " + expectedValue);
        } catch (Exception e) {
            logReportMessage("Failed to verify field in database: " + e.getMessage());
            throw new RuntimeException("Database field verification failed", e);
        }
    }

    /**
     * Close database connection after verification
     */
    @After("@database")
    public void close_database_connection() {
        if (dbVerification != null) {
            dbVerification.disconnect();
            System.out.println("Database connection closed after scenario");
        }
    }

    /**
     * Map UI field names to database column names
     */
    private String mapUIFieldToDBField(String uiFieldName) {
        return switch (uiFieldName.toLowerCase()) {
            case "company name" -> "company_name";
            case "tax id", "company tax" -> "company_tax_num";
            case "phone" -> "phone";
            case "fax" -> "fax";
            case "address1", "address 1" -> "address1";
            case "address2", "address 2" -> "address2";
            case "city" -> "city";
            case "state", "province", "state/province" -> "province_code";
            case "country" -> "country_code";
            case "zip code", "zipcode" -> "zipcode";
            case "comments", "notes" -> "notes";
            default -> uiFieldName.toLowerCase();
        };
    }

    public static class TestData {
        public String testCaseId;
        public String companyName;
        public String taxId;
        public String naics;
        public String phone;
        public String fax;
        public String country;
        public String address1;
        public String address2;
        public String city;
        public String state;
        public String zipcode;
        public String comments;
        public String expectedResult;

        @Override
        public String toString() {
            return "TestData{" +
                    "testCaseId='" + testCaseId + '\'' +
                    ", companyName='" + companyName + '\'' +
                    ", taxId='" + taxId + '\'' +
                    ", naics='" + naics + '\'' +
                    ", phone='" + phone + '\'' +
                    ", fax='" + fax + '\'' +
                    ", country='" + country + '\'' +
                    ", address1='" + address1 + '\'' +
                    ", address2='" + address2 + '\'' +
                    ", city='" + city + '\'' +
                    ", state='" + state + '\'' +
                    ", zipcode='" + zipcode + '\'' +
                    ", comments='" + comments + '\'' +
                    ", expectedResult='" + expectedResult + '\'' +
                    '}';
        }
    }

}

