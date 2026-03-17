# TC_001 Login Implementation - Complete Code Reference

## Files Involved (5 key files)

```
src/test/resources/
  ├── TestStub.xlsx                          [Excel Data]
  ├── Login.feature                          [Feature File]
  └── configuration.properties               [Config]

src/test/java/com/orangeHRM/stepDefinitions/
  └── LoginStepDefinitions.java              [Step Definition + Helpers]

src/main/java/com/orangeHrm/utils/
  └── ExcelReader.java                       [Refactored Reader]

src/main/java/com/orangeHrm/pages/
  └── BaseOrangeHRMLoginPageObjects.java     [Page Object]
```

---

## 1. Excel Data File (TestStub.xlsx)

### File Location
```
src/test/resources/TestStub.xlsx
```

### Sheet Structure: "LoginData"

```
┌─────────────────────────────────────────────────────────────────┐
│                    LoginData Sheet                              │
├─────────────────────────────────────────────────────────────────┤
│ Row 1 (Header):                                                 │
│  │ TestCaseID │ Username  │ Password  │ ExpectedResult          │
│  │ (Col 0)    │ (Col 1)   │ (Col 2)   │ (Col 3)                │
│
│ Row 2 (TC_001):
│  │ TC_001     │ Admin     │ admin123  │ Login Successful        │
│
│ Row 3 (TC_002):
│  │ TC_002     │ employee1 │ emp123456 │ Login Successful        │
│
│ Row 4 (TC_003):
│  │ TC_003     │ manager1  │ mgr123456 │ Login Successful        │
│
│ Row 5 (TC_004):
│  │ TC_004     │ invalidus │ wrongpass │ Login Failed            │
│
└─────────────────────────────────────────────────────────────────┘
```

### Data for TC_001
```
testCaseId = "TC_001"
username = "Admin"
password = "admin123"
expectedResult = "Login Successful"
```

---

## 2. Feature File (Login.feature)

**Location:** `src/test/resources/Login.feature`

### TC_001 Scenario

```gherkin
@DataDriven @Login
Feature: OrangeHRM Login - Data Driven Testing with Excel

  Background:
    Given I open the OrangeHRM login page

  @TC_001 @ValidLogin @BasicDataDriven
  Scenario: Login with Admin credentials from Excel
    Description: Test login with Admin user - uses simple getData() method
    When I log in with data from test case TC_001
    Then element with xpath "//div[@class='oxd-layout-context']" should be visible
```

### What Each Step Does

1. **Given I open the OrangeHRM login page**
   - Calls: `i_open_the_orange_hrm_login_page()`
   - Action: Navigates to login URL from config
   - Result: Login page is displayed

2. **When I log in with data from test case TC_001**
   - Calls: `i_log_in_with_test_case_data("TC_001")`
   - Action: Loads TC_001 data from Excel and performs login
   - Parameters: testCaseId = "TC_001"

3. **Then element with xpath should be visible**
   - Verifies dashboard is displayed
   - XPath: `//div[@class='oxd-layout-context']`

---

## 3. Step Definition Implementation

**File:** `src/test/java/com/orangeHRM/stepDefinitions/LoginStepDefinitions.java`

### Instance Variables
```java
private BaseOrangeHRMLoginPageObjects baseOrangeHRMLoginPageObjects;
private ExcelReader excelReader;
```

### Lazy Initialization Methods

#### A. Get Login Page Object
```java
private BaseOrangeHRMLoginPageObjects getLoginPageObject() {
    if (baseOrangeHRMLoginPageObjects == null) {
        WebDriver driver = Driver.getInstance();
        baseOrangeHRMLoginPageObjects = new BaseOrangeHRMLoginPageObjects(driver);
    }
    return baseOrangeHRMLoginPageObjects;
}
```
**Purpose:** Create page object only once, reuse for all steps

#### B. Get Excel Reader
```java
private ExcelReader getExcelReader() {
    if (excelReader == null) {
        excelReader = new ExcelReader();
    }
    return excelReader;
}
```
**Purpose:** Create ExcelReader only once, reuse for all data loading

### Background Step Definition

#### Step: "I open the OrangeHRM login page"
```java
@Given("I open the OrangeHRM login page")
public void i_open_the_orange_hrm_login_page() {
    String url = Configurations.getProperty("url_hrm");
    if (url == null || url.isEmpty()) {
        url = Configurations.getProperty("baseUrl");
    }
    if (url == null || url.isEmpty()) {
        logReportMessage("ERROR: Login URL not configured");
        throw new RuntimeException("Login URL not configured");
    }
    
    getLoginPageObject().open(url);
    logReportMessage("Successfully opened OrangeHRM login page: " + url);
}
```

### Data-Driven Step Definition (TC_001)

