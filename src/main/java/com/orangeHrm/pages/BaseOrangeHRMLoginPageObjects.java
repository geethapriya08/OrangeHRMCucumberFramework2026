package com.orangeHrm.pages;

import java.util.NoSuchElementException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.orangeHrm.utils.Configurations;
import com.orangeHrm.utils.Driver;
import com.orangeHrm.utils.SeleniumTestHelper;

@SuppressWarnings("null")
public class BaseOrangeHRMLoginPageObjects {
	
	private OrangeHRMLoginPageObjects hrmLoginPageObjects;
	private WebDriver driver;
	
	/**
	 * Lazy initialization of nested page object
	 */
	private OrangeHRMLoginPageObjects getHrmLoginPageObjects() {
		if (hrmLoginPageObjects == null) {
			hrmLoginPageObjects = new OrangeHRMLoginPageObjects(driver);
		}
		return hrmLoginPageObjects;
	}
	
	// Login Page Elements - OrangeHRM (local instance) - Flexible selectors
	@FindBy(xpath = "//input[contains(@name, 'UserName') or contains(@name, 'username') or contains(@id, 'username')]")
	public WebElement userNameTxtBx;
	
	@FindBy(xpath = "//input[contains(@name, 'Password') or contains(@name, 'password') or contains(@id, 'password')]")
	public WebElement userPwdTxtBx;
	
	@FindBy(xpath = "//input[@name='Submit' or @id='btnLogin' or @type='submit']")
	public WebElement SignInBtn;
	
	/**
	 * Constructor with WebDriver injection - prevents duplicate Driver.getInstance() calls
	 */
	public BaseOrangeHRMLoginPageObjects(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	/**
	 * Deprecated: Use constructor with WebDriver parameter instead
	 */
	@Deprecated
	public BaseOrangeHRMLoginPageObjects() {
		this(Driver.getInstance());
	}

	

	public void login(String userName, String userPwd) throws InterruptedException {
		// Wait for page to load
		Thread.sleep(2500);
		
		try {
			// Debug: List all input fields on the page
			List<WebElement> allInputs = driver.findElements(By.tagName("input"));
			System.out.println("Total input fields found: " + allInputs.size());
			for (int i = 0; i < allInputs.size(); i++) {
				WebElement input = allInputs.get(i);
				System.out.println("  Input " + i + ": id='" + input.getAttribute("id") + "', name='" + input.getAttribute("name") + "', type='" + input.getAttribute("type") + "', placeholder='" + input.getAttribute("placeholder") + "'");
			}
			
			// Wait for username field to be visible before interacting
			System.out.println("Waiting for username field...");
			SeleniumTestHelper.WaitForElement(getHrmLoginPageObjects().userNameTxtBx, 10);
			System.out.println("Username field found!");
			
			SeleniumTestHelper.enterText(getHrmLoginPageObjects().userNameTxtBx, userName);
			SeleniumTestHelper.enterText(getHrmLoginPageObjects().userPwdTxtBx, userPwd);

			// Primary: click the located sign-in button
			try {
				SeleniumTestHelper.click(getHrmLoginPageObjects().SignInBtn);
				return;
			} catch (Exception e) {
				// continue to fallbacks
			}

			// Fallback 1: submit the password field
			try {
				getHrmLoginPageObjects().userPwdTxtBx.sendKeys(Keys.ENTER);
				return;
			} catch (Exception e) {
				// continue
			}

			// Fallback 2: try common alternative locators
			    String[] xpaths = new String[] {
				    "//input[@name='Submit']",
				    "//input[@name='txtUserName']",
				    "//input[@name='txtPassword']",
				    "//input[@id='btnLogin']",
				    "//input[contains(translate(@value,'LOGIN','login'),'login')]",
				    "//button[contains(translate(.,'LOGIN','login'),'login')]"
			    };
			for (String xp : xpaths) {
				try {
					List<WebElement> els = driver.findElements(By.xpath(xp));
					if (els != null && !els.isEmpty()) {
						System.out.println("Found button with xpath: " + xp);
						SeleniumTestHelper.click(els.get(0));
						return;
					}
				} catch (Exception e) {
					// ignore and try next
				}
			}

		} catch (NoSuchElementException noSuchElementExec) {
			System.out.println("Element not found exception: " + noSuchElementExec.getMessage());
			// try direct send and click with best effort
			try {
				SeleniumTestHelper.enterText(getHrmLoginPageObjects().userNameTxtBx, userName);
				SeleniumTestHelper.enterText(getHrmLoginPageObjects().userPwdTxtBx, userPwd);
			} catch (Exception e) {
				System.out.println("Failed to enter credentials: " + e.getMessage());
			}
			try {
				SeleniumTestHelper.click(getHrmLoginPageObjects().SignInBtn);
			} catch (Exception e) {
				System.out.println("Failed to click sign in button: " + e.getMessage());
				try {
					getHrmLoginPageObjects().userPwdTxtBx.sendKeys(Keys.ENTER);
				} catch (Exception ex) {
					System.out.println("Failed to submit form: " + ex.getMessage());
				}
			}
		}

	}
	
	public void open(String url) {
		System.out.println("Opening URL: " + url);
		Driver.getInstance().get(url);
		System.out.println("Page URL after load: " + Driver.getInstance().getCurrentUrl());
		System.out.println("Page Title: " + Driver.getInstance().getTitle());
		System.out.println("Page Source length: " + Driver.getInstance().getPageSource().length());
	}
	
	public WebDriver getDriver() {
		return driver;
	}

	public OrangeHRMLoginPageObjects retailerLogin() throws InterruptedException {
		open(Configurations.getProperty("url_hrm"));
		login(Configurations.getProperty("user_name"), Configurations.getProperty("user_password"));
		return new OrangeHRMLoginPageObjects();
	}
}
