package com.orangeHrm.stepDefinitions;

import com.orangeHrm.pages.AdminPageObjects;
import com.orangeHrm.pages.LeavePageObjects;
import com.orangeHrm.pages.SpecificHolidayPageObjects;
import com.orangeHrm.utils.Driver;
import com.orangeHrm.utils.ExcelReader;
import com.orangeHrm.utils.SeleniumTestHelper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Objects;

public class SpecificHolidayStepDefinitions extends BaseStepDefinition {
    private SpecificHolidayPageObjects specificHolidayPageObjects;
    private SpecificHolidayPageObjects getSpecificHolidayPageObjects() {
        if (specificHolidayPageObjects == null) {
            specificHolidayPageObjects = new SpecificHolidayPageObjects(Driver.getInstance());
        }
        return specificHolidayPageObjects;
    }


    @And("I navigate to specific holiday module")
    public void i_navigate_to_specific_holiday_module() {
        try {
            getSpecificHolidayPageObjects().navigateToSpecificHolidayModule();
            logReportMessage("Successfully navigated to specific holiday module");
        } catch (InterruptedException e) {
            logReportMessage("Failed to navigate to leave module: " + e.getMessage());
            SeleniumTestHelper.markCurrentThreadInterrupted();
            throw new RuntimeException("Failed to navigate to specific holiday module", e);
        }

    }

    @And("I click on add button to define specific holidays")
    public void i_click_on_add_button_to_add_a_specific_holidays() {
        WebDriver driver = Driver.getInstance();
        try {
            // OrangeHRM local (classic UI) flow: Specific Holidays -> Add Specific Holidays (often in a dropdown), then fill form in rightMenu frame.
            driver.switchTo().frame("rightMenu");
            clickAddButton();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void clickAddButton() throws InterruptedException {
        System.out.println("Attempting to click Add button...");
        try {
            System.out.println("Trying addButton (button contains 'Add')");
            SeleniumTestHelper.click(specificHolidayPageObjects.specificHolidayAddButton);
            System.out.println("Successfully clicked addButton");
            return;
        } catch (Exception e) {
            System.out.println("Failed with addButton: " + e.getMessage());
        }
    }

    @And("I enter all Define Days Off : Specific Holidays details")
    public void i_enter_all_define_days_off_specific_holidays_details() throws InterruptedException {
        try {
            String nameOfHoliday = Objects.requireNonNull(loadSpecificHolidayDataFromExcel("TC_Specific_001")).nameOfHoliday;
            String date = Objects.requireNonNull(loadSpecificHolidayDataFromExcel("TC_Specific_001")).date;
            String day = Objects.requireNonNull(loadSpecificHolidayDataFromExcel("TC_Specific_001")).day;

            getSpecificHolidayPageObjects().enterDefineDaysOfSpecificHoliday(nameOfHoliday,date,day);

            logReportMessage("Define Days Off : Specific Holidays details entered successfully");
        } catch (Exception e) {
            //SpecificHolidayPageObjects.enterCompanyInformation(username, password);
            //logReportMessage("Submitted login for user: " + username);
            logReportMessage("Failed to enter company information details: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
    private SpecificHolidayStepDefinitions.TestData loadSpecificHolidayDataFromExcel(String testCaseId) {
        try {
            ExcelReader excelReader = new ExcelReader();
            excelReader.setReadingSheet("SpecificHoliday");

            if (!testCaseExists(testCaseId, excelReader)) {
                System.out.println("Test case " + testCaseId + " not found in Excel");
                return null;
            }
            String nameOfHoliday = excelReader.getData(testCaseId, "Name_of_Holiday");
            String date =  excelReader.getData(testCaseId,  "Date");
            String day =  excelReader.getData(testCaseId, "Full_Day/Half_Day");

            String expectedResult = excelReader.getData(testCaseId, "ExpectedResult");

            if (nameOfHoliday != null ) {
                SpecificHolidayStepDefinitions.TestData data = new SpecificHolidayStepDefinitions.TestData();
                data.testCaseId = testCaseId;
                data.nameOfHoliday = nameOfHoliday;
                data.date = date;
                data.day = day;
                data.expectedResult = expectedResult != null ? expectedResult : "Login Successful";

                System.out.println("Loaded test data for " + testCaseId + ": nameOfHoliday=" + data.nameOfHoliday);
                return data;
            }

        } catch (Exception e) {
            System.out.println("Error loading test data from Excel: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public class TestData {
        String testCaseId;
        String nameOfHoliday;
        String date;
        String day;
        String expectedResult;

        @Override
        public String toString() {
            return "TestData{" +
                    "testCaseId='" + testCaseId + '\'' +
                    ", nameOfHoliday='" + nameOfHoliday + '\'' +
                    ", date='" + date + '\'' +
                    ", day='" + day + '\'' +
                    ", expectedResult='" + expectedResult + '\'' +
                    '}';
        }
    }

    private boolean testCaseExists(String testCaseId, ExcelReader excelReader) {
        try {
            String nameOfHoliday = excelReader.getData(testCaseId, "Name_of_Holiday");
            return nameOfHoliday != null && !nameOfHoliday.isEmpty();
        } catch (Exception e) {
            System.out.println("Error checking test case existence: " + e.getMessage());
            return false;
        }
    }

}


