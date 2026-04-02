#Author: Geetha Priya
#Keywords Summary : This feature is for verifying Admin menu options after login

@admin @navigation @smoke
Feature: Admin Module Navigation and Menu Access
  As an admin user
  I want to access and navigate through the Admin module
  So that I can manage system configuration and data

  Background:
    Given I am logged in as an admin user

  @TC-ADM-001 @TC-ADM-002 @TC-ADM-003 @TC-ADM-004 @TC-ADM-005 @Verify_Admin_module_menu_navigations_after_login @high @positive
  Scenario: Verify Admin menu options is visible after login
    When I hover on Admin menu
    #Then the Admin menu should be visible in the below navigation panel
    Then verify all the admin menu options as below:
      | Company Info        |
      | Job                 |
      | Qualification       |
      | Skills              |
      | Memberships         |
      | Nationality & Race  |
      | Users               |
      | Email Notifications |
      | Project Info        |
      | Data Import/Export  |
      | Custom Fields       |

    And I hover over or click on Company Info
    Then I should see the following sub-items:
      | General           |
      | Locations         |
      | Company Structure |
      | Company Property  |
    And I expand the Job submenu
    Then I should see the following sub-items of Job:
      | Job Titles         |
      | Job Specifications |
      | Pay Grades         |
      | Employment Status  |
      | EEO Job Categories |
    And I hover over or click on Qualification
    Then I should see the following sub-items of Qualification:
      | Education |
      | Licenses  |


