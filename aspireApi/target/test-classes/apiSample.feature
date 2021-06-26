@functional
Feature: Asipration Sample Scenarios

  Scenario: user should be able to fetch the list of people
    Given the peoples endpoint exists
    When requested for list of people
    Then response status code should be 200
    And the total number of people equals to 82
    And  there are 10 people whose height is greater than 200 
    And the peoples names contains Darth Vader, Chewbacca, Roos Tarpals, Rugor Nass, Yarael Poof, Lama Su, Tuan Wu, Grievous, Tarfful, Tion Medon
   