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
@tag
Feature: Title of your feature
  I want to use this template for my feature file

  @tag1
  Scenario: Verify employee creation in OrangeHRM
    Given I open the OrangeHRM login page
    When I log in with valid admin credentials
    And I navigate to the Admin module
    And I enter all company information details
#    And I add a new employee with required details


  @admin2
  Scenario Outline: Verify employee creation in OrangeHRM via examples
    Given I open the OrangeHRM login page
    When I log in with username "<username>" and password "<password>"
    #And I navigate to the Admin module
    And I navigate to the PIM module
    And I add a new employee with first name "<firstName>" and last name "<lastName>"

    Examples:
      | username | password | firstName | lastName | status  |
      | admin    | admin    | John      | Doe      | success |
      #| admin    | admin    | Jane      | Smith    | success |