#### Step: "I log in with data from test case {string}"
```java
@When("I log in with data from test case {string}")
public void i_log_in_with_test_case_data(String testCaseId) {
    try {
        // STEP A: Load test data from Excel using helper method
        TestData testData = loadTestDataFromExcel(testCaseId, "LoginData");
        
        // STEP B: Log the loaded data
        logReportMessage("Loading test data from Excel:");
        logReportMessage("  Test Case ID: " + testData.testCaseId);
        logReportMessage("  Username: " + testData.username);
        logReportMessage("  Expected Result: " + 
            (testData.expectedResult != null ? testData.expectedResult : "N/A"));
        
        // STEP C: Perform login with loaded credentials
        getLoginPageObject().login(testData.username, testData.password);
        logReportMessage("Successfully logged in with test case data: " + testCaseId);
        
    } catch (InterruptedException e) {
        logReportMessage("Failed to login with test case data: " + testCaseId);
        SeleniumTestHelper.markCurrentThreadInterrupted();
        throw new RuntimeException("Login failed with test case: " + testCaseId, e);
    }
}
```

**Execution for TC_001:**
- `testCaseId` = "TC_001"
- Calls: `loadTestDataFromExcel("TC_001", "LoginData")`
- Returns: TestData object with Admin credentials
- Logs data being used
- Calls: `getLoginPageObject().login("Admin", "admin123")`

### Helper Method 1: Load Test Data from Excel

#### Method: `loadTestDataFromExcel(String testCaseId, String sheetName)`

```java
/**
 * Helper method to load test data from Excel sheet
 * Uses refactored ExcelReader reusable methods
 * @param testCaseId The test case ID to load
 * @param sheetName The sheet name to read from
 * @return TestData object with credentials and expected result
 */
private TestData loadTestDataFromExcel(String testCaseId, String sheetName) {
    ExcelReader reader = getExcelReader();
    reader.setReadingSheet(sheetName);
    
    // Uses new getData(rowHeader, columnHeader) method that leverages reusable helpers
    String username = reader.getData(testCaseId, "Username");
    String password = reader.getData(testCaseId, "Password");
    String expectedResult = reader.getData(testCaseId, "ExpectedResult");
    
    if (username == null || password == null) {
        throw new RuntimeException("Test case data not found for ID: " + testCaseId);
    }
    
    return new TestData(testCaseId, username, password, expectedResult);
}
```

**For TC_001:**
1. Gets ExcelReader instance
2. Sets sheet to "LoginData"
3. Calls: `reader.getData("TC_001", "Username")` → Returns "Admin"
4. Calls: `reader.getData("TC_001", "Password")` → Returns "admin123"
5. Calls: `reader.getData("TC_001", "ExpectedResult")` → Returns "Login Successful"
6. Validates data found (throws exception if null)
7. Returns: `new TestData("TC_001", "Admin", "admin123", "Login Successful")`

### Helper Method 2: Check if Test Case Exists

#### Method: `testCaseExists(String testCaseId, String sheetName)`

```java
/**
 * Helper method to verify if test case exists in Excel
 * Uses refactored ExcelReader's checkForPresenceOfValueForHeader method
 * @param testCaseId The test case ID to check
 * @param sheetName The sheet name to search
 * @return true if test case exists, false otherwise
 */
private boolean testCaseExists(String testCaseId, String sheetName) {
    ExcelReader reader = getExcelReader();
    reader.setReadingSheet(sheetName);
    
    // Uses refactored reusable method that efficiently searches for values
    return reader.checkForPresenceOfValueForHeader(testCaseId, testCaseId);
}
```

**Usage:** For advanced scenarios with validation

### Helper Method 3: Get All Test Case Values

#### Method: `getAllTestCaseValues(String testCaseId, String sheetName)`

```java
/**
 * Helper method to get all test credentials for a specific test case
 * Demonstrates usage of ExcelReader's getAllValuesForHeader method
 * @param testCaseId The test case ID
 * @param sheetName The sheet name
 * @return List of all values for the test case row
 */
private java.util.List<String> getAllTestCaseValues(String testCaseId, 
                                                      String sheetName) {
    ExcelReader reader = getExcelReader();
    reader.setReadingSheet(sheetName);
    
    // Uses refactored reusable method to get all values efficiently
    return reader.getAllValuesForHeader(testCaseId);
}
```

**For TC_001:** Returns `["TC_001", "Admin", "admin123", "Login Successful"]`

### Inner Class: TestData

```java
/**
 * Type-safe data container for test case information
 */
class TestData {
    String testCaseId;
    String username;
    String password;
    String expectedResult;
    
    public TestData(String testCaseId, String username, String password, 
                   String expectedResult) {
        this.testCaseId = testCaseId;
        this.username = username;
        this.password = password;
        this.expectedResult = expectedResult;
    }
    
    @Override
    public String toString() {
        return "TestData{" +
            "testCaseId='" + testCaseId + '\'' +
            ", username='" + username + '\'' +
            ", password='" + password + '\'' +
            ", expectedResult='" + expectedResult + '\'' +
            '}';
    }
}
```

