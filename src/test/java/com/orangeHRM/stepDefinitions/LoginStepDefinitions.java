package com.orangeHRM.stepDefinitions;

import org.testng.Assert;

import com.orangeHrm.utils.ExcelReader;
import com.orangeHrm.pages.BaseOrangeHRMLoginPageObjects;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

public class LoginStepDefinitions extends BaseStepDefinition {

    private BaseOrangeHRMLoginPageObjects loginPageObjects;
    private TestData testData;

    public LoginStepDefinitions() {
        this.loginPageObjects = new BaseOrangeHRMLoginPageObjects(driver);
    }

    @When("I log in with data from test case {string}")
    public void i_log_in_with_test_case_data(String testCaseId) {
        try {
            testData = loadTestDataFromExcel(testCaseId);
            
            if (testData == null) {
                throw new Exception("Test case " + testCaseId + " not found in Excel file");
            }

            System.out.println("Logging in with " + testCaseId + ": " + testData);
            loginPageObjects.login(testData.username, testData.password);
            System.out.println("Login successful for test case: " + testCaseId);

        } catch (Exception e) {
            throw new RuntimeException("Failed to execute login with test case " + testCaseId + ": " + e.getMessage(), e);
        }
    }

    @Given("I navigate to OrangeHRM login page")
    public void i_navigate_to_orangehrm_login_page() {
        try {
            loginPageObjects.open("https://opensource-demo.orangehrm.com/web/index.php/auth/login");
            System.out.println("Navigated to OrangeHRM login page");
        } catch (Exception e) {
            throw new RuntimeException("Failed to navigate to login page: " + e.getMessage());
        }
    }

    @Then("I should be logged in successfully")
    public void i_should_be_logged_in_successfully() {
        try {
            Thread.sleep(2000);
            String currentUrl = loginPageObjects.getDriver().getCurrentUrl();
            System.out.println("Current URL after login: " + currentUrl);
            
            boolean isLoggedIn = !currentUrl.contains("auth/login");
            Assert.assertTrue(isLoggedIn, "User is still on login page - login may have failed");
            System.out.println("User successfully logged in");
        } catch (Exception e) {
            throw new RuntimeException("Failed to verify successful login: " + e.getMessage());
        }
    }

    private TestData loadTestDataFromExcel(String testCaseId) {
        try {
            ExcelReader excelReader = new ExcelReader();
            excelReader.setReadingSheet("LoginData");
            
            if (!testCaseExists(testCaseId, excelReader)) {
                System.out.println("Test case " + testCaseId + " not found in Excel");
                return null;
            }

            String username = excelReader.getData(testCaseId, "Username");
            String password = excelReader.getData(testCaseId, "Password");
            String expectedResult = excelReader.getData(testCaseId, "ExpectedResult");
            
            if (username != null && password != null) {
                TestData data = new TestData();
                data.testCaseId = testCaseId;
                data.username = username;
                data.password = password;
                data.expectedResult = expectedResult != null ? expectedResult : "Login Successful";
                
                System.out.println("Loaded test data for " + testCaseId + ": Username=" + data.username);
                return data;
            }

        } catch (Exception e) {
            System.out.println("Error loading test data from Excel: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    private boolean testCaseExists(String testCaseId, ExcelReader excelReader) {
        try {
            String username = excelReader.getData(testCaseId, "Username");
            return username != null && !username.isEmpty();
        } catch (Exception e) {
            System.out.println("Error checking test case existence: " + e.getMessage());
            return false;
        }
    }

    private static class TestData {
        String testCaseId;
        String username;
        String password;
        String expectedResult;

        @Override
        public String toString() {
            return "TestData{" +
                    "testCaseId='" + testCaseId + '\'' +
                    ", username='" + username + '\'' +
                    ", password='" + password + '\'' +
                    ", expectedResult='" + expectedResult + '\'' +
                    '}';
        }
    }
}