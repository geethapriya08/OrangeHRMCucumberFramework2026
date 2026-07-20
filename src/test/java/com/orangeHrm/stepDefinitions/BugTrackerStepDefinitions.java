package com.orangeHrm.stepDefinitions;

import com.orangeHrm.pages.BaseOrangeHRMLoginPageObjects;
import com.orangeHrm.pages.BugTrackerPageObjects;
import com.orangeHrm.utils.Configurations;
import com.orangeHrm.utils.Driver;

import com.orangeHrm.utils.SeleniumTestHelper;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.HashMap;
import java.util.Map;

public class BugTrackerStepDefinitions extends BaseStepDefinition{
     BugTrackerPageObjects bugTrackerPageObjects;
	BaseOrangeHRMLoginPageObjects baseOrangeHRMLoginPageObjects;
	private Map<String, String> lastReportBugData;



	public BugTrackerStepDefinitions(){
    	super();
    	this.baseOrangeHRMLoginPageObjects = new BaseOrangeHRMLoginPageObjects(driver);
    }

    private BugTrackerPageObjects getBugTrackerPageObjects() {
    	if(bugTrackerPageObjects == null) {
    		bugTrackerPageObjects = new BugTrackerPageObjects(Driver.getInstance());
    	}
    	return bugTrackerPageObjects;
    }

    @Given("I open the OrangeHRM login page \\(Bug Tracker Module)")
    public void i_open_the_orange_hrm_login_page_bug_tracker_module() {
		try {
			String url = null;
			try {
				 url = Configurations.getProperty("url_hrm");
			}catch(Exception e) {
			e.printStackTrace();
		}
		if(url == null || url.isBlank()){
			url = "http://localhost/orangehrm/login.php";
		}
			baseOrangeHRMLoginPageObjects.open(url);
			logReportMessage("Opened OrangeHRM login page: " + url);

		}catch(Exception e){
			throw new RuntimeException("Failed to open OrangeHRM login page: " + e.getMessage(), e);
		}
	}


	@And("I navigate to the Bug Tracker module")
	public void i_navigate_to_the_bug_tracker_module() {
	    try{
			getBugTrackerPageObjects().navigateToBugTrackerPage();
			logReportMessage("Navigated to the Bug Tracker module");
		}catch(Exception e){
			logReportMessage("Failed to navigate to leave module: " + e.getMessage());
			SeleniumTestHelper.markCurrentThreadInterrupted();
			throw new RuntimeException("Failed to navigate to specific holiday module", e);
		}
	}
	private Map<String, String> normalizeReportBugData(Map<String, String> rawData) {
		Map<String, String> normalized = new HashMap<>();
		for (Map.Entry<String, String> entry : rawData.entrySet()) {
			String key = entry.getKey() == null ? "" : entry.getKey().trim();
			String value = entry.getValue() == null ? null : entry.getValue().trim();

			// Skip header row that may be included by DataTable parsing
			if ("Field".equalsIgnoreCase(key) && "Value".equalsIgnoreCase(value)) {
				continue;
			}

			normalized.put(key, value);
		}
		return normalized;
	}

	@When("I should fill all the fields in Report Bugs")
	public void i_should_fill_all_the_fields_in_report_bugs(DataTable dataTable) {
		try {
			SeleniumTestHelper.switchToFrame(getBugTrackerPageObjects().bugTrackerframeID);

			Map<String, String> rawReportBugData = dataTable.asMap(String.class, String.class);
			this.lastReportBugData = normalizeReportBugData(rawReportBugData);

			getBugTrackerPageObjects().fillReportBugForm(lastReportBugData);
			logReportMessage("Successfully filled all the fields in Report Bugs");
		} catch (Exception e) {
			logReportMessage("Failed to fill all the fields in Report Bugs: " + e.getMessage());
			throw new RuntimeException("Failed to fill all the fields in Report Bugs", e);
		}
	}

	@Then("click on Save button")
	public void click_on_save_button() {
		try{
			getBugTrackerPageObjects().reportBugsClickSaveButton();
			logReportMessage("Clicked on Save button");
		}catch(Exception e){
			logReportMessage("Failed to click on Save button: " + e.getMessage());
			throw new RuntimeException("Failed to click on Save button", e);
		}

	}

}
