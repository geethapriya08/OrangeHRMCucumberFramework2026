package com.orangeHrm.utils;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Driver {

	private static WebDriver driver;
	private static WebDriverDispatcher dispatcher;

	public static synchronized WebDriver getInstance() {

		if (driver == null || ((RemoteWebDriver) driver).getSessionId() == null) {

			switch (Configurations.getProperty("browser")) {
			case "firefox":
				driver = WebDriverManager.firefoxdriver().create();
				break;
			case "ie":
				System.setProperty("webdriver.ie.driver", Configurations.getProperty("ie"));
				driver = WebDriverManager.iedriver().create();
				break;
			default:
				ChromeOptions options = new ChromeOptions();
				// options.setExperimentalOption("useAutomationExtension", false);
//				options.addArguments("--headless");
//				options.setHeadless(true);
//				options.addArguments("window-size=1200x600");
//				options.addArguments("--start-fullscreen");
				// options.addArguments("--start-maximized");

				if (!System.getProperty("os.name").contains("Windows")) {
					String chromeBinary = Configurations.getProperty("chrome_linux");
					if (chromeBinary != null && !chromeBinary.isEmpty()) {
						options.setBinary(chromeBinary);
					}
				}

				Map<String, Object> prefs = new HashMap<String, Object>();
				prefs.put("plugins.plugins_disabled", new String[] { "Chrome PDF Viewer"});
				prefs.put("plugins.always_open_pdf_externally", true);
				prefs.put("download.default_directory", Configurations.getDownloadLocation());
				prefs.put("profile.default_content_setting_values.notifications", 2);
				prefs.put("profile.default_content_setting_values.automatic_downloads", 1);
				prefs.put("credentials_enable_service", false);
				options.setExperimentalOption("prefs", prefs);
				options.addArguments("disable-infobars");

				// BasicConfigurator.configure();
				// WebDriverManager.chromedriver().setup();
				driver = new ChromeDriver(options);
			}
			driver.manage().deleteAllCookies();
			driver.manage().window().maximize();
			
			// Create dispatcher only once per driver instance
			dispatcher = new WebDriverDispatcher(driver);
			dispatcher.registerListener(new SeleniumCustomListener());
		}
		return dispatcher;
	}
	
	public static void closeDriver() {
		if (driver != null) {
			driver.quit();
			driver = null;
			dispatcher = null;
		}
	}

}
