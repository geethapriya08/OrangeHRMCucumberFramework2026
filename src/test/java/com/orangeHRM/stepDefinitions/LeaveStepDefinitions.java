package com.orangeHRM.stepDefinitions;

import com.orangeHrm.pages.AdminPageObjects;
import com.orangeHrm.pages.LeavePageObjects;
import com.orangeHrm.utils.Driver;
import com.orangeHrm.utils.SeleniumTestHelper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;

public class LeaveStepDefinitions extends BaseStepDefinition{

    private LeavePageObjects  leavePageObjects;


    private LeavePageObjects getLeavePageObjects() {
        if (leavePageObjects == null) {
            // Pass the driver to constructor to avoid duplicate getInstance() calls
            WebDriver driver = Driver.getInstance();
            leavePageObjects = new LeavePageObjects(driver);
        }
        return leavePageObjects;
    }

    @When("I navigate to the leave module")
    public void i_navigate_to_the_leave_module() {
        try {
            getLeavePageObjects().navigateToLeaveModule();
            logReportMessage("Successfully navigated to leave module");
        } catch (InterruptedException e) {
            logReportMessage("Failed to navigate to leave module: " + e.getMessage());
            SeleniumTestHelper.markCurrentThreadInterrupted();
            throw new RuntimeException("Failed to navigate to leave module", e);
        }
    }

    @And("I should view all employee leave details")
    public void i_should_view_all_employee_leave_details() {
        try {
            getLeavePageObjects().selectEmployee();
            logReportMessage("Successfully I can view all employee leave details");
        } catch (InterruptedException e) {
            logReportMessage("Failed to view employee leave details: " + e.getMessage());
            SeleniumTestHelper.markCurrentThreadInterrupted();
            throw new RuntimeException("Failed to view employee leave details", e);
        }
    }
}
