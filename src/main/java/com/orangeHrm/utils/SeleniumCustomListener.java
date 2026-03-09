package com.orangeHrm.utils;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.NoSuchElementException;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverListener;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.cucumber.java.Scenario;

public class SeleniumCustomListener implements WebDriverListener{

	By byHolder;
	public static String currentScenario;
	Scenario scenario;
	public static int externalTimeout;
	public static Operations operation;
	public enum Operations{
		FIND_ELEMENTS, FIND_ELEMENT, CLICK, SEND_KEYS, GET_TEXT, VERIFY_ELEMENT;
	}

	public void beforeAlertAccept(WebDriver driver) {
		// TODO Auto-generated method stub

	}

	public void afterAlertAccept(WebDriver driver) {
		// TODO Auto-generated method stub

	}

	public void afterAlertDismiss(WebDriver driver) {
		// TODO Auto-generated method stub

	}

	
	public void beforeAlertDismiss(WebDriver driver) {
		// TODO Auto-generated method stub

	}

	
	public void beforeNavigateTo(String url, WebDriver driver) {
		// TODO Auto-generated method stub

	}

	
	public void afterNavigateTo(String url, WebDriver driver) {
		// TODO Auto-generated method stub

	}

	
	public void beforeNavigateBack(WebDriver driver) {
		// TODO Auto-generated method stub

	}

	
	public void afterNavigateBack(WebDriver driver) {
		// TODO Auto-generated method stub

	}

	
	public void beforeNavigateForward(WebDriver driver) {
		// TODO Auto-generated method stub

	}

	
	public void afterNavigateForward(WebDriver driver) {
		// TODO Auto-generated method stub

	}

	
	public void beforeNavigateRefresh(WebDriver driver) {
		// TODO Auto-generated method stub

	}

	
	public void afterNavigateRefresh(WebDriver driver) {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("incomplete-switch")
	
	public void beforeFindBy(By by, WebElement element, WebDriver driver) {
		waitForPageToBeLoaded(driver, 120);
		switch(operation){
		case CLICK:
			waitForTheElement(by, driver, 30);
			break;
		case FIND_ELEMENT:
		case FIND_ELEMENTS:
			waitForTheElement(by, driver, 30);
			break;
		case GET_TEXT:
		case VERIFY_ELEMENT:
			if(externalTimeout == 0)
				waitForTheElementWithOutException(by, driver,30);
			else{
				waitForTheElementWithOutException(by, driver, externalTimeout);
			}
		}
		
	}

	
	public void afterFindBy(By by, WebElement element, WebDriver driver) {

	}

	
	public void beforeClickOn(WebElement element, WebDriver driver) {
		if (element == null) {
			return;
		}
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
		try {
			wait.until(ExpectedConditions.elementToBeClickable(element));
		} catch (Exception e) {
			// Element clickability check failed, continue
		}
	}

	
	public void afterClickOn(WebElement element, WebDriver driver) {
		waitForPageToBeLoaded(driver, 20);
	}

	
	public void beforeChangeValueOf(WebElement element, WebDriver driver,
			CharSequence[] keysToSend) {
		/*waitForPageToBeLoaded(driver, 20);
		WebDriverWait wait = new WebDriverWait(driver, 20);
		try{
			wait.until(ExpectedConditions.elementToBeClickable(element));
		}catch(Exception e){
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
		*/
		/*WebDriverWait wait = new WebDriverWait(driver, 60);
		wait.until(ExpectedConditions.visibilityOf(element));*/
	}

	
	public void afterChangeValueOf(WebElement element, WebDriver driver,
			CharSequence[] keysToSend) {
		// TODO Auto-generated method stub

	}

	
	public void beforeScript(String script, WebDriver driver) {
		// TODO Auto-generated method stub

	}

	
	public void afterScript(String script, WebDriver driver) {
		// TODO Auto-generated method stub

	}

	
	public void onException(Throwable throwable, WebDriver driver) {

		File f = ((TakesScreenshot) (Driver.getInstance()))
				.getScreenshotAs(OutputType.FILE);

		try {
			FileUtils
					.copyFile(f,
							new File("./screenshots/" + new Random().nextInt()
									+ ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void waitForTheElement(By by, WebDriver driver, int timeout) {

		long start = System.currentTimeMillis();
		while (true) {

			try {
				if (by != null) {
					driver.findElement(by);
				}
				return;
			} catch (Exception e) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException ex) {
				}
				continue;

			} finally {

				long end = System.currentTimeMillis();
				if ((end - start) / 1000 > timeout) {
					throw new NoSuchElementException(
							"Timeout exceeded and element couldn't be found in "+ timeout + " seconds. ");
				}
			}
		}
	}
	private void waitForTheElementWithOutException(By by, WebDriver driver, int timeout ) {

		long start = System.currentTimeMillis();
		while (true) {

			try {
				if (by != null) {
					driver.findElement(by);
				}
				return;
			} catch (Exception e) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException ex) {
				}
				continue;

			} finally {

				long end = System.currentTimeMillis();
				if ((end - start) / 1000 > timeout) {
					break;
				}
			}
		}
	}

	private static void waitForPageToBeLoaded(WebDriver driver, int maxTimeOutInSec) {
		long intialTime = System.currentTimeMillis();
		long currentTime = intialTime;
		long maxWaitTime = maxTimeOutInSec * 1000;
		JavascriptExecutor js = (JavascriptExecutor) driver;
		while (currentTime - intialTime < maxWaitTime) {
			try {
				Object result = js.executeScript("return document.readyState");
				if (result != null && result.equals("complete")) {
					break;
				}
				Thread.sleep(200);
			} catch (Exception e) {
				e.printStackTrace();
			}
			currentTime = System.currentTimeMillis();
		}
	}

	
	public void afterSwitchToWindow(String arg0, WebDriver arg1) {
		// TODO Auto-generated method stub

	}

	
	public void beforeSwitchToWindow(String arg0, WebDriver arg1) {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("unused")
	private By getElementBy(WebElement ele) {
		By by = null;
		if (ele == null) {
			return by;
		}

		String byString = ele.toString().split("->", 2)[1];
		String[] byDetail = byString.split(": ", 2);
		String locator = byDetail[1].substring(0,byDetail[1].length()-1);
		if (locator != null) {
			switch (byDetail[0].trim().toLowerCase()) {
			case "id":
				by = By.id(locator);
				break;
			case "name":
				by = By.name(locator);
				break;
			case "class name":
				by = By.className(locator);
				break;
			case "link text":
				by = By.linkText(locator);
				break;
			case "partial link text":
				by = By.partialLinkText(locator);
				break;
			case "tag name":
				by = By.tagName(locator);
				break;
			case "xpath":
				by = By.xpath(locator);
				break;
			case "css selector":
				by = By.cssSelector(locator);
				break;
			default:
				System.out.println("None of the locator type matched in getElementBy. Locator type found is "+byDetail[0]+" and locator found is "+locator);
			}
		}

		return by;
	}
	@SuppressWarnings("unused")
	private String getLocatorDetails(By by) {
		
		String byString = by.toString().split("By.", 2)[1];
		String[] byDetail = byString.split(":", 2);
		String elementLocator = null;
		switch (byDetail[0].trim().toLowerCase()) {
		case "id":
			elementLocator = "Attempting by id::: " + byDetail[1];
			break;
		case "name":
			elementLocator = "Attempting by name::: " + byDetail[1];
			break;
		case "class name":
		case "classname":
			elementLocator = "Attempting by classname::: " + byDetail[1];
			break;
		case "link text":
		case "linktext":
			elementLocator = "Attempting by linktext::: " + byDetail[1];
			break;
		case "partial link text":
		case "partiallinktext":
			elementLocator = "Attempting by partiallinktext::: " + byDetail[1];
			break;
		case "tag name":
		case "tagname":
			elementLocator = "Attempting by tagname::: " + byDetail[1];
			break;
		case "xpath":
			elementLocator = "Attempting by xpath::: " + byDetail[1];
			break;
		case "css selector":
		case "cssselector":
			elementLocator = "Attempting by cssselector::: " + byDetail[1];
			break;
		default:
			System.out.println("None of the locator type is matching. Locatory type found is "+byDetail[0]+". Locator value found is "+byDetail[1]);
		}

		return elementLocator;
	}

	
	public <X> void beforeGetScreenshotAs(OutputType<X> target) {
		// TODO Auto-generated method stub
		
	}

	
	public <X> void afterGetScreenshotAs(OutputType<X> target, X screenshot) {
		// TODO Auto-generated method stub
		
	}

	
	public void beforeGetText(WebElement element, WebDriver driver) {
		// TODO Auto-generated method stub
		
	}

	
	public void afterGetText(WebElement element, WebDriver driver, String text) {
		// TODO Auto-generated method stub
		
	}

}
