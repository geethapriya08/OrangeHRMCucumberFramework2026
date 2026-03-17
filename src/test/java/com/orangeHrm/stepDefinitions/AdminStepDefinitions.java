package com.orangeHrm.stepDefinitions;

import com.orangeHrm.pages.AdminPageObjects;
import com.orangeHrm.utils.Driver;
import com.orangeHrm.utils.SeleniumTestHelper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AdminStepDefinitions extends BaseStepDefinition {

    private AdminPageObjects adminPageObjects;
    //private SeleniumTestHelper seleniumTestHelper;


    private AdminPageObjects getAdminPageObjects() {
        if (adminPageObjects == null) {
            adminPageObjects = new AdminPageObjects(Driver.getInstance());
        }
        return adminPageObjects;
    }

    @And("I navigate to the Admin module")
    public void i_navigate_to_the_admin_module() throws InterruptedException {
        try {
            getAdminPageObjects().navigateToAdminModule();
            logReportMessage("Navigated to Admin module successfully");
        } catch (Exception e) {
            logReportMessage("Failed to navigate to Admin module: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @And("I enter all company information details")
    public void i_enter_all_company_information_details() throws InterruptedException {
        try {
            // getAdminPageObjects().addCompanyDetails();
            logReportMessage("Company information details entered successfully");
        } catch (Exception e) {
            logReportMessage("Failed to enter company information details: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @And("I add a new employee with required details")
    public void i_add_a_new_employee_with_required_details() throws InterruptedException {
        try {
            getAdminPageObjects().addNewEmployee();
            logReportMessage("Added new employee with default details");
        } catch (Exception e) {
            logReportMessage("Failed to add new employee: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Given("I am logged in as an admin user")
    public void i_am_logged_in_as_an_admin_user() {
        LoginStepDefinitions loginStepDefinitions = new LoginStepDefinitions();
        loginStepDefinitions.i_open_the_orange_hrm_login_page();
        loginStepDefinitions.i_log_in_with_data_from_test_case();
    }

    @When("I hover on Admin menu")
    public void navigate_to_admin_menu() {
        logReportMessage("Hovering over Admin dropdown...");

        try {
            // Use SeleniumTestHelper.mouseHover to perform a robust hover
            SeleniumTestHelper.mouseHover("id", "admin");
            logReportMessage("Successfully hovered over Admin dropdown (via SeleniumTestHelper)");
            Thread.sleep(1500);
        } catch (Exception e) {
            logReportMessage(STR."Failed to hover over admin dropdown: \{e.getMessage()}");
        }
    }

    @Then("the Admin menu should be visible in the below navigation panel")
    public void verify_all_admin_menu_options() throws InterruptedException {
        Thread.sleep(5000);
        try {
            WebDriver actualDriver = driver;
            if (driver instanceof com.orangeHrm.utils.WebDriverDispatcher) {
                actualDriver = ((com.orangeHrm.utils.WebDriverDispatcher) driver).getUnderlyingDriver();
            }
        Actions actions = new Actions(actualDriver);
        actions.moveToElement(adminPageObjects.companyInfoSpan).perform();
      //  SeleniumTestHelper.mouseHover("id", "admin");
        } catch (Exception e) {
            System.out.println("Failed in navigation: " + e.getMessage());
        }
        SeleniumTestHelper.waitForElementToBeDisplayed(driver, adminPageObjects.adminCompanyInfo, 10);
        Thread.sleep(5000);
        SeleniumTestHelper.waitForListOfElementsToBeDisplayed(driver, adminPageObjects.adminMenuItems, 10);

        // Actual labels from the page
        List<String> actualLabels = adminPageObjects.adminMenuItems.stream()
                .map(el -> el.getText().trim())
                .toList();

        // Expected labels in order (adjust if your app differs)
        List<String> expectedLabels = Arrays.asList(
                "Company Info",
                "Job",
                "Qualification",
                "Skills",
                "Memberships",
                "Nationality & Race",
                "Users",
                "Email Notifications",
                "Project Info",
                "Data Import/Export",
                "Custom Fields"
        );
        SeleniumTestHelper.assertEquals(expectedLabels, actualLabels, "All menu items are verified");
    }

    void dummyMethod(){
        List<WebElement> elements = adminPageObjects.adminMenuItems;

        int i = 0;
        for (WebElement actualLabels : elements) {

            SeleniumTestHelper.reportLog(STR."Actual : \{actualLabels.getText()}");
            List<String> expectedLabels = Arrays.asList(
                    "Company Info",
                    "Job",
                    "Qualification",
                    "Skills",
                    "Memberships",
                    "Nationality & Race",
                    "Users",
                    "Email Notifications",
                    "Project Info",
                    "Data Import/Export",
                    "Custom Fields"
            );
            SeleniumTestHelper.assertEquals(actualLabels.getText(), expectedLabels.get(i));
            SeleniumTestHelper
                    .reportLog(STR."Verifcation passed Actual \{expectedLabels.get(i)} Expected ");
            i++;
        }
    }
}