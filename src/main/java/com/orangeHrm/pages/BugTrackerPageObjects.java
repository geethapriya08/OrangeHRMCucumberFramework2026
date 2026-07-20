package com.orangeHrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.orangeHrm.utils.SeleniumTestHelper;

import java.util.List;
import java.util.Map;

public class BugTrackerPageObjects {

	private final WebDriver driver;
	@FindBy(xpath="//span[contains(text(),'Bug Tracker')]")
	private WebElement bugTracker;
	@FindBy(id = "rightMenu")
    public WebElement bugTrackerframeID;

	@FindBy(id = "category_id")
	private WebElement categoryDropdown;
	@FindBy(id = "cmbModule")
	private WebElement moduleDropdown;
	@FindBy(id = "priority")
	private WebElement priorityDropdown;
	@FindBy(id = "summary")
	private WebElement summaryField;
	@FindBy(id = "txtEmail")
	private WebElement emailField;
	@FindBy(id = "txtDescription")
	private WebElement descriptionField;
	@FindBy(xpath = "//input[@class='savebutton']")
	public WebElement reportBugsSaveButton;

	@FindBy(xpath = "//input[contains(@value, 'Save')]")
	public WebElement reportBugsSaveButtonByValue;

	public BugTrackerPageObjects(WebDriver driver) {
		this.driver = driver;
		WebDriver actualDriver = driver;
		if(driver instanceof com.orangeHrm.utils.WebDriverDispatcher) {
			actualDriver  = ((com.orangeHrm.utils.WebDriverDispatcher) driver).getUnderlyingDriver();
		}
		PageFactory.initElements(actualDriver, this);
	}

	public void switchToRightMenuFrame() {
		SeleniumTestHelper.switchToFrame(bugTrackerframeID);
	}

	/**
	 * Fills the Report Bugs form. Keys are matched case-sensitively against the
	 * feature file's field names: Category, Module, Priority, Summary, Your Email, Description.
	 */
	public void fillReportBugForm(Map<String, String> fieldValues) {
		for (Map.Entry<String, String> entry : fieldValues.entrySet()) {
			String value = entry.getValue();
			if (value == null || value.trim().isEmpty()) {
				continue;
			}

			switch (entry.getKey()) {
				case "Category" -> SeleniumTestHelper.selectFromDropDown(categoryDropdown, value, SeleniumTestHelper.DropDownMode.VISIBLE_TEXT);
				case "Module" -> SeleniumTestHelper.selectFromDropDown(moduleDropdown, value, SeleniumTestHelper.DropDownMode.VISIBLE_TEXT);
				case "Priority" -> SeleniumTestHelper.selectFromDropDown(priorityDropdown, value, SeleniumTestHelper.DropDownMode.VISIBLE_TEXT);
				case "Summary" -> {
					SeleniumTestHelper.clear(summaryField);
					SeleniumTestHelper.enterText(summaryField, value);
				}
				case "Your Email" -> {
					SeleniumTestHelper.clear(emailField);
					SeleniumTestHelper.enterText(emailField, value);
				}
				case "Description" -> {
					SeleniumTestHelper.clear(descriptionField);
					SeleniumTestHelper.enterText(descriptionField, value);
				}
				default -> throw new IllegalArgumentException("Unsupported Report Bug field: " + entry.getKey());
			}
		}
	}

	public void navigateToBugTrackerPage() {
		try {
			WebDriver actualDriver = driver;
			if (driver instanceof com.orangeHrm.utils.WebDriverDispatcher) {
				actualDriver = ((com.orangeHrm.utils.WebDriverDispatcher) driver).getUnderlyingDriver();
			}
			Actions actions = new Actions(actualDriver);
		System.out.println("Clicking on Bug Tracker...");
		actions.click(bugTracker).perform();
		System.out.println("Successfully clicked specific Holiday");
		Thread.sleep(2000);
	} catch (Exception e) {
		System.out.println("Failed in navigation: " + e.getMessage());
	}
	}

	/**
	 * Click the Save button
	 */
	public void reportBugsClickSaveButton() throws InterruptedException {
		try {
			SeleniumTestHelper.click(reportBugsSaveButton);
			return;
		} catch (Exception e) {
			// Try alternative
		}

		try {
			SeleniumTestHelper.click(reportBugsSaveButtonByValue);
			return;
		} catch (Exception e) {
			// Fallback: Try generic button locators
		}

		// Fallback: Try alternative button locators
		String[] saveXpaths = new String[]{"//input[contains(@class, 'savebutton')]", "//input[@id='saveBtn']", "//input[@class='savebutton']"};

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
