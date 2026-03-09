package com.orangeHRM.stepDefinitions;

import com.orangeHrm.pages.AdminPageObjects;
import com.orangeHrm.pages.BaseOrangeHRMLoginPageObjects;
import com.orangeHrm.pages.PimPageObjects;
import com.orangeHrm.utils.Driver;
import com.orangeHrm.utils.SeleniumTestHelper;
import io.cucumber.java.en.And;
import org.openqa.selenium.WebDriver;

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
}
