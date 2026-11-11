
# Restaurant Rush — Testing Documentation

## 1. Overview

This document records the testing carried out for our SC2002 Restaurant Rush project across all three development stages.

We used Eclipse IDE to test the Java implementations. To make our tests reproducible, we created text files containing predetermined console inputs. These files were supplied to the game through Eclipse Run Configurations, allowing us to execute complete gameplay scenarios without manually entering every action.

Our testing focused on:

- Verifying that each development stage runs successfully.
- Checking the victory and defeat conditions after 18 turns.
- Testing customer seating, ordering, food preparation, serving and payment.
- Verifying customer satisfaction and abandonment.
- Checking the additional VIP and Critic behaviours introduced in Stage 2.
- Testing the Host, ComboMeal and Happy Hour features introduced in Stage 3.
- Checking input validation and discount calculations.

All seven completed automated test scenarios produced their expected outcomes.

---

## 2. Testing Environment

| Component | Details |
|---|---|
| Programming language | Java |
| Development environment | Eclipse IDE |
| Version control | Git (Eclipse EGit) |
| Testing approach | Automated console-input functional testing |
| Game duration | 18 turns |
| Historical versions | `stage-1`, `stage-2`, `stage-3` Git tags |

The historical Stage 1 and Stage 2 implementations were tested using a separate Git clone to avoid changing our main working repository.

Stage 3 functional testing was conducted on the reorganized working version of the project.

The input and output files are stored in the `tests` directory of our main repository.

## 3. Folder Structure

```text
tests/
├── README.md
├── inputs/
│   ├── stage1-victory-input.txt
│   ├── stage1-defeat-input.txt
│   ├── stage2-victory-input.txt
│   ├── stage3-victory-input.txt
│   ├── stage3-defeat-invalid-input.txt
│   └── stage3-combined-discount-input.txt
└── outputs/
    ├── stage1-victory-output.txt
    ├── stage1-defeat-output.txt
    ├── stage2-victory-output.txt
    ├── stage2-defeat-output.txt
    ├── stage3-victory-output.txt
    ├── stage3-defeat-output.txt
    └── stage3-combined-discount-output.txt
```

Stage 2's defeat test reuses `stage1-defeat-input.txt` because both versions support the same Waiter and Chef wait actions.

The `inputs` folder contains the menu choices used in our tests.

The `outputs` folder contains the complete console logs recorded during execution.

---

## 4. Automated Test Summary

| Test ID | Stage | Test Scenario | Result |
|---|---|---|---|
| ST1-01 | Stage 1 | Full gameplay victory | PASS |
| ST1-02 | Stage 1 | Full gameplay defeat | PASS |
| ST2-01 | Stage 2 | Victory with VIP and Critic behaviour | PASS |
| ST2-02 | Stage 2 | Defeat and satisfaction penalties | PASS |
| ST3-10 | Stage 3 | Full gameplay victory | PASS |
| ST3-11 | Stage 3 | Invalid inputs and gameplay defeat | PASS |
| ST3-12 | Stage 3 | VIP and Happy Hour combined discounts | PASS |

### Victory Result Comparison

| Metric | Stage 1 | Stage 2 | Stage 3 |
|---|---:|---:|---:|
| Paid customers | 4 | 4 | 5 |
| Served count | 4 | 4 | 5 |
| Unhappy departures | 0 | 0 | 0 |
| Revenue | $51.00 | $46.50 | $69.00 |
| Average satisfaction | 80.00 | 83.50 | 87.60 |
| Turns used | 18 | 18 | 18 |
| Final outcome | VICTORY | VICTORY | VICTORY |

All three victory scenarios satisfied the required conditions:

- At least 4 paid customers.
- At least $45.00 in revenue.
- Average paid customer satisfaction of at least 60.
- Game completion after 18 turns.

---

# 5. Stage 1 — Basic Restaurant Operation

Stage 1 implements the core restaurant workflow, including customer arrivals, seating, ordering, preparation, serving, automatic payment and satisfaction tracking.

