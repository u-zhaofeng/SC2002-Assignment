
# Stage 2 → Stage 3: New Role and Menu Policy

[Back to Main README](../../README.md)

**GitHub Comparison:** [View Stage 2 → Stage 3 Changes](https://github.com/u-zhaofeng/SC2002-Assignment/compare/stage-2...stage-3)

## 1. Overview

Stage 2 introduced different customer types with their own satisfaction penalties, pricing and food preferences, while retaining the basic restaurant management workflow developed in Stage 1.

Building on Stage 2, Stage 3 introduced three additional features: Host, ComboMeal and Happy Hour. The Host provides an additional seating option, ComboMeal allows customers to order a combination of two existing menu items, and Happy Hour introduces temporary discounts and satisfaction recovery.

To accommodate these changes, we introduced new classes and modified parts of the existing implementation. In particular, SeatingService was introduced to manage seating operations shared by the Host and Waiter, while HappyHourPolicy handles the new pricing and satisfaction recovery rules.

These changes allowed us to extend the restaurant's functionality while retaining the existing customer, ordering, preparation and payment processes.

### GitHub Change Summary

| Metric | Result |
|---|---|
| Commits | 17 |
| Files changed | 7 |
| Lines added | 211 |
| Lines deleted | 22 |
| New classes | 4 |
| Modified classes | 3 |
| Net deleted classes | 0 |


## 2. New Classes Added

Four classes were introduced in Stage 3 to implement the additional requirements.

| Class | Responsibility | Reason for Addition |
|---|---|---|
| `Host.java` | Provides an additional staff role that seats queued customers. | Allows customers to be seated independently of the Waiter's assigned task. |
| `SeatingService.java` | Validates and performs customer seating operations. | Prevents duplication of seating logic between Host and Waiter. |
| `ComboMeal.java` | Extends MenuItem and combines two existing menu items. | Supports combo meals while reusing the existing menu and order-processing structure. |
| `HappyHourPolicy.java` | Manages Happy Hour activation, duration, discounts and satisfaction recovery. | Separates temporary pricing and recovery rules from the main restaurant controller. |


### 2.1 Host.java

Host was introduced as an additional staff role responsible for seating customers.

The class provides two main methods:

- `getTasks()` — Determines whether seating is available based on the waiting queue and available tables.
- `seatCustomer()` — Delegates the seating operation to SeatingService.

Unlike the Waiter, the Host is only responsible for seating customers or waiting.

This allows the Host and Waiter to perform separate tasks within the same turn, providing the manager with greater flexibility when assigning staff.

### 2.2 SeatingService.java

SeatingService was introduced to handle the common seating operations used by both Host and Waiter.

Its main method is:

`seatCustomer(Customer customer, Table table, WaitingQueue queue)`

Before seating a customer, the method checks that:

1. The customer, table and waiting queue are valid.
2. The selected table is unoccupied.
3. The customer is currently in the waiting queue.

If these conditions are satisfied, the service:

- Assigns the customer to the selected table.
- Removes the customer from the waiting queue.
- Updates the customer's status to `SEATED`.
- Returns `true` to indicate a successful operation.

Otherwise, it returns `false`.

By moving this logic into a separate class, both Host and Waiter can use the same seating process without implementing duplicate validation and status updates.

### 2.3 ComboMeal.java

ComboMeal was introduced to support orders consisting of two existing menu items.

The class extends MenuItem and stores references to its two component items through the attributes:

- `first`
- `second`

The constructor calculates the total preparation units by adding the preparation requirements of both components.

The combo price is calculated as 90% of the combined item prices, rounded to the nearest cent.

For example:

| Item | Price | Preparation Units |
|---|---|---|
| Salad | $9.00 | 1 |
| Pasta | $18.00 | 2 |
| Salad + Pasta Combo | $24.30 | 3 |


The combo price is calculated as:

`($9.00 + $18.00) × 0.90 = $24.30`

ComboMeal also provides:

- `getFirstItem()`
- `getSecondItem()`

These methods allow the component items to be retrieved when needed.

As ComboMeal inherits from MenuItem, it can be included in the existing menu and processed through the same ordering and preparation workflow as other dishes.

### 2.4 HappyHourPolicy.java

HappyHourPolicy was introduced to manage the temporary Happy Hour feature.

The policy can be activated once per game and remains active for two consecutive turns.

Its main methods include:

| Method | Responsibility |
|---|---|
| `hasBeenActivated()` | Checks whether Happy Hour has already been used. |
| `activate(int turnNo)` | Activates Happy Hour during the selected turn. |
| `isActive(int turnNo)` | Checks whether Happy Hour is active in the current turn. |
| `remainingTurns(int turnNo)` | Returns the remaining active duration. |
| `billCents()` | Calculates the final price after applicable discounts. |
| `recoverWaitingCustomers()` | Restores satisfaction for eligible customers. |


During Happy Hour, orders taken in an active turn receive an additional 20% discount.

The implementation applies any customer-specific discount first, followed by the Happy Hour discount.

For example, a VIP customer ordering a Burger during Happy Hour pays:

`$15.00 × 0.90 × 0.80 = $10.80`

The final amount is stored when the order is placed, ensuring that the bill does not change after Happy Hour ends.

Happy Hour also restores 15 satisfaction points to customers waiting in the queue or seated and still waiting for food.

The customer's satisfaction is capped at 100.

## 3. Existing Classes Modified

Three existing classes were modified during the Stage 2 to Stage 3 transition.

| Class | Modification | Reason |
|---|---|---|
| `Customer.java` | Added an overloaded `setOrder()` method that accepts a calculated bill amount. | Allows orders to store prices calculated using Happy Hour and customer-specific discounts. |
| `Waiter.java` | Delegated seating to SeatingService and added an overloaded `takeOrder()` method. | Reuses common seating logic and supports orders with calculated prices. |
| `RestaurantRushGame.java` | Integrated Host, ComboMeal and HappyHourPolicy into the game setup and turn sequence. | Coordinates the new Stage 3 features while retaining the existing restaurant workflow. |


## 4. Detailed Code Changes

### 4.1 Customer.java

In Stage 2, Customer already supported customer-specific bill calculations through `calculateBillCents()`.

Stage 3 introduced an overloaded method:

`setOrder(MenuItem orderItem, int billCents)`

This allows the calculated bill amount to be passed directly when creating an Order.

The existing `setOrder(MenuItem orderItem)` method was retained and updated to delegate to the new overloaded method.

This maintains compatibility with standard orders while allowing additional pricing policies to determine the final amount charged.

### 4.2 Waiter.java

Two main changes were introduced in Waiter.

**A. Delegating Seating Operations**

Previously, Waiter handled seating validation, table assignment, waiting queue removal and customer status updates directly.

In Stage 3, a SeatingService object was introduced:

`private final SeatingService seating = new SeatingService();`

The Waiter's `seatCustomer()` method now delegates the operation to:

`seating.seatCustomer(cust, table, waitingQueue)`

This allows Host and Waiter to share the same seating implementation.

**B. Supporting Calculated Order Prices**

Stage 3 introduced an overloaded method:

`takeOrder(Table table, MenuItem orderItem, int billCents)`

The method validates the selected table and customer before creating an order using the supplied bill amount.

The original `takeOrder(Table table, MenuItem orderItem)` method was retained.

This allows the existing ordering functionality to continue working while supporting Happy Hour discounts.

### 4.3 RestaurantRushGame.java

The main game controller was modified to coordinate the additional Stage 3 features.

**A. New Staff and Policy Objects**

The following fields were introduced:

- `private Host host`
- `private HappyHourPolicy happyHour`

Both objects are initialized during the default game setup.

**B. ComboMeal Registration**

A ComboMeal was added to the existing menu using Salad and Pasta as its components.

This makes ComboMeal available through the existing customer food selection process.

**C. Happy Hour Activation**

The game controller now calls:

`processHappyHourChoice(turnNo)`

This allows the player to activate Happy Hour at the beginning of a turn, provided it has not already been activated.

The controller also displays the current Happy Hour status and remaining duration.

**D. Customer Satisfaction Recovery**

After processing customer arrivals, the controller invokes:

`happyHour.recoverWaitingCustomers(waitingQueue, tables, turnNo)`

This applies satisfaction recovery to eligible customers during active Happy Hour turns, before staff actions are selected.

**E. Host Task Selection**

A new method, `processHostChoice()`, was introduced to allow the manager to choose between seating a customer and waiting.

The Host acts separately from the Waiter, allowing both staff members to complete their respective tasks during the same turn.

**F. Happy Hour Pricing**

Previously, the Waiter used the customer's calculated price directly.

Stage 3 introduced:

`happyHour.billCents(cust, orderItem, turnNo)`

The calculated amount is then passed to:

`waiter.takeOrder(table, orderItem, billCents)`

This ensures the final price accounts for both customer-specific discounts and any active Happy Hour discount.

## 5. Object-Oriented Design Considerations

### 5.1 Single Responsibility Principle

SeatingService was introduced to separate seating validation and table assignment from the responsibilities of Host and Waiter.

Rather than allowing both staff classes to implement the same seating logic, they delegate the operation to a shared service.

Similarly, HappyHourPolicy encapsulates Happy Hour activation, duration, pricing and satisfaction recovery rules.

This helps reduce the amount of feature-specific logic that must be implemented directly within the staff classes.

### 5.2 Inheritance

ComboMeal extends MenuItem and reuses the existing menu item attributes and operations.

This allows ComboMeal to be treated as a regular MenuItem within the ordering and preparation workflow.

The existing customer inheritance structure from Stage 2 remains unchanged.

### 5.3 Composition

ComboMeal uses composition to store references to two MenuItem objects.

The combo's price and preparation requirements are calculated from these component items.

This allows the implementation to reuse existing menu items instead of duplicating their attributes or creating separate preparation rules.

### 5.4 Encapsulation

HappyHourPolicy maintains its activation state using the private attribute:

`activatedTurn`

Other classes interact with the policy through methods such as `activate()`, `isActive()` and `remainingTurns()`.

This prevents the game controller from directly modifying the internal activation state.

Similarly, ComboMeal keeps its component items private and provides getter methods to access them.

### 5.5 Delegation and Code Reuse

Both Host and Waiter delegate customer seating to SeatingService.

This ensures that the same validation and seating rules apply regardless of which staff member performs the task.

If the seating rules need to be updated in the future, the changes can be made within SeatingService instead of modifying both staff classes separately.

## 6. Impact on Existing Functionality

Stage 3 was developed to extend the existing restaurant operations while preserving the functionality established in earlier stages.

The following behaviours remain part of the system:

1. Customers arrive according to the predetermined schedule.
2. The Waiter can still seat customers, take orders and serve food.
3. The Chef continues preparing orders according to their preparation units.
4. The Cashier automatically handles eligible payments.
5. VIP and Critic customers retain their customer-specific behaviour.
6. Customer satisfaction and abandonment rules remain applicable.
7. Victory and defeat are determined after 18 turns.

The main additions are the separate Host seating action, ComboMeal ordering and Happy Hour pricing and satisfaction recovery.

### Regression Testing

The following functionality should be verified using the Stage 3 version.

| Test | Expected Result | Actual Result |
|---|---|---|
| Regular customer behaviour | Original satisfaction and ordering rules remain correct | PASSED |
| VIP customer pricing | VIP receives a 10% discount | PASSED |
| Critic behaviour | Critic retains its distinct satisfaction loss and food preference | PASSED |
| Waiter seating | Waiter can still seat a queued customer | PASSED |
| Host seating | Host can seat one queued customer independently | PASSED |
| ComboMeal pricing | Combo price equals 90% of combined item prices | PASSED |
| ComboMeal preparation | Preparation units equal the sum of component units | PASSED |
| Happy Hour activation | Can only be activated once per game | PASSED |
| Happy Hour pricing | Additional 20% discount during active turns | PASSED |
| Satisfaction recovery | Eligible customers recover 15 points, capped at 100 | PASSED |
| Victory and defeat | Original game objectives remain unchanged | PASSED |


Actual results will be documented in [Testing Documentation](../../tests/README.md).

## 7. Design Evaluation

Stage 3 introduced additional features that required changes to staff responsibilities, menu items and pricing behaviour. One of the main considerations was how to incorporate these features without significantly affecting the functionality developed in the earlier stages.

The introduction of SeatingService allowed us to separate the seating process from the individual staff classes. Both Host and Waiter could then use the same seating logic, reducing code duplication and ensuring that the seating rules remained consistent.

We also extended MenuItem through ComboMeal, allowing the existing ordering and preparation processes to handle combination meals without requiring a separate workflow. Similarly, HappyHourPolicy was introduced to manage the temporary discount and satisfaction recovery rules, rather than placing these calculations directly within the Waiter class.

Although RestaurantRushGame still coordinates several aspects of the game, the introduction of these additional classes helped distribute specific responsibilities across the implementation. One possible improvement would be to further separate user interaction from the game controller to improve maintainability as additional features are introduced.

Overall, Stage 3 demonstrated how the existing object-oriented design could be extended to accommodate new requirements while maintaining much of the functionality developed in Stages 1 and 2.
