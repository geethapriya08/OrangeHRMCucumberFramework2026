@tag
Feature: Title of your feature
  I want to use this template for my feature file

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