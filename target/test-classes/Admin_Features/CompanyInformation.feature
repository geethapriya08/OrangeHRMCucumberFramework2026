#Author: Geetha Priya
#Keywords Summary : This feature is for verifying Company information menu options after login

@admin @company @smoke
Feature: Company Information Management
  As an admin user
  I want to manage company information
  So that I can maintain accurate company details

  Background:
    Given I am logged in as an admin user

  @TC_ADM_006 @TC-ADM-007 @high @positive
  Scenario Outline: Access Company General Information and View Company General Details
    When I navigate to Admin → Company Info → General
    Then the General company information page "<title>" should load
    And I should see the following company information:
      | Field          | FieldId         |
      | Company Name   | txtCompanyName  |
      | Tax ID         | txtTaxID        |
      | NAICS          | txtNAICS        |
      | Phone          | txtPhone        |
      | Country        | cmbCountry      |
      | Address1       | txtStreet1      |
      | Address2       | txtStreet2      |
      | City           | cmbCity         |
      | State/Province | txtState        |
      | ZIP Code       | txtZIP          |
      | Comments       | txtComments     |
    When I click the Edit button
    And I edit the company information details in the general section
    And I click the Save button
    Then the updated information should be reflected in the form
    And verify the company information details in database
    Examples:
      | title                  |
      | Company Info : General |

