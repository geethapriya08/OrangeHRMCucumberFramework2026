#Author: your.email@your.domain.com
#Keywords Summary :
#Feature: List of scenarios.
#Scenario: Business rule through list of steps with arguments.
#Given: Some precondition step
#When: Some key actions
#Then: To observe outcomes or validation
#And,But: To enumerate more Given,When,Then steps
#Scenario Outline: List of steps for data-driven as an Examples and <placeholder>
#Examples: Container for s table
#Background: List of steps run before each of the scenarios
#""" (Doc Strings)
#| (Data Tables)
#@ (Tags/Labels):To group Scenarios
#<> (placeholder)
#""
## (Comments)
#Sample Feature Definition Template
@admin @navigation @smoke
Feature: Admin Module Navigation and Menu Access
  As an admin user
  I want to access and navigate through the Admin module
  So that I can manage system configuration and data

  Background:
    Given I am logged in as an admin user

  @TC-ADM-001 @high @positive
  Scenario: Verify Admin menu is visible after login
    When I hover on Admin menu
    Then the Admin menu should be visible in the below navigation panel
