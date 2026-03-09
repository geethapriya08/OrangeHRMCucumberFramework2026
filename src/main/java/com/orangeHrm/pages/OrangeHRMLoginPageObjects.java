package com.orangeHrm.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.orangeHrm.utils.Driver;

public class OrangeHRMLoginPageObjects extends BaseOrangeHRMLoginPageObjects {

	public OrangeHRMLoginPageObjects(WebDriver driver) {
		super(driver);
	}
	
	/**
	 * Deprecated: Use constructor with WebDriver parameter instead
	 */
	@Deprecated
	public OrangeHRMLoginPageObjects() {
		super();
	}

}
