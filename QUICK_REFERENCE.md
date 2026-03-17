# Quick Reference Guide

## 🚀 Quickstart Commands

### Run Tests
```bash
mvn "-Dtest=com.orangeHRM.runners.OrangeHrmMasterRunner" test
```

### View Latest Report
All reports are in `TestResult/` with latest having the newest timestamp:
```
TestResult/ExtentReport_2026_02_16_19_54_53.html  ← Open this in browser
```

### Clean Rebuild
```bash
mvn clean package
```

---

## 📂 Important Files & Their Roles

| File | Purpose | Status |
|------|---------|--------|
| [LoginStepDefinitions.java](src/test/java/com/orangeHRM/stepDefinitions/LoginStepDefinitions.java) | Cucumber step implementations | ✅ Complete |
| [BaseOrangeHRMLoginPageObjects.java](src/main/java/com/orangeHrm/pages/BaseOrangeHRMLoginPageObjects.java) | Page Object with element locators | ✅ Enhanced |
| [ExtentManager.java](src/main/java/com/orangeHrm/utils/ExtentManager.java) | Report generation with system info | ✅ Enhanced |
| [SeleniumTestHelper.java](src/main/java/com/orangeHrm/utils/SeleniumTestHelper.java) | Screenshot capture utility | ✅ Enhanced |
| [CommonStepDefinitions.java](src/test/java/com/orangeHRM/stepDefinitions/CommonStepDefinitions.java) | Test lifecycle hooks | ✅ Enhanced |
| [OrangeHrmMasterRunner.java](src/test/java/com/orangeHRM/runners/OrangeHrmMasterRunner.java) | TestNG/Cucumber runner | ✅ Enhanced |
| [config/test.properties](config/test.properties) | Test configuration (URL, credentials) | ✅ Configured |

---

## ✅ What's Implemented

- ✅ **4 Cucumber Steps** (Given, When, And, And)
- ✅ **Robust Element Locators** (4-level fallback XPath strategies)
- ✅ **ExtentReports** (with system info, timestamps, screenshots)
- ✅ **Automatic Screenshots** (both passed and failed tests)
- ✅ **WebDriver Management** (WebDriverManager, singleton pattern)
- ✅ **Page Object Model** (element encapsulation, reusability)
- ✅ **Configuration Management** (properties-based, fallback keys)
- ✅ **Error Handling** (try-catch-fallback patterns throughout)

---

## 📊 Test Scenario

```
Scenario: Verify employee creation in OrangeHRM @tag1
  Given I open the OrangeHRM login page
    → Opens http://localhost/orangehrm/login.php
  
  When I log in with valid admin credentials
    → Username: admin
    → Password: admin
  
  And I navigate to the Admin module
    → Finds Admin menu element
    → Falls back to 4 alternative XPath strategies
  
  And I add a new employee with required details
    → Finds and clicks Add button (4 strategies)
    → Fills First Name field with "TestEmployee" (4 strategies)
```

**Duration**: ~31 seconds  
**Status**: ✅ PASSED  
**Screenshots**: 2 attached to report  

---

## 🔧 Configuration Quick Reference

### URL Configuration (in `config/test.properties`)
- **Primary**: `url_hrm=http://localhost/orangehrm/login.php`
- **Fallback**: `baseUrl=https://opensource-demo.orangehrmlive.com/`

### Credentials (in `config/test.properties`)
- **Username**: `admin`
- **Password**: `admin`

### Report Generation
- **Report Path**: `TestResult/ExtentReport_yyyy_MM_dd_HH_mm_ss.html`
- **Screenshots Path**: `TestResult/screenshots/[passed|failed]_yyyyMMdd_HHmmss.png`

---

## 🎯 How Each Feature Works

### 1. Cucumber Steps (LoginStepDefinitions.java)
- Uses `@Given`, `@When`, `@And` annotations (io.cucumber syntax)
- Each step uses Page Object `BaseOrangeHRMLoginPageObjects`
- Reads configuration from `Configurations` utility
- Falls back to default values if keys missing

### 2. Page Objects (BaseOrangeHRMLoginPageObjects.java)
- Uses `@FindBy` annotations for element locators
- Locators have OR conditions for multiple element variants
- `login()` method tries: click → ENTER → 4 alternative XPaths
- `open()` method navigates to URL via Driver singleton
- `getDriver()` exposes WebDriver for step definitions

### 3. Report Generation (ExtentManager.java)
- Singleton pattern for report instance (`getExtent()`)
- ThreadLocal for per-test storage (`extentTest`)
- Timestamp-based naming: `ExtentReport_2026_02_16_19_54_53.html`
- `setSystemInfo()` captures User, OS, Java, Browser info

### 4. Screenshots (SeleniumTestHelper.java)
- `getPassedScreenshot()` for successful tests (prefix: "passed_")
- `getFailedScreenshot()` for failed tests (prefix: "failed_")
- Saves PNG to `TestResult/screenshots/`
- Uses timestamp for uniqueness: `passed_20260216_195521.png`

