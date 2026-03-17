# TC_001 Login Flow - Quick Start Guide

## What is TC_001?

TC_001 is the **Admin login test case** that reads credentials from your Excel file and logs into OrangeHRM using the refactored ExcelReader with data-driven approach.

---

## TC_001 Test Data (From TestStub.xlsx)

```
Test Case ID:    TC_001
Username:        Admin
Password:        admin123
Result:          Login Successful
```

---

## How It Works - Simple Overview

```
Feature File (Login.feature)
    ↓
Step Definition (LoginStepDefinitions.java)
    ↓
Helper Method: loadTestDataFromExcel ("TC_001", "LoginData")
    ↓
ExcelReader reads from TestStub.xlsx:
    • Gets "Admin" from Excel (Username column, TC_001 row)
    • Gets "admin123" from Excel (Password column, TC_001 row)
    • Gets "Login Successful" from Excel (ExpectedResult column, TC_001 row)
    ↓
TestData object created: {testCaseId="TC_001", username="Admin", password="admin123", expectedResult="Login Successful"}
    ↓
Page Object performs login:
    • Enters "Admin" in username field
    • Enters "admin123" in password field
    • Clicks login button
    ↓
Verification:
    • Checks if dashboard is visible
    ✅ Test PASSED if dashboard appears
    ❌ Test FAILED otherwise
```

---

## The Three Refactored ExcelReader Helper Methods Used

### 1. **getColumnIndexByHeader("Username")**
- **What:** Finds which column contains "Username"
- **How:** Uses getCellStringValue() to safely check each header cell
- **Result:** Returns column number (1 in this case)

### 2. **getRowByHeader("TC_001")**
- **What:** Finds which row contains "TC_001" in the first column
- **How:** Uses getCellStringValue() to safely check each row's first cell
- **Result:** Returns the Row object containing TC_001

### 3. **getCellStringValue(cell)**
- **What:** Safely extracts text from a cell with null-checking
- **How:** Handles missing cells and type conversions gracefully
- **Result:** Returns the cell value or null if not found

---

## Key Code References

### Feature File (Login.feature)
```gherkin
@TC_001 @ValidLogin @BasicDataDriven
Scenario: Login with Admin credentials from Excel
    Given I open the OrangeHRM login page
    When I log in with data from test case TC_001
    Then element with xpath "//div[@class='oxd-layout-context']" should be visible
```

### Step Definition (LoginStepDefinitions.java)
```java
@When("I log in with data from test case {string}")
public void i_log_in_with_test_case_data(String testCaseId) {
    TestData testData = loadTestDataFromExcel(testCaseId, "LoginData");
    logReportMessage("Loading: " + testData.username);
    getLoginPageObject().login(testData.username, testData.password);
}
```

### Helper Method (LoginStepDefinitions.java)
```java
private TestData loadTestDataFromExcel(String testCaseId, String sheetName) {
    ExcelReader reader = getExcelReader();
    reader.setReadingSheet(sheetName);
    
    String username = reader.getData(testCaseId, "Username");
    String password = reader.getData(testCaseId, "Password");
    String expectedResult = reader.getData(testCaseId, "ExpectedResult");
    
    if (username == null || password == null) {
        throw new RuntimeException("Test case data not found for ID: " + testCaseId);
    }
    
    return new TestData(testCaseId, username, password, expectedResult);
}
```

### Data Source (TestStub.xlsx - LoginData sheet)
```
Row 1:  [TestCaseID]  [Username]  [Password]  [ExpectedResult]
Row 2:  [TC_001]      [Admin]     [admin123]  [Login Successful]
```

---

## How to Run TC_001 Test

### **Step 1: Verify Excel File Exists**
```bash
# Check if TestStub.xlsx exists
ls src/test/resources/TestStub.xlsx
# Expected: File should exist with LoginData sheet
```

### **Step 2: Compile the Project**
```bash
mvn clean compile test-compile
```

### **Step 3: Run the Test**

**Option A: Run custom TC_001 runner**
```bash
mvn test -Dtest=TC_001LoginRunner
```

**Option B: Run via feature file with tags**
```bash
mvn test -Dtest=OrangeHrmMasterRunner -Dcucumber.filter.tags="@TC_001"
```

**Option C: Run quick verification**
```bash
mvn test -Dtest=TC_001LoginFlowTest
```

### **Step 4: Check Results**

**If successful (✅):**
```
[INFO] BUILD SUCCESS
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
```

**If failed (❌):**
```
[INFO] BUILD FAILURE
[ERROR] Tests run: 1, Failures: 1, Errors: 0, Skipped: 0
```

