Feature: Shopping Cart

  @browser
  Scenario: Add 2 the most expensive items to the shopping cart under category
    Given User opens Main page
    When User navigates to the category:
      | Zahrada, dílna a dům              |
      | Zahrada                           |
      | Zahradní domky, skleníky, pergoly |
      | Markýzy                           |
    And User sorts items by 'descending price'
    And Users adds 2 first items to the shopping cart
    Then Users sees 2 items in the shopping cart
