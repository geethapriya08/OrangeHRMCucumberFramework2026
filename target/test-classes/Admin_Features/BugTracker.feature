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
@ReportBug
Feature: Bug Tracker Module
  As an Admin user i want to Report Bugs in thr Bug Tracker Module


  @ReportBug
  Scenario Outline: Report a Bug by adding valid data from excel
    Given I open the OrangeHRM login page (Bug Tracker Module)
    When I log in with username "<username>" and password "<password>"
    And I navigate to the Bug Tracker module
    And I should fill all the fields in Report Bugs
      | Field       | Value                   |
      | Category    | Database                |
      | Module      | Recruitment             |
      | Priority    | 5 - Medium              |
      | Summary     | Test Summary            |
      | Your Email  | y.geethapriya@gmail.com |
      | Description | Test Description        |

    Then click on Save button
    Examples:
      | username | password | status  |
      | admin    | admin    | success |

    

  #@tag2
  #Scenario Outline: Title of your scenario outline
    #Given I want to write a step with <name>
    #When I check for the <value> in step
    #Then I verify the <status> in step
#
    #Examples: 
      #| name  | value | status  |
      #| name1 |     5 | success |
      #| name2 |     7 | Fail    |