Testing was conducted against the historical `stage-1` Git tag.

## ST1-01 — Full Gameplay Victory

**Objective:** Verify that the basic restaurant workflow works correctly and allows the player to achieve the victory conditions.

**Input:** [stage1-victory-input.txt](inputs/stage1-victory-input.txt)

### Expected Results

1. Customers can be seated by the Waiter.
2. Seated customers can place orders.
3. The Chef prepares orders according to their preparation requirements.
4. Ready orders can be served.
5. Customers finish eating before becoming eligible for payment.
6. Completed payments increase revenue and free tables.
7. The game displays `VICTORY!` after 18 turns when the required conditions are met.

### Actual Results

| Metric | Result |
|---|---:|
| Paid customers | 4 |
| Served count | 4 |
| Unhappy departures | 0 |
| Revenue | $51.00 |
| Average satisfaction | 80.00 |
| Turns used | 18 |
| Final outcome | VICTORY! |

### Observations

- Customer 1 was seated at Table 1 and ordered a Salad.
- The Chef prepared the Salad, and the Waiter served it on the following turn.
- Customer 2 ordered a Burger, which was successfully prepared, served and paid for.
- Customer 3 ordered Pasta, which required two preparation units.
- Customer 4 completed payment on Turn 15.
- All victory thresholds were reached by Turn 15.
- The game continued running until Turn 18 before displaying the final result.

**Status: PASS**

**Evidence:** [stage1-victory-output.txt](outputs/stage1-victory-output.txt)

## ST1-02 — Full Gameplay Defeat

**Objective:** Verify that the game correctly handles customer abandonment and produces defeat when the victory conditions are not met.

**Input:** [stage1-defeat-input.txt](inputs/stage1-defeat-input.txt)

### Test Procedure

The Waiter and Chef were instructed to wait during all 18 turns.

No customers were seated, orders were not prepared, and no payments occurred.

### Expected Results

- Waiting customers lose satisfaction.
- Customers abandon the restaurant when their satisfaction reaches zero.
- Revenue remains at $0.00.
- The game completes 18 turns.
- The final outcome is `DEFEAT!`.

### Actual Results

| Metric | Result |
|---|---:|
| Paid customers | 0 |
| Served count | 0 |
| Unhappy departures | 2 |
| Revenue | $0.00 |
| Average satisfaction | N/A |
| Turns used | 18 |
| Final outcome | DEFEAT! |

Customer 1 abandoned the restaurant during Turn 13, followed by Customer 2 during Turn 16.

The game continued until Turn 18 and correctly displayed the defeat outcome.

**Status: PASS**

**Evidence:** [stage1-defeat-output.txt](outputs/stage1-defeat-output.txt)

---

# 6. Stage 2 — VIP and Critic Customer Behaviour

Stage 2 builds on the basic restaurant functionality by introducing specialized customer types.

The additional features include:

- VIP customers receiving a 10% order discount.
- VIP customers losing 5 satisfaction points per waiting turn.
- Critic customers preferring Burger.
- Critic customers losing 12 satisfaction points per waiting turn.
- Regular customers continuing to lose 8 satisfaction points per waiting turn.

Testing was conducted against the historical `stage-2` Git tag.

## ST2-01 — Full Gameplay Victory and Special Customer Behaviour

**Objective:** Verify that Stage 1 functionality remains operational while the new VIP and Critic behaviours work correctly.

**Input:** [stage2-victory-input.txt](inputs/stage2-victory-input.txt)

### Expected Results

1. VIP customers receive the required discount.
2. Critic customers select their preferred Burger.
3. Customer satisfaction changes according to customer type.
4. Orders and payments are processed correctly.
5. The game reaches the victory conditions by the end of Turn 18.

### VIP Discount Verification

VIP Customer 2 ordered a Burger during Turn 5.

```text
Original Burger price = $15.00
VIP discount          = 10%

Final bill = $15.00 × 0.90
           = $13.50
```

