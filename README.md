
# Restaurant Rush — SC2002 Group Assignment

A Java command-line, turn-based restaurant management game developed for NTU SC2002 Object-Oriented Design & Programming.

## Project Overview

Restaurant Rush is a game where the player acts as a restaurant manager, assigning tasks to staff and deciding which customers to prioritize.

**Game objectives:**
- Complete all 18 turns.
- Serve and collect payment from at least 4 customers.
- Earn at least $45 in revenue.
- Maintain an average paid-customer satisfaction of at least 60.

## Build / Run

### Using Eclipse IDE

1. Clone this GitHub repository.
2. Open Eclipse IDE.
3. Select **File → Import → Existing Projects into Workspace**.
4. Select the cloned repository directory.
5. Import `SC2002_Assignment`.
6. Expand `src → (default package)`.
7. Right-click `RestaurantRushGame.java`.
8. Select **Run As → Java Application**.

**Main class:** `RestaurantRushGame.java`

**Source directory:** `src/`

**Output directory:** `bin/`


## Development Stages

Restaurant Rush was developed in three stages.
Each stage is preserved using a Git tag.

| Stage | GitHub Version | Features |
|---|---|---|
| Stage 1 | [View Stage 1](https://github.com/u-zhaofeng/SC2002-Assignment/tree/stage-1) | Regular customers, Waiter, Chef, Cashier, restaurant operations |
| Stage 2 | [View Stage 2](https://github.com/u-zhaofeng/SC2002-Assignment/tree/stage-2) | VIP and Critic customers |
| Stage 3 | [View Stage 3](https://github.com/u-zhaofeng/SC2002-Assignment/tree/stage-3) | Host, ComboMeal and Happy Hour |


### Stage 1 — Basic Restaurant Operation

Includes the basic restaurant management game with
regular customers, staff assignments, food preparation,
serving, payment, satisfaction and victory/defeat.

### Stage 2 — Customer Behaviour

Extends Stage 1 by introducing VIP and Critic customers
with different satisfaction and pricing behaviours.

### Stage 3 — New Role and Menu Policy

Extends Stage 2 by introducing:
- Host: An additional staff member who can seat customers.
- ComboMeal: A combination of two existing menu items.
- Happy Hour: Temporary discounts and satisfaction recovery.


## Repository Structure

```text
SC2002-Assignment/
├── src/
├── docs/
│   └── uml/
│       ├── class-diagrams/
│       └── sequence-diagrams/
├── tests/
├── report/
├── presentation/
├── README.md
├── .classpath
├── .project
└── .gitignore
```


## UML Diagrams

| Diagram | Stage |
|---|---|
| [Class Diagram 1](docs/uml/class-diagrams/stage-1/stage-1.png) | Stage 1 |
| [Class Diagram 2](docs/uml/class-diagrams/stage-2/stage-2.png) | Stage 2 |
| [Class Diagram 3](docs/uml/class-diagrams/stage-3/stage-3.png) | Stage 3 |
| [Sequence Diagram](docs/uml/sequence-diagrams/RestaurantRush_Stage3_Sequence_Diagram.png) | Stage 3 |





## Stage Comparisons

Restaurant Rush was developed in three stages, with each
stage introducing new requirements while building upon
the existing implementation.

The following documentation outlines the changes made
between each stage, including newly introduced classes,
modifications to existing classes, their responsibilities,
and the object-oriented design considerations involved.

### Design Evolution

| Transition | Main Changes | Documentation |
|---|---|---|
| Stage 1 → Stage 2 | VIP and Critic customer behaviour | [View Documentation](docs/stages/stage-1-to-2.md) |
| Stage 2 → Stage 3 | Host, ComboMeal and Happy Hour | [View Documentation](docs/stages/stage-2-to-3.md) |


### GitHub Code Comparisons

The following links show the actual code changes
between the three tagged versions:

- [Compare Stage 1 → Stage 2](https://github.com/u-zhaofeng/SC2002-Assignment/compare/stage-1...stage-2)
- [Compare Stage 2 → Stage 3](https://github.com/u-zhaofeng/SC2002-Assignment/compare/stage-2...stage-3)



## Testing


Automated gameplay tests and their results are documented in the [Testing Documentation](tests/README.md).

## Presentation

Video link: To be added.

Video timestamps will be added after recording.

## Group Members and AI Histories

To be completed with each member's GitHub branch and full AI conversation history.
