
# Stage 1 → Stage 2: Customer Behaviour Extension

[Back to Main README](../../README.md)

**GitHub Comparison:** [View Stage 1 → Stage 2 Changes](https://github.com/u-zhaofeng/SC2002-Assignment/compare/stage-1...stage-2)


## 1. Overview

In Stage 1, we developed the basic restaurant management system,
which included regular customers, the Waiter, Chef, and Cashier,
as well as the main restaurant operations such as customer
arrivals, ordering, food preparation, serving, and payment.

Building on Stage 1, Stage 2 introduced two additional customer
types, VIPCustomer and CriticCustomer, each with their own
behaviours and satisfaction loss rates. VIP customers receive
a 10% discount and lose satisfaction more slowly, while Critic
customers lose satisfaction faster and have a preference for
ordering Burger.

To accommodate these changes, we extended the existing Customer
class using inheritance and polymorphism rather than modifying
the overall restaurant workflow. This allowed each customer type
to have its own behaviour while continuing to use the same
ordering, preparation, serving, and payment processes.


### GitHub Change Summary

| Metric | Result |
|---|---|
| Commits | 9 |
| Files changed | 6 |
| Lines added | 121 |
| Lines deleted | 47 |
| New classes | 3 |
| Modified classes | 3 |
| Net deleted classes | 0 |


## 2. New Classes Added

The following classes were introduced in Stage 2.

| Class | Responsibility | Reason for Addition |
|---|---|---|
| `VIPCustomer.java` | Extends Customer and implements VIP-specific satisfaction and discount behaviour. | Supports VIP customers without changing the main restaurant workflow. |
| `CriticCustomer.java` | Extends Customer and implements Critic-specific satisfaction and food selection behaviour. | Supports a customer type with different waiting penalties and food preferences. |
| `CustomerFactory.java` | Creates the appropriate Customer subclass using the customer number. | Centralizes customer creation and reduces direct subclass instantiation in the game controller. |


### VIPCustomer.java

VIPCustomer extends the existing Customer class.

It overrides the following methods:

- `getTypeLabel()` — Returns "VIP".
- `getWaitingSatisfactionLoss()` — Returns 5 instead of 8.
- `calculateBillCents()` — Applies a 10% discount to the standard menu price.

This allows VIP customers to receive different treatment while reusing the inherited customer attributes and methods.

### CriticCustomer.java

CriticCustomer extends Customer and overrides:

- `getTypeLabel()` — Returns "Critic".
- `getWaitingSatisfactionLoss()` — Returns 12.
- `chooseFood()` — Prioritizes Burger when available.

If Burger is unavailable, the implementation falls back to the inherited food selection behaviour.

### CustomerFactory.java

CustomerFactory centralizes customer creation through the method:

`createCustomer(int customerNo)`

The factory creates:

- Customer 2 → VIPCustomer
- Customer 3 → CriticCustomer
- All other customers → Regular Customer

This allows the game controller to request a customer object without directly constructing each subclass.

## 3. Existing Classes Modified

Three existing classes were modified during the transition.

| Class | Modification | Reason |
|---|---|---|
| `Customer.java` | Added methods for customer type, satisfaction penalties, food selection, and bill calculation. | Allows subclasses to override customer-specific behaviour. |
| `Order.java` | Added `billCents`, an overloaded constructor, and `getBillCents()`. | Stores the actual customer bill instead of assuming every customer pays the standard menu price. |
| `RestaurantRushGame.java` | Integrated CustomerFactory and delegated customer-specific decisions to Customer objects. | Supports multiple customer types without rewriting the overall turn sequence. |


## 4. Detailed Code Changes

### 4.1 Customer.java

In Stage 1, all customers shared the same satisfaction penalty, pricing rules, and deterministic food selection.

Stage 2 introduced the following methods:

| Method | Purpose |
|---|---|
| `getTypeLabel()` | Returns the customer's type. |
| `getWaitingSatisfactionLoss()` | Returns the applicable waiting penalty. |
| `applyWaitingPenalty()` | Applies the customer's satisfaction penalty. |
| `chooseFood(List<MenuItem> menu)` | Determines the customer's food selection. |
| `calculateBillCents(MenuItem item)` | Calculates the customer's bill. |


These methods allow VIPCustomer and CriticCustomer to override their own behaviour.

For example, the default Customer has a satisfaction loss of 8, while VIPCustomer returns 5 and CriticCustomer returns 12.

This removes the need for the main controller to handle different satisfaction penalties using customer-type checks.

### 4.2 Order.java

Stage 1 retrieved the standard price directly from MenuItem.

Stage 2 introduced:

- `private final int billCents`
- `Order(MenuItem item, int billCents)`
- `getBillCents()`

The original `Order(MenuItem item)` constructor was retained and delegates to the new constructor using the standard price.

This allows orders to store a calculated price while preserving the existing constructor for standard orders.

The change also ensures that customer-specific discounts are reflected correctly during payment.

### 4.3 RestaurantRushGame.java

Several changes were made to support Stage 2.

**A. Customer Creation**

Stage 1 created customers directly:

`new Customer(currCustomerNo)`

Stage 2 uses:

`customerFactory.createCustomer(currCustomerNo)`

This enables the creation of different customer subclasses.

**B. Customer Food Selection**

Stage 1 selected food within the controller.

Stage 2 delegates food selection to:

`cust.chooseFood(menu)`

This allows CriticCustomer to implement a distinct food preference without requiring additional checks in the game controller.

**C. Satisfaction Penalties**

The original fixed satisfaction reduction:

`cust.decreaseSatisfaction(8)`

was replaced with:

`cust.applyWaitingPenalty()`

The appropriate satisfaction loss is now determined by the customer object.

**D. Payment Calculation**

Stage 1 retrieved the item's standard price.

Stage 2 retrieves the calculated amount through:

`order.getBillCents()`

This ensures that customer discounts are reflected correctly in revenue.

**E. Game Display**

The controller was updated to display customer type labels in:

- Waiting queue information.
- Occupied table information.
- Food selection messages.
- Payment messages.
- Customer abandonment messages.

This allows the player to distinguish between Regular, VIP, and Critic customers during gameplay.

## 5. Object-Oriented Design Considerations

### 5.1 Inheritance

VIPCustomer and CriticCustomer inherit from Customer.

They reuse the existing customer's common attributes and operations while introducing specialized behaviour through method overriding.

This reduces duplication between customer types.

### 5.2 Polymorphism

The game controller interacts with customers through Customer references.

For example:

`cust.applyWaitingPenalty()`

Java determines the correct method implementation based on the customer's actual object type.

This allows different customer types to use the same game workflow.

### 5.3 Encapsulation

Order stores the customer's calculated bill using a private final attribute.

The bill can be accessed through:

`getBillCents()`

This prevents other classes from directly modifying the stored bill.

### 5.4 Open/Closed Principle

The Customer class supports new customer behaviours through inheritance and method overriding.

For example, additional customer types could override `getWaitingSatisfactionLoss()` or `chooseFood()` without modifying the existing waiting-penalty or order-selection workflow.

However, registering new customer types still requires changes to CustomerFactory.

Therefore, the design supports extensibility through polymorphism, although the factory is not completely closed to modification.

## 6. Impact on Existing Functionality

The Stage 2 implementation retains the original restaurant workflow:

1. Customers arrive according to the scheduled turns.
2. The Waiter seats customers, takes orders, and serves food.
3. The Chef advances food preparation.
4. The Cashier processes eligible payments.
5. Customers lose satisfaction while waiting.
6. The game determines victory or defeat after 18 turns.

The main difference is that individual Customer objects now determine their own satisfaction penalties, food preferences, and pricing.

### Regression Testing

The following Stage 1 behaviours should be verified in Stage 2:

| Test | Expected Result | Actual Result |
|---|---|---|
| Customer arrivals | Customers arrive on the scheduled turns | PASSED|
| Waiter actions | At most one Waiter task per turn | PASSED |
| Chef preparation | One preparation unit per turn | PASSED |
| Customer payment | Cashier charges eligible customers | PASSED |
| Victory condition | All three objectives are satisfied | PASSED |
| Defeat condition | At least one objective is not satisfied | PASSED |


Actual testing evidence will be recorded in [Testing Documentation](../../tests/README.md).


## 7. Design Evaluation

One of the key improvements introduced in Stage 2 was the
ability to support different customer behaviours without
significantly modifying the existing restaurant operations.

By applying inheritance and polymorphism, VIPCustomer and
CriticCustomer could reuse the common attributes and methods
defined in Customer while overriding specific methods to
implement their respective behaviours. This reduced code
duplication and allowed the existing restaurant workflow to
remain largely unchanged.

We also introduced CustomerFactory to centralize the creation
of different customer types, reducing the responsibility of
RestaurantRushGame for customer instantiation. In addition,
Order was modified to store the calculated bill amount rather
than relying solely on the standard menu price. This supported
the introduction of VIP discounts and provided a foundation
for additional pricing features in later stages.

Overall, these changes improved the organization and
extensibility of our code while maintaining the functionality
developed in Stage 1. The implementation also demonstrated
how object-oriented principles could be applied to accommodate
new requirements without requiring major changes to the
existing system.

