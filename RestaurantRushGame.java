
import java.util.ArrayList;
import java.util.Set;
import java.util.Scanner;

class RestaurantRushGame {

    private Scanner scanner = new Scanner(System.in);
    private int userChoice = 0;
    private int totalTurns;
    private int revenueCents;

    private final ArrayList<MenuItem> menu = new ArrayList<>();
    private final ArrayList<Table> tables = new ArrayList<>();

    private int currCustomerNo = 1;
    private WaitingQueue waitingQueue;
    private final ArrayList<Customer> paidCustomers =
            new ArrayList<>();

    private Set<Integer> arrivalTurns;

    private Waiter waiter;
    private Chef chef;
    private Cashier cashier;

    // Additional statistics for Stage 1
    private int servedCount = 0;
    private int unhappyDepartures = 0;

    private void DefaultGameSetup() {
        totalTurns = 18;
        revenueCents = 0;

        waitingQueue = new WaitingQueue();

        arrivalTurns = Set.of(1, 4, 7, 10, 13, 16);

        tables.add(new Table(1));
        tables.add(new Table(2));

        menu.add(new MenuItem("Salad", 1, 900));
        menu.add(new MenuItem("Burger", 1, 1500));
        menu.add(new MenuItem("Pasta", 2, 1800));

        waiter = new Waiter();
        chef = new Chef();
        cashier = new Cashier();
    }

    public static void main(String[] args) {
        RestaurantRushGame game = new RestaurantRushGame();

        game.DefaultGameSetup();
        game.StartGame();
    }

    private void StartGame() {

        System.out.println("Restaurant Rush Game Starting...");
        System.out.println("Stage 1 - Basic Restaurant Operation");

        for (int turnNo = 1; turnNo <= totalTurns; turnNo++) {

            System.out.println(
                    "\n========= Turn " + turnNo + " =========");

            // Start-of-turn display
            displayGameState();

            // Step 1: Arrivals
            System.out.println("\n--- Arrivals ---");

            if (arrivalTurns.contains(turnNo)) {
                Customer newCustomer =
                        new Customer(currCustomerNo);

                waitingQueue.addCustomer(newCustomer);

                System.out.println("Customer "
                        + currCustomerNo + " arrives.");

                currCustomerNo++;
            }
            else {
                System.out.println("No new arrivals.");
            }

            // Step 2 and 3: Manager choices,
            // Waiter first, then Chef
            System.out.println("\n--- Manager Choices ---");

            System.out.println("\nAvailable Waiter Tasks:");
            for (String task :
                    waiter.getTasks(waitingQueue, tables)) {
                System.out.println("- " + task);
            }

            processWaiterChoice(turnNo);

            System.out.println("\nAvailable Chef Tasks:");
            for (String task : chef.getTasks(tables)) {
                System.out.println("- " + task);
            }

            processChefChoice();

            // Step 4: Eating and payment
            processEatingAndPayment(turnNo);

            // Step 5: Satisfaction and abandonment
            processSatisfaction();

            // Step 6: End-of-turn summary
            displayTurnResults(turnNo);
        }

        displayFinalResults();
    }

    // Displays all required game information
    private void displayGameState() {

        System.out.printf(
                "Revenue: $%.2f%n", revenueCents / 100.0);

        System.out.println(
                "Average Paid Customers Satisfaction: "
                + calculateAvgCustSatisfaction());

        System.out.println("\nWaiting Queue:");

        if (waitingQueue.getCustomerInQueue().isEmpty()) {
            System.out.println("(empty)");
        }
        else {
            for (Customer waitingCustomer :
                    waitingQueue.getCustomerInQueue()) {

                System.out.println(
                        "Customer "
                        + waitingCustomer.getCustomerNo()
                        + " | Satisfaction: "
                        + waitingCustomer.getSatisfaction()
                        + " | Status: "
                        + waitingCustomer.getCustomerStatus());
            }
        }

        System.out.println("\nTables:");

        for (Table table : tables) {

            if (table.getCustomer() == null) {
                System.out.println(
                        "Table " + table.getTableNo() + ": Empty");
            }
            else {
                Customer cust = table.getCustomer();

                System.out.printf(
                        "Table %d: Customer %d | Satisfaction %d"
                        + " | Status: %s | Order: %s%n",
                        table.getTableNo(),
                        cust.getCustomerNo(),
                        cust.getSatisfaction(),
                        cust.getCustomerStatus(),
                        constructOrderString(table));
            }
        }

        System.out.println("\nAvailable Tasks:");

        System.out.println("Waiter:");
        for (String task :
                waiter.getTasks(waitingQueue, tables)) {
            System.out.println("- " + task);
        }

        System.out.println("Chef:");
        for (String task : chef.getTasks(tables)) {
            System.out.println("- " + task);
        }

        System.out.println(
                "Cashier: Automatic payment when eligible");
    }

