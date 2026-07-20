#Author: Geetha Priya
#Keywords Summary : This feature is for verifying Admin menu options after login
@admin @navigation @smoke
Feature: Admin Module Navigation and Menu Access
  As an admin user
  I want to access and navigate through the Admin module
  So that I can manage system configuration and data

  Background: 
    Given I am logged in as an admin user

  @TC-ADM-001 @TC-ADM-002 @TC-ADM-003 @TC-ADM-004 @TC-ADM-005 @Verify_Admin_module_menu_navigations_after_login @high @positive @regression
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
    And I hover over or click on Skills
    Then I should see the following sub-items of Skills:
      | Skills    |
      | Languages |
    And I hover over or click on Memberships
    Then I should see the following sub-items of Memberships:
      | Membership Types |
      | Memberships      |
    And I hover over or click on Nationality & Race
    Then I should see the following sub-items of Nationality & Race:
      | Nationality  |
      | Ethnic Races |
    And I hover over or click on Users
    Then I should see the following sub-items of Users:
      | HR Admin Users    |
      | ESS Users         |
      | Admin User Groups |
    And I hover over or click on Email Notifications
    Then I should see the following sub-items of Email Notifications:
      | Configuration |
      | Subscribe     |
    And I hover over or click on Project Info
    Then I should see the following sub-items of Project Info:
      | Customers          |
      | Projects           |
      | Project Activities |
    And I hover over or click on Data Import/Export
    Then I should see the following sub-items of Data Import/Export:
      | Define Custom Export |
      | Export               |
      | Define Custom Import |
      | Import               |
    And I hover over or click on Custom Fields