**TC_001 Object:**
```java
TestData {
    testCaseId = "TC_001"
    username = "Admin"
    password = "admin123"
    expectedResult = "Login Successful"
}
```

---

## 4. ExcelReader - Refactored (4 Reusable Methods)

**File:** `src/main/java/com/orangeHrm/utils/ExcelReader.java`

### Overview of getData() Method

```java
public String getData(String rowHeader, String colHeader) {
    try {
        // Use Reusable Helper #1: Get column index from header name
        int colNum = getColumnIndexByHeader(colHeader);
        
        // Use Reusable Helper #2: Get row by header value
        Row row = getRowByHeader(rowHeader);
        
        if (row == null || colNum == -1) {
            return null;
        }
        
        Cell cell = row.getCell(colNum);
        
        // Use Reusable Helper #3: Safely get cell value
        return getCellStringValue(cell);
    } catch (Exception e) {
        return null;
    }
}
```

### Reusable Helper #1: getCellStringValue(Cell cell)

```java
/**
 * Safely extract string value from a cell with null checking
 * Used by: getData(), getAllValuesForHeader(), getColumnIndexByHeader()
 */
private String getCellStringValue(Cell cell) {
    if (cell == null) {
        return null;
    }
    
    try {
        if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue();
        } else if (cell.getCellType() == CellType.NUMERIC) {
            return String.valueOf(cell.getNumericCellValue());
        }
        return cell.toString();
    } catch (Exception e) {
        return null;
    }
}
```

### Reusable Helper #2: getRowByHeader(String headerValue)

```java
/**
 * Find a row by searching first column for matching value
 * Starts from row 2 (skips header row)
 * Uses: getCellStringValue() for safe string extraction
 * Optimization: Early exit when match found
 */
private Row getRowByHeader(String headerValue) {
    try {
        int rowCount = sheet.getLastRowNum();
        
        for (int i = 1; i <= rowCount; i++) {  // Start from row 2 (index 1)
            Row row = sheet.getRow(i);
            if (row == null) continue;
            
            Cell cell = row.getCell(0);  // First column
            String cellValue = getCellStringValue(cell);
            
            if (cellValue != null && cellValue.equals(headerValue)) {
                return row;  // Found! Return immediately
            }
        }
    } catch (Exception e) {
        // Log exception if needed
    }
    
    return null;  // Not found
}
```

**For TC_001:**
- Searches for "TC_001" in first column
- Finds row 2 (index 1)
- Returns Row object for row 2

### Reusable Helper #3: getColumnIndexByHeader(String headerName)

```java
/**
 * Find column index by searching first row (header row) for matching value
 * Uses: getCellStringValue() for safe string extraction
 * Returns: Column number (0-based), or -1 if not found
 */
private int getColumnIndexByHeader(String headerName) {
    try {
        Row headerRow = sheet.getRow(0);  // First row is header
        if (headerRow == null) {
            return -1;
        }
        
        short lastCellNum = headerRow.getLastCellNum();
        
        for (int i = 0; i < lastCellNum; i++) {
            Cell cell = headerRow.getCell(i);
            String cellValue = getCellStringValue(cell);
            
            if (cellValue != null && cellValue.equals(headerName)) {
                return i;  // Found! Return column number
            }
        }
    } catch (Exception e) {
        // Log exception if needed
    }
    
    return -1;  // Not found
}
```

**For TC_001:**
- **"Username"** search → Returns 1
- **"Password"** search → Returns 2
- **"ExpectedResult"** search → Returns 3

### Supporting Methods Used by getData()

#### checkForPresenceOfValueForHeader(String headerValue, String valueToCheck)
```java
public boolean checkForPresenceOfValueForHeader(String headerValue, 
                                               String valueToCheck) {
    try {
        Row row = getRowByHeader(headerValue);
        if (row == null) {
            return false;
        }
        
        // Uses: findValueIndexInRow()
        int colNum = findValueIndexInRow(row, valueToCheck);
        return colNum != -1;
    } catch (Exception e) {
        return false;
    }
}
```

#### getAllValuesForHeader(String headerValue)
```java
public List<String> getAllValuesForHeader(String headerValue) {
    try {
        List<String> valueList = new ArrayList<>();
        Row row = getRowByHeader(headerValue);
        
        if (row == null) {
            return valueList;
        }
        
        short lastCellNum = row.getLastCellNum();
        
        for (int i = 0; i < lastCellNum; i++) {
            Cell cell = row.getCell(i);
            String cellValue = getCellStringValue(cell);
            valueList.add(cellValue != null ? cellValue : "");
        }
        
        return valueList;
    } catch (Exception e) {
        return new ArrayList<>();
    }
}
```

