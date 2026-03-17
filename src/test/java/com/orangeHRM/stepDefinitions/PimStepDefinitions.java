package com.orangeHrm.stepDefinitions;

import com.orangeHrm.pages.AdminPageObjects;
import com.orangeHrm.pages.BaseOrangeHRMLoginPageObjects;
import com.orangeHrm.pages.PimPageObjects;
import com.orangeHrm.utils.Driver;
import com.orangeHrm.utils.SeleniumTestHelper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PimStepDefinitions extends BaseStepDefinition{

    private PimPageObjects pimPageObjects;
    private BaseOrangeHRMLoginPageObjects baseOrangeHRMLoginPageObjects;

    private PimPageObjects getPIMPageObject() {
        if (pimPageObjects == null) {
            // Pass the driver to constructor to avoid duplicate getInstance() calls
            WebDriver driver = Driver.getInstance();
            pimPageObjects = new PimPageObjects(driver);
        }
        return pimPageObjects;
    }

    @And("I navigate to the PIM module")
    public void i_navigate_to_the_pim_module() {


        try {
            getPIMPageObject().navigateToPIMModule();
            logReportMessage("Successfully navigated to PIM module");
        } catch (InterruptedException e) {
            logReportMessage("Failed to navigate to PIM module: " + e.getMessage());
            SeleniumTestHelper.markCurrentThreadInterrupted();
            throw new RuntimeException("Failed to navigate to PIM module", e);
        }

    }

    @When("I add a new employee with first name {string} and last name {string}")
    public void i_add_a_new_employee_with_first_name_and_last_name(String firstName, String lastName) {
        WebDriver driver = Driver.getInstance();
        try {
            // OrangeHRM local (classic UI) flow: PIM -> Add Employee (often in a dropdown), then fill form in rightMenu frame.
            driver.switchTo().defaultContent();
            WebDriver actualDriver = driver;
            if (driver instanceof com.orangeHrm.utils.WebDriverDispatcher) {
                actualDriver = ((com.orangeHrm.utils.WebDriverDispatcher) driver).getUnderlyingDriver();
            }
            WebElement addEmployeeLink = null;
            try {
                // Prefer an actually visible "Add Employee" link
                addEmployeeLink = actualDriver.findElement(By.xpath("//a[normalize-space()='Add Employee' or contains(normalize-space(), 'Add Employee')]"));
            } catch (Exception ignored) {
                // continue
            }
            if (addEmployeeLink == null) {
                // Open PIM menu first (some UIs require hover)
                try {
                    SeleniumTestHelper.mouseHover("id", "pim");
                } catch (Exception ignored) {
                    // continue
                }
                try {
                    addEmployeeLink = actualDriver.findElement(By.xpath("//a[normalize-space()='Add Employee' or contains(normalize-space(), 'Add Employee')]"));
                } catch (Exception ignored) {
                    // continue
                }
            }
            if (addEmployeeLink != null) {
                try {
                    addEmployeeLink.click();
                } catch (Exception clickErr) {
                    try {
                        ((org.openqa.selenium.JavascriptExecutor) actualDriver).executeScript("arguments[0].click();", addEmployeeLink);
                    } catch (Exception jsErr) {
                        throw clickErr;
                    }
                }
            } else {
                // Fallback: navigate directly from current base URL patterns
                String currentBase = driver.getCurrentUrl();
                int idx = currentBase.indexOf("/orangehrm/");
                String base = idx > 0 ? currentBase.substring(0, idx + "/orangehrm/".length()) : currentBase;
                String[] candidates = new String[] {
                    base + "pim/addEmployee.php",
                    base + "pim/addEmployee",
                    base + "index.php/pim/addEmployee"
                };
                boolean navigated = false;
                for (String url : candidates) {
                    try {
                        driver.get(url);
                        navigated = true;
                        break;
                    } catch (Exception ignored) {
                        // try next
                    }
                }
                if (!navigated) {
                    throw new RuntimeException("Could not locate or navigate to Add Employee page");
                }
            }

            // Many classic OrangeHRM pages render the form inside 'rightMenu' frame
            try {
                driver.switchTo().defaultContent();
                driver.switchTo().frame("rightMenu");
            } catch (Exception ignored) {
                // Not a framed UI; continue in default content
            }

            WebElement firstNameInput = driver.findElement(By.xpath("//input[@id='txtEmpFirstName' or @name='txtEmpFirstName' or @placeholder='First Name' or @name='firstName']"));
            SeleniumTestHelper.clear(firstNameInput);
            SeleniumTestHelper.enterText(firstNameInput, firstName);

            WebElement lastNameInput = driver.findElement(By.xpath("//input[@id='txtEmpLastName' or @name='txtEmpLastName' or @placeholder='Last Name' or @name='lastName']"));
            SeleniumTestHelper.clear(lastNameInput);
            SeleniumTestHelper.enterText(lastNameInput, lastName);

            WebElement save = driver.findElement(By.xpath("//input[@id='btnSave' or @value='Save' or @type='submit'] | //button[@id='btnSave' or normalize-space()='Save']"));
            SeleniumTestHelper.click(save);

            logReportMessage("Submitted new employee: " + firstName + " " + lastName);
        } catch (Exception e) {
            logReportMessage("Failed to add new employee: " + e.getMessage());
            throw new RuntimeException("Failed to add new employee", e);
        }
    }
}