Check logs in: `target/surefire-reports/`

---

## Complete Execution Flow for TC_001

```
INPUT: testCaseId = "TC_001"
       sheetName = "LoginData"

Step 1: loadTestDataFromExcel("TC_001", "LoginData")
        ├─ ExcelReader.setReadingSheet("LoginData")
        ├─ reader.getData("TC_001", "Username")
        │  ├─ getColumnIndexByHeader("Username") → 1
        │  ├─ getRowByHeader("TC_001") → Row 2
        │  └─ getCellStringValue(cell[2][1]) → "Admin"
        │
        ├─ reader.getData("TC_001", "Password")
        │  ├─ getColumnIndexByHeader("Password") → 2
        │  ├─ getRowByHeader("TC_001") → Row 2
        │  └─ getCellStringValue(cell[2][2]) → "admin123"
        │
        ├─ reader.getData("TC_001", "ExpectedResult")
        │  ├─ getColumnIndexByHeader("ExpectedResult") → 3
        │  ├─ getRowByHeader("TC_001") → Row 2
        │  └─ getCellStringValue(cell[2][3]) → "Login Successful"
        │
        └─ new TestData("TC_001", "Admin", "admin123", "Login Successful")

Step 2: TestData object = {
            testCaseId = "TC_001"
            username = "Admin"
            password = "admin123"
            expectedResult = "Login Successful"
        }

Step 3: getLoginPageObject().login("Admin", "admin123")
        ├─ usernameField.sendKeys("Admin")
        ├─ passwordField.sendKeys("admin123")
        ├─ loginButton.click()
        └─ Wait for page load

Step 4: Verify dashboard is visible
        ├─ Check if element with xpath "//div[@class='oxd-layout-context']" exists
        ├─ If YES → ✅ TEST PASSED
        └─ If NO  → ❌ TEST FAILED

OUTPUT: Login Test Result
```

---

## File Locations

| File | Location | Purpose |
|------|----------|---------|
| **Excel Data** | `src/test/resources/TestStub.xlsx` | Contains TC_001 credentials |
| **Feature File** | `src/test/resources/Login.feature` | Gherkin test scenario |
| **Step Definitions** | `src/test/java/com/orangeHRM/stepDefinitions/LoginStepDefinitions.java` | Step implementation with helper methods |
| **ExcelReader** | `src/main/java/com/orangeHrm/utils/ExcelReader.java` | Refactored reader with reusable methods |
| **Page Object** | `src/main/java/com/orangeHrm/pages/BaseOrangeHRMLoginPageObjects.java` | Login page actions |
| **Test Reports** | `target/surefire-reports/` | Execution results |

---

## Reusable Methods Hierarchy

```
loadTestDataFromExcel()
    ├─ ExcelReader.setReadingSheet()
    ├─ ExcelReader.getData()
    │  ├─ getColumnIndexByHeader()     ← Reusable Helper #3
    │  ├─ getRowByHeader()             ← Reusable Helper #2
    │  └─ getCellStringValue()         ← Reusable Helper #1
    └─ new TestData()

testCaseExists()
    ├─ ExcelReader.setReadingSheet()
    └─ ExcelReader.checkForPresenceOfValueForHeader()
       ├─ getRowByHeader()             ← Reusable Helper #2
       └─ findValueIndexInRow()        ← Reusable Helper #4 (bonus)

getAllTestCaseValues()
    ├─ ExcelReader.setReadingSheet()
    └─ ExcelReader.getAllValuesForHeader()
       ├─ getRowByHeader()             ← Reusable Helper #2
       └─ getCellStringValue()         ← Reusable Helper #1
```

---

## Troubleshooting

| Issue | Solution |
|-------|----------|
| **Excel file not found** | Ensure `src/test/resources/TestStub.xlsx` exists and has "LoginData" sheet |
| **TC_001 data not found** | Check if Row 2 in LoginData sheet contains TC_001 data |
| **Login fails** | Verify URL in configuration.properties matches OrangeHRM instance |
| **Element not visible** | Check if login credentials are correct and page loads properly |
| **Compilation errors** | Run `mvn clean compile` to resolve dependency issues |

---

## Summary

✅ **TC_001 Test Case**
- Reads credentials from Excel file (TestStub.xlsx)
- Uses refactored ExcelReader's 4 reusable helper methods
- Performs Admin login with data "Admin" / "admin123"
- Verifies successful login to OrangeHRM dashboard
- Demonstrates complete data-driven login flow

**Status:** Ready to Execute  
**Last Updated:** March 9, 2026
