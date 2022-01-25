Feature: Booking functionality
  Testing the booking functionality by performing end to end testing using CRUD operations

  @Regression
  Scenario: Admin should be able to generate a new auth token successfully by entering the
  valid username and password
    When Admin creates a new post request by entering username "admin" and password "password123" as payload
    Then Admin should see an auth token generated to use for future requests

  @Regression
  Scenario Outline: User should be able to perform CRUD operations successfully by entering valid details
    Given User extracts a list of existing ID's before creating a new record
    When  User creates a new booking using the correct details "<lastname>" "<totalprice>""<depositpaid>" "<additionalneeds>"
    And User verifies the new booking has been created successfully
    And User updates the created record by updating the firstname "<lastname>" "<totalprice>""<depositpaid>" "<additionalneeds>"
    And User verifies that the record has been updated successfully
    And User deletes the newly created record by providing the booking id
    Then User verifies that the record has been deleted successfully
    Examples:
      | lastname | totalprice | depositpaid | additionalneeds |
      | Smith    | 500        | true        | Vegetarian Food |






