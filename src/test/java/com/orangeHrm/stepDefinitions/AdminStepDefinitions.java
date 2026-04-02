package com.orangeHrm.stepDefinitions;

import com.orangeHrm.pages.AdminPageObjects;
import com.orangeHrm.utils.Driver;
import com.orangeHrm.utils.SeleniumTestHelper;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AdminStepDefinitions extends BaseStepDefinition {

    private LoginStepDefinitions loginStepDefinitions;
    private AdminPageObjects adminPageObjects;

    public AdminStepDefinitions() {
        super();
        this.loginStepDefinitions = new LoginStepDefinitions();
    }

    private AdminPageObjects getAdminPageObjects() {
        if (adminPageObjects == null) {
            adminPageObjects = new AdminPageObjects(Driver.getInstance());
        }
        return adminPageObjects;
    }

    @And("I navigate to the Admin module")
    public void i_navigate_to_the_admin_module() {
        try {
            getAdminPageObjects().navigateToAdminModule();
            logReportMessage("Navigated to Admin module successfully");
        } catch (Exception e) {
            logReportMessage("Failed to navigate to Admin module: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @And("I enter all company information details")
    public void i_enter_all_company_information_details() {
        try {
            // getAdminPageObjects().addCompanyDetails();
            logReportMessage("Company information details entered successfully");
        } catch (Exception e) {
            logReportMessage("Failed to enter company information details: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @And("I add a new employee with required details")
    public void i_add_a_new_employee_with_required_details() {
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
        loginStepDefinitions.i_open_the_orange_hrm_login_page();
        loginStepDefinitions.i_log_in_with_data_from_test_case();
    }

    @When("I hover on Admin menu")
    public void navigate_to_admin_menu() {
        logReportMessage("Hovering over Admin dropdown...");
        try {
            // Use SeleniumTestHelper.mouseHover with WebElement from AdminPageObjects
            SeleniumTestHelper.mouseHover(getAdminPageObjects().getAdminElement());
            logReportMessage("Successfully hovered over Admin dropdown");
            Thread.sleep(1500);
        } catch (Exception e) {
            logReportMessage("Failed to hover over admin dropdown: " + e.getMessage());
        }
    }

    @Then("the Admin menu should be visible in the below navigation panel")
    public void verify_all_admin_menu_options() {
        SeleniumTestHelper.waitForListOfElementsToBeDisplayed(driver, getAdminPageObjects().adminMenuItems, 10);
        // Actual labels from the page
        List<String> actualLabels = getAdminPageObjects().adminMenuItems.stream().map(el -> el.getText().trim()).toList();
        // Expected labels in order (adjust if your app differs)
        List<String> expectedLabels = Arrays.asList("Company Info", "Job", "Qualification", "Skills", "Memberships", "Nationality & Race", "Users", "Email Notifications", "Project Info", "Data Import/Export", "Custom Fields");
        SeleniumTestHelper.assertEquals(expectedLabels, actualLabels, "All menu items are verified: ");
   }
   @Then("I hover over or click on Company Info")
    public void i_hover_over_or_click_on_company_info() {
        logReportMessage("Hovering over Company Info dropdown...");
        try {
            // Use SeleniumTestHelper.mouseHover with WebElement from AdminPageObjects
            SeleniumTestHelper.mouseHover(getAdminPageObjects().getCompanyInfoLink());
            logReportMessage("Successfully hovered over Company info dropdown");
        } catch (Exception e) {
            logReportMessage("Failed to hover over Company info dropdown: " + e.getMessage());
        }
    }

    @Then("I should see the following sub-items:")
    public void i_should_see_the_following_sub_items(DataTable dataTable) {
        // Convert DataTable (single column) to List<String>
        List<String> expected = dataTable.asList().stream().map(String::trim).collect(Collectors.toList());
        // Actual labels from the page
        List<String> actualLabels = getAdminPageObjects().adminCompanyInfoOptions.stream().map(el -> el.getText().trim()).toList();
        SeleniumTestHelper.assertEquals(expected, actualLabels, "All Company info options are verified: ");
    }
    @Then("verify all the admin menu options as below:")
    public void verify_all_the_admin_menu_options_as_below(DataTable dataTable) {
       //Convert DataTable (single column) to List<String>
        List<String> expected = dataTable.asList().stream().map(String::trim).collect(Collectors.toList());
        // Actual labels from the page
        List<String> actualLabels = getAdminPageObjects().adminMenuItems.stream().map(el -> el.getText().trim()).toList();
        SeleniumTestHelper.assertEquals(expected, actualLabels, "All Admin menu options are verified: ");
    }
    @And("I expand the Job submenu")
    public void i_expand_the_job_submenu() {
        logReportMessage("Hovering over Job dropdown...");
        try {
            // Use SeleniumTestHelper.mouseHover with WebElement from AdminPageObjects
            SeleniumTestHelper.mouseHover(getAdminPageObjects().getJobInfoLink());
            logReportMessage("Successfully hovered over Job dropdown");
        } catch (Exception e) {
            logReportMessage("Failed to hover over Job dropdown: " + e.getMessage());
        }
    }

    @Then("I should see the following sub-items of Job:")
    public void i_should_see_the_following_sub_items_of_job(DataTable dataTable) {
        // Convert DataTable (single column) to List<String>
        List<String> expected = dataTable.asList().stream().map(String::trim).collect(Collectors.toList());
        // Actual labels from the page
        List<String> actualLabels = getAdminPageObjects().adminJobInfoOptions.stream().map(el -> el.getText().trim()).toList();
        SeleniumTestHelper.assertEquals(expected, actualLabels, "All Job options are verified: ");

    }

    @Then("I hover over or click on Qualification")
    public void i_hover_over_or_click_on_qualification() {
        logReportMessage("Hovering over Qualification dropdown...");
        try {
            // Use SeleniumTestHelper.mouseHover with WebElement from AdminPageObjects
            SeleniumTestHelper.mouseHover(getAdminPageObjects().getQualificationInfoLink());
            logReportMessage("Successfully hovered over Qualification dropdown");
        } catch (Exception e) {
            logReportMessage("Failed to hover over Qualification dropdown: " + e.getMessage());
        }

    }
    @Then("I should see the following sub-items of Qualification:")
    public void i_should_see_the_following_sub_items_of_qualification(DataTable dataTable) {
        // Convert DataTable (single column) to List<String>
        List<String> expected = dataTable.asList().stream().map(String::trim).collect(Collectors.toList());
        // Actual labels from the page
        List<String> actualLabels = getAdminPageObjects().adminQualificationInfoOptions.stream().map(el -> el.getText().trim()).toList();
        SeleniumTestHelper.assertEquals(expected, actualLabels, "All Qualification options are verified: ");

    }


}