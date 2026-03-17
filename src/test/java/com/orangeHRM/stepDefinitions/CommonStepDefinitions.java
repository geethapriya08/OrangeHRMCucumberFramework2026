package com.orangeHrm.stepDefinitions;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.Reporter;

import com.orangeHrm.utils.Driver;
import com.orangeHrm.utils.SeleniumTestHelper;
import com.orangeHrm.utils.ExtentManager;

import io.cucumber.java.Scenario;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

/**
 * Unified step definitions class containing:
 * 1. Cucumber hooks (@Before, @After) for test setup/teardown
 * 2. All UI interaction step definitions (from former UIInteractionStepDefinitions)
 * 3. Common reusable helper methods
 */
public class CommonStepDefinitions extends BaseStepDefinition {

	public static int testDataNo = -1;
	static String currentScenario;
	static String currentStatus;
	private WebElement lastElement;

	// ============ HOOKS ============

	@Before
	public void intiate(Scenario scenario) {
		Reporter.log("Execution started for : " + scenario.getName(), true);
		if (!scenario.getName().equals(currentScenario)) {
			testDataNo = -1;
			currentScenario = scenario.getName();
		}
		// Initialize driver once per scenario
		WebDriver driver = Driver.getInstance();
		// Update SeleniumTestHelper with the current driver instance
		SeleniumTestHelper.setDriver(driver);
		// create extent test for this scenario
		ExtentManager.createTest(scenario.getName());
	}

