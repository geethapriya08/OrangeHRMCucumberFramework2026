@tag
Feature: Title of your feature
  I want to use this template for my feature file

  Background:
    Given I open the OrangeHRM login page

  @admin3
  Scenario: Verify employee creation in OrangeHRM
    #Given I open the OrangeHRM login page
    When I log in with data from test case
    And I navigate to specific holiday module
    And I click on add button to define specific holidays
    And I enter all Define Days Off : Specific Holidays details