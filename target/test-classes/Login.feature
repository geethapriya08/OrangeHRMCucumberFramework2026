# Login Feature File - Data Driven Testing with Excel and Refactored ExcelReader
# This file demonstrates advanced usage of the refactored ExcelReader with reusable methods

@DataDriven @Login
Feature: OrangeHRM Login - Data Driven Testing with Excel

  Background:
    Given I open the OrangeHRM login page

  @TC_001 @ValidLogin @BasicDataDriven
  Scenario: Login with Admin credentials from Excel
    Description: Test login with Admin user - uses simple getData() method
    When I log in with data from test case
    Then I should navigate to admin module
#    Then element with xpath "//div[@class='oxd-layout-context']" should be visible

  @TC_002 @ValidLogin @BasicDataDriven
  Scenario: Login with Employee credentials from Excel
    Description: Uses refactored ExcelReader's getData(rowHeader, columnHeader) method
    When I log in with data from test case TC_002
    Then element with xpath "//div[@class='oxd-layout-context']" should be visible

  @TC_003 @ValidLogin @BasicDataDriven
  Scenario: Login with Manager credentials from Excel
    Description: Demonstrates use of ExcelReader's reusable row and column lookup methods
    When I log in with data from test case TC_003
    Then element with xpath "//div[@class='oxd-layout-context']" should be visible

  @TC_004 @InvalidLogin @AdvancedDataDriven
  Scenario: Login with Invalid credentials and verify result
    Description: Uses ExcelReader's checkForPresenceOfValueForHeader method
    When I log in with test case TC_004 and verify result
    Then I capture login page screenshot

  @TC_001_WithDetails @ValidLogin @AdvancedDataDriven
  Scenario: Login and capture all test case details
    Description: Uses ExcelReader's getAllValuesForHeader method
    When I log in and capture all test case details TC_001
    Then element with xpath "//div[@class='oxd-layout-context']" should be visible

  @DirectLogin @TraditionalApproach
  Scenario Outline: Login with hardcoded credentials per test case
    Description: Traditional Gherkin approach without Excel
    When I log in with username "<username>" and password "<password>"
    Then element with xpath "<expectedElement>" should be visible

    Examples:
      | username   | password     | expectedElement                              |
      | admin      | admin123     | //div[@class='oxd-layout-context']          |
      | employee1  | emp123456    | //div[@class='oxd-layout-context']          |

  # Advanced Scenarios demonstrating ExcelReader capabilities

  @Advanced @ExcelValidation
  Scenario: Validate Admin login from Excel using reusable methods
    Description: Demonstrates:
    - loadTestDataFromExcel() helper method
    - ExcelReader's getData() with column headers
    - ExcelReader's checkForPresenceOfValueForHeader() method
    When I log in with data from test case TC_001
    Then element with xpath "//div[@class='oxd-layout-context']" should be visible

  @Advanced @ErrorHandling
  Scenario: Verify non-existent test case handling
    Description: Tests error handling when test case doesn't exist
    When I log in with test case INVALID_TC and verify result
    Then I capture login page screenshot