    // Waiter can perform at most one valid task
    private void processWaiterChoice(int turnNo) {

        while (true) {

            System.out.println(
                    "\nWaiter:\n"
                    + "1. Seat queuing customer\n"
                    + "2. Take order\n"
                    + "3. Serve an order\n"
                    + "4. Wait");

            userChoice = readInt("Choose Waiter action: ");

            switch (userChoice) {

                case 1: {
                    int customerNo = readInt(
                            "Enter customer number: ");

                    int tableNo = readInt(
                            "Enter table number: ");

                    Customer cust =
                            waitingQueue.findCustomer(customerNo);

                    Table table = findTable(tableNo);

                    if (waiter.seatCustomer(
                            cust, table, waitingQueue)) {

                        System.out.println(
                                "Customer " + customerNo
                                + " seated at Table " + tableNo);
                        return;
                    }

                    System.out.println(
                            "Invalid: customer not queued "
                            + "or table is occupied.");
                    break;
                }

                case 2: {
                    int tableNo = readInt(
                            "Enter table number: ");

                    Table table = findTable(tableNo);

                    if (table == null ||
                            table.getCustomer() == null ||
                            table.getCustomer().getOrder() != null) {

                        System.out.println(
                                "Invalid: no customer waiting "
                                + "to order at that table.");
                        break;
                    }

                    Customer cust = table.getCustomer();

                    // Food is chosen by the customer,
                    // not the manager.
                    MenuItem orderItem =
                            chooseCustomerFood(cust);

                    if (waiter.takeOrder(table, orderItem)) {

                        System.out.printf(
                                "Customer %d chooses %s ($%.2f)%n",
                                cust.getCustomerNo(),
                                orderItem.getName(),
                                orderItem.getPrice() / 100.0);

                        return;
                    }

                    System.out.println(
                            "Unable to take order.");
                    break;
                }

                case 3: {
                    int tableNo = readInt(
                            "Enter table number: ");

                    Table table = findTable(tableNo);

                    if (waiter.serveOrder(table, turnNo)) {

                        servedCount++;

                        System.out.println(
                                "Order served to Customer "
                                + table.getCustomer()
                                        .getCustomerNo());

                        return;
                    }

                    System.out.println(
                            "Invalid: order is not READY.");
                    break;
                }

                case 4:
                    System.out.println("Waiter waits.");
                    return;

                default:
                    System.out.println(
                            "Invalid choice. Enter 1 to 4.");
            }

            // Invalid choices do not consume the task.
            System.out.println("Please try again.");
        }
    }

    // Chef can prepare one unit of one order
    private void processChefChoice() {

        while (true) {

            System.out.println(
                    "\nChef:\n"
                    + "1. Advance order\n"
                    + "2. Wait");

            userChoice = readInt("Choose Chef action: ");

            switch (userChoice) {

                case 1: {
                    int tableNo = readInt(
                            "Enter table number: ");

                    Table table = findTable(tableNo);

                    if (chef.prepareOrder(table)) {

                        System.out.println(
                                "Chef prepared one unit.");

                        System.out.println(
                                "Order: "
                                + constructOrderString(table));

                        return;
                    }

                    System.out.println(
                            "Invalid: no PREPARING order "
                            + "at that table.");
                    break;
                }

                case 2:
                    System.out.println("Chef waits.");
                    return;

                default:
                    System.out.println(
                            "Invalid choice. Enter 1 or 2.");
            }

            System.out.println("Please try again.");
        }
    }

    // Customer food choice is deterministic
    private MenuItem chooseCustomerFood(Customer cust) {

        int index =
                (cust.getCustomerNo() - 1) % menu.size();

        return menu.get(index);
    }

    // Finds a table by its number
    private Table findTable(int tableNo) {

        for (Table table : tables) {
            if (table.getTableNo() == tableNo) {
                return table;
            }
        }

        return null;
    }

    // Handles eating and automatic payment
    private void processEatingAndPayment(int turnNo) {

        System.out.println("\n--- Eating and Payment ---");

        for (Table table : tables) {

            Customer cust = table.getCustomer();

            if (cust == null) {
                continue;
            }

            if (cust.getCustomerStatus()
                    == CustomerStatus.SERVED
                    && cust.getServedTurn() < turnNo) {

                cust.markReadyToPay(turnNo);

                System.out.println(
                        "Customer " + cust.getCustomerNo()
                        + " finished eating and is READY_TO_PAY.");
            }
        }

        // Cashier handles at most one eligible customer.
        Customer paidCustomer =
                cashier.handlePayments(tables, turnNo);

        if (paidCustomer != null) {

            int priceCents = paidCustomer.getOrder()
                    .getOrderItem().getPrice();

            revenueCents += priceCents;

            paidCustomers.add(paidCustomer);

            System.out.printf(
                    "Customer %d paid $%.2f%n",
                    paidCustomer.getCustomerNo(),
                    priceCents / 100.0);

            System.out.println("Table has been freed.");
        }
        else {
            System.out.println(
                    "No customer eligible for payment.");
        }
    }

