package com.orangeHrm.stepDefinitions;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Reporter;

import com.aventstack.extentreports.ExtentTest;
import com.orangeHrm.utils.Driver;
import com.orangeHrm.utils.SeleniumTestHelper;
import com.orangeHrm.utils.ExtentManager;

public class BaseStepDefinition {

	protected WebDriver driver;

	public BaseStepDefinition() {
		this.driver = Driver.getInstance();
	}

	public WebDriver getDriver() {
		if (driver == null) {
			driver = Driver.getInstance();
		}
		return driver;
	}

	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}

	// Helper methods for step definitions

	public void clickElement(WebElement element) {
		SeleniumTestHelper.clickOnButton(element);
		logReportMessage("Clicked on element");
	}

	public void enterText(WebElement element, String text) {
		SeleniumTestHelper.enterText(element, text);
		logReportMessage("Entered text: " + text);
	}

	public void clearElement(WebElement element) {
		SeleniumTestHelper.clear(element);
		logReportMessage("Cleared element");
	}

	public void waitForElement(WebElement element, long seconds) {
		SeleniumTestHelper.waitForElementToBeDisplayed(driver, element, (int) seconds);
		logReportMessage("Waited for element for " + seconds + " seconds");
	}

	public boolean isElementVisible(WebElement element) {
		return SeleniumTestHelper.isElementDisplayed(element);
	}

	public boolean isElementClickable(WebElement element) {
		try {
			return element.isDisplayed() && element.isEnabled();
		} catch (Exception e) {
			return false;
		}
	}

	public void scrollToElement(WebElement element) {
		SeleniumTestHelper.scrollToElement(driver, element);
		logReportMessage("Scrolled to element");
	}

	public String captureScreenshot() {
		String screenshot = SeleniumTestHelper.getFailedScreenshot();
		logReportMessage("Screenshot captured: " + screenshot);
		return screenshot;
	}

	public void verifyElementDisplayed(WebElement element, String message) {
		SeleniumTestHelper.verifyElementDisplayed(element, message);
		logReportMessage("Verified element is displayed: " + message);
	}

	public void logReportMessage(String message) {
		Reporter.log(message, true);
		ExtentTest test = ExtentManager.getTest();
		if (test != null) {
			test.info(message);
		}
	}
}
