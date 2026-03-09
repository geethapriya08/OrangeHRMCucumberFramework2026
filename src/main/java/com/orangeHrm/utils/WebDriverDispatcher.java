package com.orangeHrm.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.events.WebDriverListener;

import com.orangeHrm.utils.SeleniumCustomListener.Operations;

@SuppressWarnings("null")
public class WebDriverDispatcher implements WebDriver, WebElement, JavascriptExecutor{
	

	private WebDriver driver;
	private WebElement currentElement;
	private By by;
	private List<WebElement> currentElements;

	List<WebDriverListener> listeners;

	public WebDriverDispatcher(WebDriver driver) {
		this.driver = driver;
		listeners = new ArrayList<>();
	}

	public void registerListener(WebDriverListener  listener) {
		listeners.add(listener);
	}

	/**
	 * Get the underlying WebDriver instance to use with Actions or other advanced features
	 */
	public WebDriver getUnderlyingDriver() {
		return driver;
	}

	// Screenshot Method
	@Override
	public <X> X getScreenshotAs(OutputType<X> target)
			throws WebDriverException {
		if (target == null) {
			throw new IllegalArgumentException("OutputType cannot be null");
		}

		switch (Configurations.getProperty("browser")) {
		case "firefox":
			return ((FirefoxDriver) driver).getScreenshotAs(target);
		case "ie":
			return ((InternetExplorerDriver) driver).getScreenshotAs(target);
		default:
			return ((ChromeDriver) driver).getScreenshotAs(target);
		}

	}

	@Override
	public List<WebElement> findElements(By by) {

		for (WebDriverListener l : listeners) {
			SeleniumCustomListener.operation = Operations.FIND_ELEMENTS;
			l.beforeFindElement(currentElement, by);
		}

		currentElements = driver.findElements(by);

		for (WebDriverListener l : listeners) {
			l.afterFindElement(driver, by, currentElement);
		}

		return currentElements;
	}

	@Override
	public WebElement findElement(By by) {
		for (WebDriverListener l : listeners) {
			SeleniumCustomListener.operation = Operations.FIND_ELEMENT;
			l.beforeFindElement(currentElement, by);
		}

		// Use implicit timeout instead of crude retry loop
		currentElement = driver.findElement(by);

		for (WebDriverListener l : listeners) {
			l.afterFindElement(driver, by, currentElement);
		}
		this.by = by;
		return this;
	}
	
	public boolean verifyElement(By by, int timeout) {
		boolean isElementFound = true;
		for (WebDriverListener l : listeners) {
			SeleniumCustomListener.operation = Operations.VERIFY_ELEMENT;
			SeleniumCustomListener.externalTimeout = timeout;
			l.beforeFindElement(currentElement , by);
			SeleniumCustomListener.externalTimeout = 0;
		}

		try {
			currentElement = driver.findElement(by);
			isElementFound = true;
		} catch (NoSuchElementException e) {
			isElementFound = false;
		}

		return isElementFound;
	}

	public WebElement findElement(By by, int maxWaitTime) {

		for (WebDriverListener l : listeners) {
			SeleniumCustomListener.operation = Operations.GET_TEXT;
			l.beforeFindElement(currentElement, by);
		}

		currentElement = driver.findElement(by);

		for (WebDriverListener l : listeners) {
			l.afterFindElement(driver, by, currentElement);
		}
		this.by = by;
		return this;
	}

	// WebElement Methods
	@Override
	public void click() {

		for (WebDriverListener l : listeners) {
			SeleniumCustomListener.operation = Operations.CLICK;
			l.beforeClick(currentElement);
		}

		driver.findElement(by).click();

		for (WebDriverListener l : listeners) {
			l.afterClick(currentElement);
		}

	}

	@Override
	public void submit() {
		driver.findElement(by).submit();
	}

	@Override
	public void sendKeys(CharSequence... keysToSend) {
		for (WebDriverListener l : listeners) {
			SeleniumCustomListener.operation = Operations.SEND_KEYS;
			l.beforeSendKeys(currentElement, keysToSend);
		}
		
		driver.findElement(by).sendKeys(keysToSend);

		for (WebDriverListener l : listeners) {
			l.afterSendKeys(currentElement, keysToSend);
		}
	}

	@Override
	public void clear() {
		driver.findElement(by).clear();
	}

	@Override
	public String getTagName() {
		return driver.findElement(by).getTagName();
	}

	@Override
	public String getAttribute(String name) {
		return driver.findElement(by).getAttribute(name);
	}

	@Override
	public boolean isSelected() {
		return driver.findElement(by).isSelected();
	}

	@Override
	public boolean isEnabled() {
		return driver.findElement(by).isEnabled();
	}

	@Override
	public String getText() {
		for (WebDriverListener l : listeners) {
			SeleniumCustomListener.operation = Operations.GET_TEXT;
			l.beforeFindElement(driver,by);
		}
		String text = null;
		try {
			text = driver.findElement(by).getText();
		} catch (NoSuchElementException e) {
			text = "";
		}
		return text;
	}

	@Override
	public boolean isDisplayed() {
		return driver.findElement(by).isDisplayed();
	}

	@Override
	public Point getLocation() {
		return driver.findElement(by).getLocation();
	}

	@Override
	public Dimension getSize() {
		return driver.findElement(by).getSize();
	}

	@Override
	public Rectangle getRect() {
		return driver.findElement(by).getRect();
	}

	@Override
	public String getCssValue(String propertyName) {
		return driver.findElement(by).getCssValue(propertyName);
	}

	// JavaScript Executor Methods
	@Override
	public Object executeScript(String script, Object... args) {
		for (WebDriverListener l : listeners) {
			l.beforeExecuteScript(driver, script, args);
		}

		Object obj = ((JavascriptExecutor) driver).executeScript(script, args);

		for (WebDriverListener l : listeners) {
			l.afterExecuteScript(driver, script, args, args);
		}

		return obj;
	}

	@Override
	public Object executeAsyncScript(String script, Object... args) {
		return ((JavascriptExecutor) driver).executeAsyncScript(script, args);
	}

	/*private void waitForTheElement(By by, WebDriver driver, int timeout) {

		long start = System.currentTimeMillis();

		while (true) {

			try {
				driver.findElement(by);
				return;
			} catch (Exception e) {

				try {
					Thread.sleep(500);
				} catch (InterruptedException ex) {
				}

				continue;

			} finally {

				long end = System.currentTimeMillis();

				System.out.println("Timeout: " + (end - start) / 1000);

				if ((end - start) / 1000 > timeout) {
					throw new NoSuchElementException(
							"Timeout exceeded and element couldn't be found");
				}
			}
		}
	}*/

	@Override
	public void get(String url) {
		driver.get(url);

	}

	@Override
	public String getCurrentUrl() {
		return driver.getCurrentUrl();
	}

	@Override
	public String getTitle() {
		return driver.getTitle();
	}

	@Override
	public String getPageSource() {
		return driver.getPageSource();
	}

	@Override
	public void close() {
		driver.close();
	}

	@Override
	public void quit() {
		driver.quit();
	}

	@Override
	public Set<String> getWindowHandles() {
		return driver.getWindowHandles();
	}

	@Override
	public String getWindowHandle() {
		return driver.getWindowHandle();
	}

	@Override
	public TargetLocator switchTo() {
		sleep(1);
		return driver.switchTo();
	}

	@Override
	public Navigation navigate() {
		return driver.navigate();
	}

	@Override
	public Options manage() {
		return driver.manage();
	}

	private void sleep(int seconds) {
		try {
			Thread.sleep(seconds * 1000);
		} catch (InterruptedException e) {
		}
	}

}