    // Regular customers lose 8 satisfaction
    // while waiting or seated but not served
    private void processSatisfaction() {

        System.out.println(
                "\n--- Satisfaction and Abandonment ---");

        ArrayList<Customer> waitingCustomers =
                new ArrayList<>(
                        waitingQueue.getCustomerInQueue());

        for (Customer cust : waitingCustomers) {

            cust.decreaseSatisfaction(8);

            if (cust.getSatisfaction() == 0) {

                waitingQueue.removeCustomer(cust);

                abandonCustomer(cust);
            }
        }

        for (Table table : tables) {

            Customer cust = table.getCustomer();

            if (cust == null) {
                continue;
            }

            if (cust.getCustomerStatus()
                    == CustomerStatus.SEATED) {

                cust.decreaseSatisfaction(8);

                if (cust.getSatisfaction() == 0) {

                    abandonCustomer(cust);

                    table.clearTable();
                }
            }
        }
    }

    private void abandonCustomer(Customer cust) {

        cust.setCustomerStatus(CustomerStatus.LEFT);

        if (cust.getOrder() != null &&
                cust.getOrder().getOrderStatus()
                        != OrderStatus.PAID) {

            cust.getOrder().setOrderStatus(
                    OrderStatus.CANCELLED);
        }

        unhappyDepartures++;

        System.out.println(
                "Customer " + cust.getCustomerNo()
                + " abandoned the restaurant.");
    }

    // Handles non-numeric input without crashing
    private int readInt(String message) {

        while (true) {

            System.out.print(message);

            if (!scanner.hasNextLine()) {
                throw new IllegalStateException(
                        "Input ended before game completion.");
            }

            String input = scanner.nextLine().trim();

            try {
                return Integer.parseInt(input);
            }
            catch (NumberFormatException e) {
                System.out.println(
                        "Invalid input. Enter a whole number.");
            }
        }
    }

    // Average satisfaction of paid customers
    private String calculateAvgCustSatisfaction() {

        if (paidCustomers.isEmpty()) {
            return "N/A";
        }

        return String.format(
                "%.2f", getAvgCustSatisfaction());
    }

    private double getAvgCustSatisfaction() {

        if (paidCustomers.isEmpty()) {
            return 0;
        }

        int total = 0;

        for (Customer cust : paidCustomers) {
            total += cust.getSatisfaction();
        }

        return (double) total / paidCustomers.size();
    }

    // Display order details
    private String constructOrderString(Table table) {

        String returnStr = "none";

        if (table.getCustomer() != null &&
                table.getCustomer().getOrder() != null) {

            Order order = table.getCustomer().getOrder();

            returnStr =
                    order.getOrderItem().getName()
                    + " "
                    + order.getOrderStatus().name();

            if (order.getOrderStatus()
                    == OrderStatus.PREPARING) {

                returnStr += String.format(
                        " %d/%d",
                        order.getUnitsDone(),
                        order.getOrderItem()
                                .getPreparationUnits());
            }
        }

        return returnStr;
    }

    private void displayTurnResults(int turnNo) {

        System.out.println(
                "\n--- End of Turn " + turnNo + " ---");

        System.out.println("Served Count: " + servedCount);

        System.out.println(
                "Unhappy Departures: " + unhappyDepartures);

        System.out.println(
                "Paid Customers: " + paidCustomers.size());

        System.out.printf(
                "Revenue: $%.2f%n", revenueCents / 100.0);

        System.out.println(
                "Average Paid Satisfaction: "
                + calculateAvgCustSatisfaction());

        System.out.println(
                "Turns Used: " + turnNo);
    }

    private void displayFinalResults() {

        boolean victory =
                paidCustomers.size() >= 4
                && revenueCents >= 4500
                && getAvgCustSatisfaction() >= 60;

        System.out.println(
                "\n========= FINAL RESULTS =========");

        if (victory) {
            System.out.println("VICTORY!");
        }
        else {
            System.out.println("DEFEAT!");
        }

        System.out.println(
                "Paid Customers: " + paidCustomers.size());

        System.out.println(
                "Served Count: " + servedCount);

        System.out.println(
                "Unhappy Departures: " + unhappyDepartures);

        System.out.printf(
                "Revenue: $%.2f%n", revenueCents / 100.0);

        System.out.println(
                "Average Satisfaction: "
                + calculateAvgCustSatisfaction());

        System.out.println(
                "Turns Used: " + totalTurns);
    }
}
