@tag
Feature: Title of your feature
  I want to use this template for my feature file

  @leave1
  Scenario: Verify employee creation in OrangeHRM
    Given I open the OrangeHRM login page
    When I log in with valid admin credentials
    And I navigate to the leave module
    And I should view all employee leave details