The console displayed a final bill of $13.50.

Customer 2 subsequently paid $13.50 during Turn 8.

**Result: PASS**

### Critic Behaviour Verification

Critic Customer 3 arrived during Turn 7.

The test confirmed that:

- The Critic ordered Burger during Turn 8.
- The Burger was billed at $15.00.
- The Critic's satisfaction decreased from 88 to 76 between Turns 8 and 9.
- Customer 3 successfully paid $15.00 during Turn 11.

**Result: PASS**

### Actual Final Results

| Metric | Result |
|---|---:|
| Paid customers | 4 |
| Served count | 4 |
| Unhappy departures | 0 |
| Revenue | $46.50 |
| Average satisfaction | 83.50 |
| Turns used | 18 |
| Final outcome | VICTORY! |

The four completed payments were:

| Customer | Type | Payment |
|---|---|---:|
| Customer 1 | Regular | $9.00 |
| Customer 2 | VIP | $13.50 |
| Customer 3 | Critic | $15.00 |
| Customer 4 | Regular | $9.00 |
| **Total** | | **$46.50** |

The game reached all victory requirements during Turn 14 and continued until Turn 18.

**Status: PASS**

**Evidence:** [stage2-victory-output.txt](outputs/stage2-victory-output.txt)

## ST2-02 — Gameplay Defeat and Customer Satisfaction

**Objective:** Verify the different satisfaction penalties introduced in Stage 2 and ensure the defeat conditions work correctly.

**Input:** [stage1-defeat-input.txt](inputs/stage1-defeat-input.txt)

### Test Procedure

All Waiter and Chef actions were set to Wait throughout the 18-turn game.

This allowed us to observe satisfaction changes for the different customer types without additional restaurant actions affecting the results.

### Satisfaction Verification

| Customer Type | Satisfaction Before | Satisfaction After | Decrease |
|---|---:|---:|---:|
| Regular | 44 | 36 | 8 |
| VIP | 80 | 75 | 5 |
| Critic | 88 | 76 | 12 |

The observed decreases matched the expected satisfaction penalties.

### Actual Final Results

| Metric | Result |
|---|---:|
| Paid customers | 0 |
| Served count | 0 |
| Unhappy departures | 2 |
| Revenue | $0.00 |
| Average satisfaction | N/A |
| Turns used | 18 |
| Final outcome | DEFEAT! |

Customer 1 abandoned the restaurant during Turn 13, while Critic Customer 3 abandoned during Turn 15.

**Status: PASS**

**Evidence:** [stage2-defeat-output.txt](outputs/stage2-defeat-output.txt)

---

# 7. Stage 3 — Host, ComboMeal and Happy Hour

Stage 3 introduces several additional features:

- A Host role capable of seating customers.
- A SeatingService used to handle seating operations.
- ComboMeal orders consisting of two dishes.
- Happy Hour discounts and satisfaction recovery.
- Combined VIP and Happy Hour discounts.

Testing was performed using the reorganized Stage 3 working version.

## ST3-10 — Full Gameplay Victory

**Objective:** Verify that the final game implementation handles the complete restaurant workflow and reaches victory after 18 turns.

**Input:** [stage3-victory-input.txt](inputs/stage3-victory-input.txt)

### Expected Results

- At least 4 customers pay.
- Revenue reaches at least $45.00.
- Average paid satisfaction is at least 60.
- The game completes all 18 turns.
- Stage 3 features function correctly during gameplay.

### Actual Final Results

| Metric | Result |
|---|---:|
| Paid customers | 5 |
| Served count | 5 |
| Unhappy departures | 0 |
| Revenue | $69.00 |
| Average satisfaction | 87.60 |
| Turns used | 18 |
| Final outcome | VICTORY! |

### Features Verified

**Host Seating**

The Host successfully seated customers during the gameplay sequence.

**Happy Hour Discount**

Happy Hour was activated on Turn 1 for Turns 1 and 2.

Customer 1 ordered a Salad:

