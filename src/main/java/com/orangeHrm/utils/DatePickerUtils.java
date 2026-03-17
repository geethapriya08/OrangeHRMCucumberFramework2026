package com.orangeHrm.utils;

import com.orangeHrm.pages.LeavePageObjects;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DatePickerUtils {

    /**
     * Selects a date from the YUI calendar datepicker using LeavePageObjects locators.
     * Supports full date selection with navigation to correct month/year if needed.
     *
     * @param driver             WebDriver instance
     * @param leavePageObjects   LeavePageObjects instance containing date picker locators
     * @param dateString         The date to select in YYYY-MM-DD format (e.g., "2026-03-12")
     */
    public static void selectDate(WebDriver driver, LeavePageObjects leavePageObjects, String dateString) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Parse the date
        LocalDate targetDate = LocalDate.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        int targetYear = targetDate.getYear();
        int targetMonth = targetDate.getMonthValue();
        int targetDay = targetDate.getDayOfMonth();

        // Click on the calendar button to open the datepicker
        SeleniumTestHelper.click(leavePageObjects.getCalendarButton());

        // Wait for the calendar to be visible
        wait.until(ExpectedConditions.visibilityOf(leavePageObjects.getCalendarContainer()));

        // Navigate to the correct month and year if needed
        navigateToMonthAndYear(driver, leavePageObjects, targetYear, targetMonth);

        // Find the date element and click it using LeavePageObjects method
        WebElement dateElement = leavePageObjects.getDateElement(driver, targetDay);
        dateElement.click();

        // Wait for the calendar to disappear (assuming it closes after selection)
        wait.until(ExpectedConditions.invisibilityOf(leavePageObjects.getCalendarContainer()));
    }

    /**
     * Navigates the calendar to the specified month and year using LeavePageObjects locators.
     *
     * @param driver             WebDriver instance
     * @param leavePageObjects   LeavePageObjects instance containing calendar navigation locators
     * @param targetYear         The target year
     * @param targetMonth        The target month (1-12)
     */
    private static void navigateToMonthAndYear(WebDriver driver, LeavePageObjects leavePageObjects, int targetYear, int targetMonth) {
        // Get current displayed month and year from the header using LeavePageObjects method
        String currentHeader = leavePageObjects.getCurrentMonthYearText();

        // Parse current month and year
        String[] parts = currentHeader.split(" ");
        String currentMonthName = parts[0];
        int currentYear = Integer.parseInt(parts[1]);

        int currentMonth = getMonthNumber(currentMonthName);

        // Calculate how many months to navigate
        int monthDiff = (targetYear - currentYear) * 12 + (targetMonth - currentMonth);

        if (monthDiff != 0) {
            boolean navigateForward = monthDiff > 0;
            int clickCount = Math.abs(monthDiff);

            // Click the navigation button the required number of times
            for (int i = 0; i < clickCount; i++) {
                // Re-fetch the navigation button each time to avoid stale element references
                WebElement navButton;
                if (navigateForward) {
                    navButton = leavePageObjects.getNextMonthButton();
                } else {
                    navButton = leavePageObjects.getPreviousMonthButton();
                }
                
                navButton.click();
                
                // Small wait to ensure calendar updates
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    /**
     * Converts month name to month number.
     *
     * @param monthName The month name (e.g., "March")
     * @return The month number (1-12)
     */
    private static int getMonthNumber(String monthName) {
        switch (monthName.toLowerCase()) {
            case "january": return 1;
            case "february": return 2;
            case "march": return 3;
            case "april": return 4;
            case "may": return 5;
            case "june": return 6;
            case "july": return 7;
            case "august": return 8;
            case "september": return 9;
            case "october": return 10;
            case "november": return 11;
            case "december": return 12;
            default: throw new IllegalArgumentException("Invalid month name: " + monthName);
        }
    }

    /**
     * Gets the selected date from the input field using LeavePageObjects.
     *
     * @param leavePageObjects LeavePageObjects instance containing the date input field
     * @return The value of the input field
     */
    public static String getSelectedDate(LeavePageObjects leavePageObjects) {
        WebElement inputField = leavePageObjects.getDateInputField();
        return inputField.getAttribute("value");
    }
}