**For TC_001:** Returns `["TC_001", "Admin", "admin123", "Login Successful"]`

---

## 5. Page Object - Login Actions

**File:** `src/main/java/com/orangeHrm/pages/BaseOrangeHRMLoginPageObjects.java`

### Login Method

```java
public void login(String username, String password) throws InterruptedException {
    // Enter username
    sendKeys(usernameTextBox, username);
    logMessage("Username entered: " + username);
    
    // Enter password
    sendKeys(passwordTextBox, password);
    logMessage("Password entered");
    
    // Click login button
    click(loginButton);
    logMessage("Login button clicked");
    
    // Wait for page load
    Thread.sleep(3000);
    logMessage("Waiting for page load");
}
```

**For TC_001:**
```java
login("Admin", "admin123");
```

---

## Complete Data Flow for TC_001 Login

```
START: Feature file execution

Step 1: "I open the OrangeHRM login page"
  ├─ Configurations.getProperty("url_hrm") → "http://localhost:8080"
  ├─ getLoginPageObject() → Create BaseOrangeHRMLoginPageObjects
  └─ getLoginPageObject().open(url) → Navigate to login page

Step 2: "I log in with data from test case TC_001"
  ├─ CALL: i_log_in_with_test_case_data("TC_001")
  │
  ├─ CALL: loadTestDataFromExcel("TC_001", "LoginData")
  │  ├─ getExcelReader() → Create ExcelReader instance
  │  ├─ reader.setReadingSheet("LoginData")
  │  │
  │  ├─ reader.getData("TC_001", "Username")
  │  │  ├─ getColumnIndexByHeader("Username")
  │  │  │  ├─ Search row 0 (header row)
  │  │  │  ├─ getCellStringValue(headerCell) for each cell
  │  │  │  ├─ FOUND at column 1 → return 1
  │  │  │
  │  │  ├─ getRowByHeader("TC_001")
  │  │  │  ├─ Search rows from 1 to max
  │  │  │  ├─ getCellStringValue(firstCell) for each row
  │  │  │  ├─ FOUND "TC_001" at row 2 → return Row 2
  │  │  │
  │  │  └─ getCellStringValue(cell[2][1])
  │  │     └─ Return "Admin"
  │  │
  │  ├─ reader.getData("TC_001", "Password")
  │  │  ├─ getColumnIndexByHeader("Password") → 2
  │  │  ├─ getRowByHeader("TC_001") → Row 2 (REUSED)
  │  │  └─ getCellStringValue(cell[2][2]) → "admin123"
  │  │
  │  ├─ reader.getData("TC_001", "ExpectedResult")
  │  │  ├─ getColumnIndexByHeader("ExpectedResult") → 3
  │  │  ├─ getRowByHeader("TC_001") → Row 2 (REUSED)
  │  │  └─ getCellStringValue(cell[2][3]) → "Login Successful"
  │  │
  │  └─ RETURN: TestData {
  │      testCaseId = "TC_001"
  │      username = "Admin"
  │      password = "admin123"
  │      expectedResult = "Login Successful"
  │  }
  │
  ├─ LOG: "Loading test data from Excel:"
  ├─ LOG: "  Test Case ID: TC_001"
  ├─ LOG: "  Username: Admin"
  ├─ LOG: "  Expected Result: Login Successful"
  │
  ├─ CALL: getLoginPageObject().login("Admin", "admin123")
  │  ├─ usernameTextBox.sendKeys("Admin")
  │  ├─ passwordTextBox.sendKeys("admin123")
  │  ├─ loginButton.click()
  │  └─ Wait 3000 ms
  │
  └─ LOG: "Successfully logged in with test case data: TC_001"

Step 3: "element with xpath should be visible"
  ├─ Wait for element: "//div[@class='oxd-layout-context']"
  ├─ IF visible → TEST PASSED ✅
  └─ IF not visible → TEST FAILED ❌

END: Feature file execution
```

---

## Key Points

| Feature | Implementation |
|---------|-----------------|
| **Excel Source** | `src/test/resources/TestStub.xlsx` with "LoginData" sheet |
| **Test Data** | TC_001: username="Admin", password="admin123" |
| **Step Definition** | `i_log_in_with_test_case_data(String testCaseId)` |
| **Helper Method** | `loadTestDataFromExcel(testCaseId, sheetName)` |
| **Reusable Methods** | 3 helper methods: getCellStringValue, getRowByHeader, getColumnIndexByHeader |
| **Data Object** | `TestData` class with 4 fields |
| **Page Object** | `BaseOrangeHRMLoginPageObjects.login(username, password)` |
| **Verification** | Dashboard element visibility check |

---

**Status:** ✅ Complete and Ready to Execute  
**Last Updated:** March 9, 2026