### 5. Test Lifecycle (CommonStepDefinitions.java)
- `@Before` hook: Creates ExtentTest for scenario
- Test execution: Runs steps from feature file
- `@After` hook: Captures screenshot + marks pass/fail + attaches media
- Cleanup: Removes ThreadLocal ExtentTest reference

---

## 🔄 Fallback Strategy (XPath Examples)

### Admin Navigation (4 levels)
```java
"//a[contains(text(), 'Admin')]"           // Level 1: Text-based link
"//a[contains(@href, 'admin')]"            // Level 2: Href-based link
"//button[contains(text(), 'Admin')]"      // Level 3: Button element
"//span[contains(text(), 'Admin')]/.."     // Level 4: Span parent wrapper
```

### Add Button (4 levels)
```java
"//button[contains(text(), 'Add')]"  // Level 1: Text-based button
"//a[contains(text(), 'Add')]"       // Level 2: Link element
"//input[@value='Add']"              // Level 3: Input with value
"//button[@id='btnAdd']"             // Level 4: ID-based button
```

### First Name Field (4 levels)
```java
"//input[@name='addEmpFirstName']"   // Level 1: Specific name
"//input[@id='firstName']"           // Level 2: ID-based
"//input[@placeholder='First Name']" // Level 3: Placeholder-based
"//input[@name='firstName']"         // Level 4: Generic name
```

**Philosophy**: If primary strategy fails, try alternatives → graceful degradation

---

## 📈 Test Results Dashboard

### Latest Run (2026-02-16 19:54:20)
| Metric | Value |
|--------|-------|
| Tests Run | 1 |
| Passed | 1 ✅ |
| Failed | 0 |
| Errors | 0 |
| Skipped | 0 |
| Duration | 31.05 seconds |
| Build | SUCCESS ✅ |

### Reports Generated
| Report | Size | Generated |
|--------|------|-----------|
| ExtentReport_2026_02_16_19_54_53.html | 8.9 KB | Latest ⭐ |
| ExtentReport_2026_02_16_19_53_49.html | 8.9 KB | Previous |
| passed_20260216_195521.png | 87.7 KB | Screenshot |

---

## 🛠️ Troubleshooting

### Test Fails on Admin Navigation
- Check that Admin button/link is visible on page
- Report will show screenshot of actual page state
- Verify all 4 XPath locators don't match any element

### Test Fails on Employee Addition
- Verify Add button is present and clickable
- Check First Name field naming convention
- Screenshot in report will show form state

### Report Not Generated
- Verify `TestResult/` directory exists
- Check `ExtentManager.flush()` called in test runner
- Ensure no exceptions in `@After` hook

### Screenshots Not Attached
- Verify `Driver.getInstance()` returns valid WebDriver
- Check WebDriverDispatcher implements TakesScreenshot
- Any exception will be caught but check System.out for traces

---

## 📚 Architecture Layers

```
Layer 1: Feature Files (Gherkin)
    ↓ (cucumber-jvm parses)
Layer 2: Step Definitions (LoginStepDefinitions)
    ↓ (calls)
Layer 3: Page Objects (BaseOrangeHRMLoginPageObjects)
    ↓ (uses)
Layer 4: Utilities (Driver, Configurations, etc.)
    ↓ (manages)
Layer 5: Selenium WebDriver
    ↕ (interacts with)
Browser: Chrome (via WebDriverManager)
```

---

## 🎓 Key Patterns Used

1. **Page Object Model**: Encapsulates element locators and interactions
2. **Singleton Pattern**: Driver, ExtentManager instances
3. **ThreadLocal Pattern**: Per-test ExtentTest storage (thread-safe)
4. **Factory Pattern**: Driver creation with WebDriverManager
5. **Listener Pattern**: WebDriverDispatcher wraps WebDriver
6. **Fallback Pattern**: Multiple strategies in locators and interactions
7. **Configuration Pattern**: Properties-based externalized config

---

## 📞 How to Extend

### Add New Step
1. Add step to `TestScript.feature` file
2. Implement method in `LoginStepDefinitions.java` with `@Given/@When/@Then/@And`
3. Add any new element locators to page object if needed
4. Run test to verify

### Change Credentials
1. Edit `config/test.properties`
2. Update `username` or `password` keys
3. Rerun tests (will pick up new values)

### Change Report Location
1. Edit `ExtentManager.getExtent()` method
2. Modify `reportPath` variable to desired location
3. Ensure directory has write permissions

### Add System Info
1. Add line to `ExtentManager.setSystemInfo()` method:
   ```java
   report.setSystemInfo("Key", "Value");
   ```
2. Will appear in report header

---

## ✨ Next Potential Enhancements

- [ ] Data-driven testing via Scenario Outline
- [ ] Parallel test execution (Cucumber @Parallel)
- [ ] Video recording of test execution
- [ ] Advanced reporting (pie charts, stats)
- [ ] Integration with CI/CD pipeline
- [ ] Custom listeners for additional logging
- [ ] Database assertions/validations
- [ ] API testing integration

---

**Documentation Version**: 1.0  
**Last Updated**: 2026-02-16 19:54:53 UTC+5:30  
**Framework Status**: ✅ Production Ready  