```text
Original price = $9.00
Happy Hour     = 20% discount

Final bill = $9.00 × 0.80
           = $7.20
```

Customer 1 successfully paid $7.20.

**Customer Satisfaction Recovery**

Customer 1's satisfaction reached 100 during the active Happy Hour period.

**VIP Discount**

Customer 2 received a 10% discount on a Burger.

```text
Original price = $15.00
Final bill     = $13.50
```

**ComboMeal Preparation**

Customer 4 ordered a ComboMeal consisting of Salad and Pasta.

The ComboMeal:

- Was billed at $24.30.
- Required three preparation units.
- Progressed through preparation states before becoming ready.
- Was successfully served and paid for.

**Automatic Payment**

Payments were completed only after customers became eligible.

Tables were released after successful payment.

### Payment Summary

| Customer | Type | Amount Paid |
|---|---|---:|
| Customer 1 | Regular | $7.20 |
| Customer 2 | VIP | $13.50 |
| Customer 3 | Critic | $15.00 |
| Customer 4 | Regular | $24.30 |
| Customer 5 | Regular | $9.00 |
| **Total** | | **$69.00** |

**Status: PASS**

**Evidence:** [stage3-victory-output.txt](outputs/stage3-victory-output.txt)

## ST3-11 — Invalid Input Handling and Full Defeat

**Objective:** Verify that invalid inputs are rejected without crashing the game and that the defeat conditions remain correct.

**Input:** [stage3-defeat-invalid-input.txt](inputs/stage3-defeat-invalid-input.txt)

### Invalid Input Tests

| Input Scenario | Expected Behaviour | Actual Behaviour | Status |
|---|---|---|---|
| Invalid Happy Hour option | Reject and redisplay menu | Menu displayed again | PASS |
| Invalid Host option | Reject and allow retry | Retry allowed | PASS |
| Invalid customer number | Reject seating attempt | Error displayed and retry allowed | PASS |
| Invalid Waiter option | Reject and allow retry | Retry allowed | PASS |
| Invalid Chef option | Reject and allow retry | Retry allowed | PASS |

Invalid choices did not terminate the game, and the affected menus were presented again.

### Defeat Scenario

After testing invalid input recovery, the remaining turns were completed using Wait actions.

No orders were served and no payments were collected.

### Actual Final Results

| Metric | Result |
|---|---:|
| Paid customers | 0 |
| Served count | 0 |
| Unhappy departures | 2 |
| Revenue | $0.00 |
| Average satisfaction | N/A |
| Turns used | 18 |
| Final outcome | DEFEAT! |

Customer 1 abandoned during Turn 13, and Critic Customer 3 abandoned during Turn 15.

The game successfully completed all 18 turns.

**Status: PASS**

**Evidence:** [stage3-defeat-output.txt](outputs/stage3-defeat-output.txt)

## ST3-12 — Combined VIP and Happy Hour Discounts

**Objective:** Verify the combined VIP and Happy Hour discount calculation, along with Happy Hour duration and one-time activation restrictions.

**Input:** [stage3-combined-discount-input.txt](inputs/stage3-combined-discount-input.txt)

### Test Scenario

Happy Hour was activated on Turn 4, when VIP Customer 2 arrived.

Customer 2 was seated and ordered a Burger with an original price of $15.00.

### Expected Calculation

```text
Original price       = $15.00

After VIP discount:
$15.00 × 0.90        = $13.50

After Happy Hour:
$13.50 × 0.80        = $10.80

Expected final bill  = $10.80
```

### Actual Results

| Test Condition | Expected | Actual | Status |
|---|---|---|---|
| Happy Hour activation | Turns 4–5 | Turns 4–5 | PASS |
| Combined discount | $10.80 | $10.80 | PASS |
| VIP payment | $10.80 | $10.80 | PASS |
| Happy Hour expiry | Turn 6 | Turn 6 | PASS |
| One-time activation | No further activation menu | Menu no longer offered | PASS |