	@After
	public void cleanUp(Scenario scenario) {
		System.out.println(scenario.getStatus());
		try {
			if (scenario.isFailed()) {
				String path = SeleniumTestHelper.getFailedScreenshot();
				if (path != null && !path.isEmpty()) {
					ExtentManager.getTest().fail("Failed - screenshot", com.aventstack.extentreports.MediaEntityBuilder.createScreenCaptureFromPath(path).build());
				} else {
					ExtentManager.getTest().fail("Failed");
				}
			} else {
				// Capture screenshot for passed tests
				String path = SeleniumTestHelper.getPassedScreenshot();
				if (path != null && !path.isEmpty()) {
					ExtentManager.getTest().pass("Passed", com.aventstack.extentreports.MediaEntityBuilder.createScreenCaptureFromPath(path).build());
				} else {
					ExtentManager.getTest().pass("Passed");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Driver.closeDriver();
			ExtentManager.removeTest();
			testDataNo = -1;
		}
	}

	// ============ CLICK OPERATIONS ============

	@When("I click on element with xpath {string}")
	public void i_click_on_element_with_xpath(String xpath) {
		try {
			WebElement element = driver.findElement(By.xpath(xpath));
			lastElement = element;
			clickElement(element);
			logReportMessage("Successfully clicked on element: " + xpath);
		} catch (Exception e) {
			logReportMessage("Failed to click on element: " + xpath);
			throw new RuntimeException("Failed to click element", e);
		}
	}

	@When("I click on element with id {string}")
	public void i_click_on_element_with_id(String id) {
		try {
			WebElement element = driver.findElement(By.id(id));
			lastElement = element;
			clickElement(element);
			logReportMessage("Successfully clicked on element with id: " + id);
		} catch (Exception e) {
			logReportMessage("Failed to click on element with id: " + id);
			throw new RuntimeException("Failed to click element", e);
		}
	}

	@When("I click on element with css selector {string}")
	public void i_click_on_element_with_css(String css) {
		try {
			WebElement element = driver.findElement(By.cssSelector(css));
			lastElement = element;
			clickElement(element);
			logReportMessage("Successfully clicked on element with css: " + css);
		} catch (Exception e) {
			logReportMessage("Failed to click on element with css: " + css);
			throw new RuntimeException("Failed to click element", e);
		}
	}

	// ============ TEXT INPUT OPERATIONS ============

	@When("I enter {string} in element with xpath {string}")
	public void i_enter_text_in_element_xpath(String text, String xpath) {
		try {
			WebElement element = driver.findElement(By.xpath(xpath));
			lastElement = element;
			clearElement(element);
			enterText(element, text);
			logReportMessage("Successfully entered text: '" + text + "' in element: " + xpath);
		} catch (Exception e) {
			logReportMessage("Failed to enter text in element: " + xpath);
			throw new RuntimeException("Failed to enter text", e);
		}
	}

	@When("I enter {string} in element with id {string}")
	public void i_enter_text_in_element_id(String text, String id) {
		try {
			WebElement element = driver.findElement(By.id(id));
			lastElement = element;
			clearElement(element);
			enterText(element, text);
			logReportMessage("Successfully entered text: '" + text + "' in element with id: " + id);
		} catch (Exception e) {
			logReportMessage("Failed to enter text in element with id: " + id);
			throw new RuntimeException("Failed to enter text", e);
		}
	}

	@When("I clear element with xpath {string}")
	public void i_clear_element_xpath(String xpath) {
		try {
			WebElement element = driver.findElement(By.xpath(xpath));
			lastElement = element;
			clearElement(element);
			logReportMessage("Successfully cleared element: " + xpath);
		} catch (Exception e) {
			logReportMessage("Failed to clear element: " + xpath);
			throw new RuntimeException("Failed to clear element", e);
		}
	}

	// ============ ELEMENT VISIBILITY & STATE CHECKS ============

	@Then("element with xpath {string} should be visible")
	public void element_should_be_visible_xpath(String xpath) {
		try {
			WebElement element = driver.findElement(By.xpath(xpath));
			lastElement = element;
			waitForElement(element, 10);
			Assert.assertTrue(isElementVisible(element), "Element is not visible: " + xpath);
			logReportMessage("Element is visible as expected: " + xpath);
		} catch (Exception e) {
			logReportMessage("Element visibility check failed: " + xpath);
			throw new RuntimeException("Element visibility check failed", e);
		}
	}

	@Then("element with id {string} should be visible")
	public void element_should_be_visible_id(String id) {
		try {
			WebElement element = driver.findElement(By.id(id));
			lastElement = element;
			waitForElement(element, 10);
			Assert.assertTrue(isElementVisible(element), "Element is not visible: " + id);
			logReportMessage("Element is visible as expected: " + id);
		} catch (Exception e) {
			logReportMessage("Element visibility check failed: " + id);
			throw new RuntimeException("Element visibility check failed", e);
		}
	}

	@Then("element with xpath {string} should not be visible")
	public void element_should_not_be_visible_xpath(String xpath) {
		try {
			WebElement element = driver.findElement(By.xpath(xpath));
			lastElement = element;
			Assert.assertFalse(isElementVisible(element), "Element should not be visible: " + xpath);
			logReportMessage("Element is not visible as expected: " + xpath);
		} catch (Exception e) {
			logReportMessage("Element visibility check failed: " + xpath);
			throw new RuntimeException("Element visibility check failed", e);
		}
	}

	@Then("element with xpath {string} should be enabled")
	public void element_should_be_enabled_xpath(String xpath) {
		try {
			WebElement element = driver.findElement(By.xpath(xpath));
			lastElement = element;
			Assert.assertTrue(isElementClickable(element), "Element is not enabled: " + xpath);
			logReportMessage("Element is enabled as expected: " + xpath);
		} catch (Exception e) {
			logReportMessage("Element enabled check failed: " + xpath);
			throw new RuntimeException("Element enabled check failed", e);
		}
	}

	// ============ SCROLL OPERATIONS ============

	@And("I scroll to element with xpath {string}")
	public void i_scroll_to_element_xpath(String xpath) {
		try {
			WebElement element = driver.findElement(By.xpath(xpath));
			lastElement = element;
			scrollToElement(element);
			logReportMessage("Successfully scrolled to element: " + xpath);
		} catch (Exception e) {
			logReportMessage("Failed to scroll to element: " + xpath);
			throw new RuntimeException("Failed to scroll to element", e);
		}
	}

	// ============ WAIT OPERATIONS ============

	@And("I wait for element with xpath {string} to be visible within {int} seconds")
	public void i_wait_for_element_xpath(String xpath, int seconds) {
		try {
			WebElement element = driver.findElement(By.xpath(xpath));
			lastElement = element;
			waitForElement(element, seconds);
			logReportMessage("Element appeared within " + seconds + " seconds: " + xpath);
		} catch (Exception e) {
			logReportMessage("Timeout waiting for element: " + xpath);
			throw new RuntimeException("Timeout waiting for element", e);
		}
	}

	@And("I wait for element with id {string} to be visible within {int} seconds")
	public void i_wait_for_element_id(String id, int seconds) {
		try {
			WebElement element = driver.findElement(By.id(id));
			lastElement = element;
			waitForElement(element, seconds);
			logReportMessage("Element appeared within " + seconds + " seconds: " + id);
		} catch (Exception e) {
			logReportMessage("Timeout waiting for element: " + id);
			throw new RuntimeException("Timeout waiting for element", e);
		}
	}

	// ============ SCREENSHOT OPERATIONS ============

	@And("I take a screenshot with name {string}")
	public void i_take_screenshot(String name) {
		try {
			String path = captureScreenshot();
			logReportMessage("Screenshot captured: " + name + " at: " + path);
		} catch (Exception e) {
			logReportMessage("Failed to capture screenshot: " + name);
			throw new RuntimeException("Failed to capture screenshot", e);
		}
	}

	// ============ VERIFICATION OPERATIONS ============

	@Then("I verify element with xpath {string} is displayed with message {string}")
	public void i_verify_element_displayed(String xpath, String message) {
		try {
			WebElement element = driver.findElement(By.xpath(xpath));
			lastElement = element;
			verifyElementDisplayed(element, message);
			logReportMessage("Element verification passed: " + message);
		} catch (Exception e) {
			logReportMessage("Element verification failed: " + message);
			throw new RuntimeException("Element verification failed", e);
		}
	}

	// ============ LOGGING OPERATIONS ============

	@And("I log report message {string}")
	public void i_log_message(String message) {
		logReportMessage(message);
	}

	// ============ HELPER METHODS ============

	/**
	 * Get the last element that was interacted with
	 * @return WebElement
	 */
	public WebElement getLastElement() {
		return lastElement;
	}

	/**
	 * Set an element for later use in chained operations
	 * @param element WebElement to set
	 */
	public void setLastElement(WebElement element) {
		this.lastElement = element;
	}
}
