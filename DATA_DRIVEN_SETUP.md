# OrangeHRM Data-Driven Framework Setup - Complete Guide

## Overview
This document explains the data-driven testing setup for the OrangeHRM Cucumber framework using Excel sheets.

## What's New

### 1. **ExcelReader Class Fixes**
All critical issues in `ExcelReader.java` have been resolved:
- ✅ Fixed `TestStubReader` class references → Changed to `ExcelReader`
- ✅ Replaced deprecated `getCellTypeEnum()` → Updated to `getCellType()`
- ✅ All methods now work with the correct class name

### 2. **TestStub.xlsx Creation**
A new Excel file has been created at: `src/test/resources/TestStub.xlsx`

**Sheet Name:** `LoginData`

**Test Data Available:**
| TestCaseID | Username | Password  | ExpectedResult |
|-----------|----------|-----------|-----------------|
| TC_001    | Admin    | admin123  | Login Successful |
| TC_002    | employee1| emp123456 | Login Successful |
| TC_003    | manager1 | mgr123456 | Login Successful |
| TC_004    | invaliduser | wrongpass | Login Failed |

### 3. **Enhanced LoginStepDefinitions**
New step definitions for data-driven login:

#### Step 1: Import ExcelReader
```java
import com.orangeHrm.utils.ExcelReader;
```

#### Step 2: New Data-Driven Login Step
```gherkin
When I log in with data from test case {string}
```

**Usage in Feature File:**
```gherkin
Scenario: Login with Admin credentials from Excel
  Given I open the OrangeHRM login page
  When I log in with data from test case TC_001
  Then dashboard should be visible
```

## How to Use

### Method 1: Data-Driven Login (Recommended)
```gherkin
Scenario: Admin Login
  Given I open the OrangeHRM login page
  When I log in with data from test case TC_001
  Then I should see dashboard
```

**How it works:**
1. Step reads from TestStub.xlsx, LoginData sheet
2. Looks for row with TestCaseID = TC_001
3. Extracts Username and Password columns
4. Performs login with those credentials

### Method 2: Direct Parameterized Login
```gherkin
Scenario: Custom Login
  Given I open the OrangeHRM login page
  When I log in with username "admin" and password "admin123"
  Then I should see dashboard
```

### Method 3: Using ConfigurationProperties (Legacy)
```gherkin
Scenario: Admin Login with Config
  Given I open the OrangeHRM login page
  When I log in with valid admin credentials
  Then I should see dashboard
```

## ExcelReader Key Methods

### Reading Data
```java
// Set the sheet to read from
excelReader.setReadingSheet("LoginData");

// Get data using Row Header and Column Header
String username = excelReader.getData("TC_001", "Username");
String password = excelReader.getData("TC_001", "Password");
```

### Writing Data (for runtime updates)
```java
// Add data to sheet
excelReader.addData("TestCaseID", "TC_005", 1);
excelReader.setValueAgainstAnotherValue("TestCaseID", "TC_005", "Username", "newuser");
excelReader.setValueAgainstAnotherValue("TestCaseID", "TC_005", "Password", "newpass");
```

## Adding New Test Cases

### Step 1: Add to Excel Sheet
Edit `src/test/resources/TestStub.xlsx`:
- Open in Excel or LibreOffice Calc
- Add new row with TestCaseID, Username, Password, ExpectedResult

Example:
| TestCaseID | Username | Password | ExpectedResult |
|-----------|----------|----------|-----------------|
| TC_005    | manager2 | mgr123789 | Login Successful |

### Step 2: Update Feature File
```gherkin
@TC_005 @ValidLogin
Scenario: Login with Manager2 credentials from Excel
  When I log in with data from test case TC_005
  Then I should see dashboard
```

### Step 3: Run the Test
```bash
mvn test -Dcucumber.filter.tags="@TC_005"
```

## File Structure
```
src/
├── main/java/com/orangeHrm/utils/
│   └── ExcelReader.java          # ✅ FIXED - All class references corrected
├── test/
│   ├── java/com/orangeHRM/stepDefinitions/
│   │   └── LoginStepDefinitions.java    # ✅ ENHANCED - Data-driven login step added
│   └── resources/
│       ├── Login.feature               # ✅ NEW - Data-driven login scenarios
│       └── TestStub.xlsx               # ✅ NEW - Login test data
```

## Running Tests

### Run Specific Test Case
```bash
mvn test -Dcucumber.filter.tags="@TC_001"
```

### Run All Login Tests
```bash
mvn test -Dcucumber.filter.tags="@DataDriven"
```

### Run All Feature Files
```bash
mvn test
```

## Troubleshooting

### Issue: "Test case data not found"
- **Solution:** Check TestCaseID in Excel matches exactly with feature file
- Make sure sheet name is "LoginData"

### Issue: "FileNotFoundException"
- **Solution:** Verify `src/test/resources/TestStub.xlsx` exists
- Run `python create_excel_sheet.py` to regenerate

### Issue: "getCellTypeEnum() not found"
- **Solution:** All deprecated methods have been fixed. Rebuild project:
```bash
mvn clean compile
```

## Best Practices

1. **Keep Excel Updated:** Update TestStub.xlsx when changing credentials
2. **Use Meaningful IDs:** Use descriptive TestCaseIDs (TC_ADMIN_VALID, etc.)
3. **Document Changes:** Add comments to feature files for test case purposes
4. **Separate Data:** Keep different test scenarios in separate rows
5. **Version Control:** Commit Excel file to repository for consistency

## Example: Complete Data-Driven Test Scenario
```gherkin
@DataDriven @Smoke
Scenario: Complete Login and Logout Flow
  Given I open the OrangeHRM login page
  When I log in with data from test case TC_001
  Then element with xpath "//div[@class='oxd-layout-context']" should be visible
  And I capture login page screenshot
  When I log out
  Then I should see login page
```

## Support & Next Steps

1. ✅ ExcelReader is now fully functional
2. ✅ Login data-driven framework is ready
3. Next: Extend to other modules using the same pattern
4. Consider: Creating separate Excel sheets for different modules (Admin, PIM, Leave, etc.)

---
**Last Updated:** 2026-02-09  
**Status:** ✅ All Issues Resolved - Framework Ready for Data-Driven Testing