The actual order bill and payment both matched the expected amount of $10.80.

The activation menu did not appear again following the initial activation, confirming the one-time activation restriction through the normal gameplay interface.

### Actual Final Results

| Metric | Result |
|---|---:|
| Paid customers | 1 |
| Served count | 1 |
| Unhappy departures | 2 |
| Revenue | $10.80 |
| Average satisfaction | 100.00 |
| Turns used | 18 |
| Final outcome | DEFEAT! |

The defeat was intentional because this test focused on discounts rather than meeting the victory requirements.

**Status: PASS**

**Evidence:** [stage3-combined-discount-output.txt](outputs/stage3-combined-discount-output.txt)

---

# 8. Reproducing the Tests in Eclipse

## 8.1 Selecting the Correct Project Version

The historical Stage 1 and Stage 2 tests require their corresponding Git versions.

To access these versions:

1. Open Eclipse's **Git Repositories** view.
2. Use a separate testing clone of the repository.
3. Expand **Tags**.
4. Check out `stage-1` or `stage-2`, depending on the test.
5. Ensure the Java project is configured correctly and compiles.
6. Run the `RestaurantRushGame` main class.

Testing historical versions in a separate clone prevents changes to the main development repository.

For the recorded Stage 3 tests, use the reorganized Stage 3 Java project.

## 8.2 Configuring Automated Console Input

1. Open **Run > Run Configurations**.
2. Under **Java Application**, create or select a configuration for `RestaurantRushGame`.
3. Select the correct Java project in the **Main** tab.
4. Open the **Common** tab.
5. Under **Standard Input and Output**, enable **Input File**.
6. Select the corresponding test file from `tests/inputs`.
7. Enable **Output File**.
8. Select the corresponding destination in `tests/outputs`.
9. Leave **Append** unchecked.
10. Click **Apply**, followed by **Run**.

Eclipse supplies the predetermined menu selections to the program and records the console output.

## 8.3 Checking the Results

After the game finishes:

1. Refresh the project in Eclipse.
2. Open the generated output file in `tests/outputs`.
3. Check the startup message to confirm the correct game stage.
4. Examine the relevant customer actions and feature behaviours.
5. Scroll to the final results.
6. Compare the observed results with the expected results in this document.

A test is marked as PASS when the recorded output matches its expected outcome.

---

# 9. Test Coverage and Limitations

The seven automated scenarios provide functional evidence for major requirements across all three development stages.

They cover:

- Complete victory and defeat scenarios.
- Customer arrivals and waiting.
- Seating and table availability.
- Order preparation and serving.
- Automatic payments and revenue.
- Customer satisfaction and abandonment.
- VIP discounts and satisfaction penalties.
- Critic ordering and satisfaction penalties.
- Host seating.
- ComboMeal preparation and payment.
- Happy Hour discounts and duration.
- Combined discount calculation.
- Selected invalid-input handling.

However, the tests are not exhaustive unit tests.

Not every possible invalid input, customer-state transition or combination of actions has been tested.

The Happy Hour one-time activation restriction was verified through the normal gameplay interface rather than through a separate unit test of the policy class.

Stage 1 and Stage 2 were tested using their historical Git tags. The Stage 3 scenarios were tested on the reorganized working version; independent execution of the historical `stage-3` tag has not been recorded in this document.

These limitations should be considered when interpreting the results.

---

# 10. Conclusion

We completed seven automated functional test scenarios across the three development stages of Restaurant Rush.

All seven scenarios produced their expected outcomes.

Stage 1 demonstrated that the basic restaurant operations could achieve both victory and defeat.

Stage 2 demonstrated that the existing gameplay continued working after VIP and Critic customers were introduced, with the expected discounts and satisfaction behaviour.

Stage 3 demonstrated the Host, ComboMeal and Happy Hour features, including combined discount calculations and selected input-validation cases.

By storing the input sequences and complete console logs, we made the tested scenarios reproducible and easier to review as part of our SC2002 submission.
