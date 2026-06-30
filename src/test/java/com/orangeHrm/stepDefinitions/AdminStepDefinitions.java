package com.orangeHrm.stepDefinitions;

import com.orangeHrm.pages.AdminPageObjects;
import com.orangeHrm.utils.Driver;
import com.orangeHrm.utils.SeleniumTestHelper;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

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
	@Then("I should see the following sub-items of Qualification:")
	public void i_should_see_the_following_sub_items_of_qualification(DataTable dataTable) {
		// Convert DataTable (single column) to List<String>
		List<String> expected = dataTable.asList().stream().map(String::trim).collect(Collectors.toList());
		// Actual labels from the page
		List<String> actualLabels = getAdminPageObjects().adminQualificationInfoOptions.stream().map(el -> el.getText().trim()).toList();
		SeleniumTestHelper.assertEquals(expected, actualLabels, "All Qualification options are verified: ");

	}
    @And("I hover over or click on Qualification")
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

    @Then("I should see the following sub-items of Nationality & Race:")
	public void i_should_see_the_following_sub_items_of_nationality_race(DataTable dataTable) {
		// Convert DataTable (single column) to List<String>
	    List<String> expected = dataTable.asList().stream().map(String::trim).collect(Collectors.toList());
	    // Actual labels from the page
	    List<String> actualLabels = getAdminPageObjects().adminNationalityRaceInfoOptions.stream().map(el -> el.getText().trim()).toList();
	    SeleniumTestHelper.assertEquals(expected, actualLabels, "All Nationality & Race options are verified: ");
	    
	}

	@And("I hover over or click on Nationality & Race")
	public void i_hover_over_or_click_on_nationality_race() {
		logReportMessage("Hovering over Nationality & Race dropdown...");
	    try {
	        // Use SeleniumTestHelper.mouseHover with WebElement from AdminPageObjects
	        SeleniumTestHelper.mouseHover(getAdminPageObjects().getNationalityRaceInfoLink());
	        logReportMessage("Successfully hovered over Nationality & Race dropdown");
	    } catch (Exception e) {
	        logReportMessage("Failed to hover over QualifiNationality & Racecation dropdown: " + e.getMessage());
	    }
	
	    
	}

	@Then("I should see the following sub-items of Memberships:")
	public void i_should_see_the_following_sub_items_of_memberships(DataTable dataTable) {
		// Convert DataTable (single column) to List<String>
	    List<String> expected = dataTable.asList().stream().map(String::trim).collect(Collectors.toList());
	    // Actual labels from the page
	    List<String> actualLabels = getAdminPageObjects().adminMembershipsInfoOptions.stream().map(el -> el.getText().trim()).toList();
	    SeleniumTestHelper.assertEquals(expected, actualLabels, "All Memberships options are verified: ");
	    
	}

	@And("I hover over or click on Memberships")
	public void i_hover_over_or_click_on_memberships() {
		logReportMessage("Hovering over Memberships dropdown...");
	    try {
	        // Use SeleniumTestHelper.mouseHover with WebElement from AdminPageObjects
	        SeleniumTestHelper.mouseHover(getAdminPageObjects().getMembershipInfoLink());
	        logReportMessage("Successfully hovered over Memberships dropdown");
	    } catch (Exception e) {
	        logReportMessage("Failed to hover over Memberships dropdown: " + e.getMessage());
	    }
	
	    
	}

	@Then("I should see the following sub-items of Skills:")
	public void i_should_see_the_following_sub_items_of_skills(DataTable dataTable) {
		// Convert DataTable (single column) to List<String>
	    List<String> expected = dataTable.asList().stream().map(String::trim).collect(Collectors.toList());
	    // Actual labels from the page
	    List<String> actualLabels = getAdminPageObjects().adminSkillsInfoOptions.stream().map(el -> el.getText().trim()).toList();
	    SeleniumTestHelper.assertEquals(expected, actualLabels, "All Skills options are verified: ");
	    
	}

	@And("I hover over or click on Skills")
	public void i_hover_over_or_click_on_skills() {
		logReportMessage("Hovering over Skills dropdown...");
	    try {
	        // Use SeleniumTestHelper.mouseHover with WebElement from AdminPageObjects
	        SeleniumTestHelper.mouseHover(getAdminPageObjects().getSkillsInfoLink());
	        logReportMessage("Successfully hovered over Skills dropdown");
	    } catch (Exception e) {
	        logReportMessage("Failed to hover over Skills dropdown: " + e.getMessage());
	    }
	
	    
	}

	@And("I hover over or click on Users")
	public void i_hover_over_or_click_on_users() {
		logReportMessage("Hovering over Users dropdown...");
	    try {
	        // Use SeleniumTestHelper.mouseHover with WebElement from AdminPageObjects
	        SeleniumTestHelper.mouseHover(getAdminPageObjects().getUsersInfoLink());
	        logReportMessage("Successfully hovered over Users dropdown");
	    } catch (Exception e) {
	        logReportMessage("Failed to hover over Users dropdown: " + e.getMessage());
	    }
	
	    
	}

	@Then("I should see the following sub-items of Users:")
	public void i_should_see_the_following_sub_items_of_users(DataTable dataTable) {
		// Convert DataTable (single column) to List<String>
	    List<String> expected = dataTable.asList().stream().map(String::trim).collect(Collectors.toList());
	    // Actual labels from the page
	    List<String> actualLabels = getAdminPageObjects().adminUsersInfoOptions.stream().map(el -> el.getText().trim()).toList();
	    SeleniumTestHelper.assertEquals(expected, actualLabels, "All Users options are verified: ");
	}

	@And("I hover over or click on Email Notifications")
	public void i_hover_over_or_click_on_email_notifications() {
		logReportMessage("Hovering over Email Notifications dropdown...");
	    try {
	        // Use SeleniumTestHelper.mouseHover with WebElement from AdminPageObjects
	        SeleniumTestHelper.mouseHover(getAdminPageObjects().getEmailNotificationsInfoLink());
	        logReportMessage("Successfully hovered over Email Notifications dropdown");
	    } catch (Exception e) {
	        logReportMessage("Failed to hover over Email Notifications dropdown: " + e.getMessage());
	    }
	
	    
	}

	@Then("I should see the following sub-items of Email Notifications:")
	public void i_should_see_the_following_sub_items_of_email_notifications(DataTable dataTable) {
		// Convert DataTable (single column) to List<String>
	    List<String> expected = dataTable.asList().stream().map(String::trim).collect(Collectors.toList());
	    // Actual labels from the page
	    List<String> actualLabels = getAdminPageObjects().adminEmailNotificationsInfoOptions.stream().map(el -> el.getText().trim()).toList();
	    SeleniumTestHelper.assertEquals(expected, actualLabels, "All Email Notifications options are verified: ");
	}

	@And("I hover over or click on Project Info")
	public void i_hover_over_or_click_on_project_info() {
		logReportMessage("Hovering over Project Info dropdown...");
	    try {
	        // Use SeleniumTestHelper.mouseHover with WebElement from AdminPageObjects
	        SeleniumTestHelper.mouseHover(getAdminPageObjects().getProjectInfoLink());
	        logReportMessage("Successfully hovered over Project Info dropdown");
	    } catch (Exception e) {
	        logReportMessage("Failed to hover over Project Info dropdown: " + e.getMessage());
	    }
	
	
	}

	@Then("I should see the following sub-items of Project Info:")
	public void i_should_see_the_following_sub_items_of_project_info(DataTable dataTable) {
		// Convert DataTable (single column) to List<String>
	    List<String> expected = dataTable.asList().stream().map(String::trim).collect(Collectors.toList());
	    // Actual labels from the page
	    List<String> actualLabels = getAdminPageObjects().adminProjectInfoOptions.stream().map(el -> el.getText().trim()).toList();
	    SeleniumTestHelper.assertEquals(expected, actualLabels, "All Project Info options are verified: ");
	}

	@And("I hover over or click on Data Import\\/Export")
	public void i_hover_over_or_click_on_data_import_export() {
		logReportMessage("Hovering over Import/Export dropdown...");
	    try {
	        // Use SeleniumTestHelper.mouseHover with WebElement from AdminPageObjects
	        SeleniumTestHelper.mouseHover(getAdminPageObjects().getDataImportExportInfoLink());
	        logReportMessage("Successfully hovered over Import/Export dropdown");
	    } catch (Exception e) {
	        logReportMessage("Failed to hover over Import/Export dropdown: " + e.getMessage());
	    }
	
	
	}

	@Then("I should see the following sub-items of Data Import\\/Export:")
	public void i_should_see_the_following_sub_items_of_data_import_export(DataTable dataTable) {
		// Convert DataTable (single column) to List<String>
	    List<String> expected = dataTable.asList().stream().map(String::trim).collect(Collectors.toList());
	    // Actual labels from the page
	    List<String> actualLabels = getAdminPageObjects().adminDataImportExportOptions.stream().map(el -> el.getText().trim()).toList();
	    SeleniumTestHelper.assertEquals(expected, actualLabels, "All Data Import\\\\/Export: options are verified: ");
	}

	@And("I hover over or click on Custom Fields")
	public void i_hover_over_or_click_on_custom_fields() {
		logReportMessage("Hovering over Custom Fields dropdown...");
	    try {
	        // Use SeleniumTestHelper.mouseHover with WebElement from AdminPageObjects
	        SeleniumTestHelper.mouseHover(getAdminPageObjects().getCustomFieldsInfoLink());
	        logReportMessage("Successfully hovered over Custom Fields dropdown");
	    } catch (Exception e) {
	        logReportMessage("Failed to hover over Custom Fields dropdown: " + e.getMessage());
	    }
	
	    
	}

	


}