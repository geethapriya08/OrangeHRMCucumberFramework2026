package com.orangeHrm.pages;

import com.orangeHrm.utils.Driver;
import com.orangeHrm.utils.SeleniumTestHelper;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class PimPageObjects {

    private WebDriver driver;

    // PIM Module Navigation - using correct selectors
    @FindBy(id = "pim")
    private WebElement pimDropdown;

    // Employee List Module Navigation - using correct selectors
    @FindBy(xpath = "//span[contains(text(), 'Employee List')]")
    private WebElement employeeListDropdown;

    public PimPageObjects(WebDriver driver) {
        this.driver = driver;
        // Ensure PageFactory elements are initialized with the underlying driver
        WebDriver actualDriver = driver;
        if (driver instanceof com.orangeHrm.utils.WebDriverDispatcher) {
            actualDriver = ((com.orangeHrm.utils.WebDriverDispatcher) driver).getUnderlyingDriver();
        }
        PageFactory.initElements(actualDriver, this);
    }



    /**
     * Navigate to PIM module by hovering on the PIM dropdown
     */
    public void navigateToPIMModule() throws InterruptedException {
        WebDriver driver = Driver.getInstance();
        Thread.sleep(1500);

        System.out.println("Hovering over PIM dropdown...");

        try {
            // Use SeleniumTestHelper.mouseHover to perform a robust hover
            SeleniumTestHelper.mouseHover("id", "pim");
            System.out.println("Successfully hovered over pim dropdown (via SeleniumTestHelper)");
            Thread.sleep(1500);
        } catch (Exception e) {
            System.out.println("Failed to hover over pim dropdown: " + e.getMessage());
        }

        System.out.println("Navigating to pim -> Employee List...");

        try {
            WebDriver actualDriver = driver;
            if (driver instanceof com.orangeHrm.utils.WebDriverDispatcher) {
                actualDriver = ((com.orangeHrm.utils.WebDriverDispatcher) driver).getUnderlyingDriver();
            }
            // Click on Employee List
            System.out.println("Clicking on Employee List...");
            Actions actions = new Actions(actualDriver);
            actions.click(employeeListDropdown).perform();
            System.out.println("Successfully clicked Employee List");
            Thread.sleep(2000);
        } catch (Exception e) {
            System.out.println("Failed in navigation: " + e.getMessage());
        }
    }
}
