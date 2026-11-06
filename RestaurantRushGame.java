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
    private CustomerFactory customerFactory;
    private Host host;
    private HappyHourPolicy happyHour;

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
        menu.add(new ComboMeal(menu.get(0), menu.get(2)));

        waiter = new Waiter();
        chef = new Chef();
        cashier = new Cashier();
        customerFactory = new CustomerFactory();
        host = new Host();
        happyHour = new HappyHourPolicy();
    }

    public static void main(String[] args) {
        RestaurantRushGame game = new RestaurantRushGame();

        game.DefaultGameSetup();
        game.StartGame();
    }

    private void StartGame() {

        System.out.println("Restaurant Rush Game Starting...");
        System.out.println("Stage 3 - Host, ComboMeal and Happy Hour");

        for (int turnNo = 1; turnNo <= totalTurns; turnNo++) {

            System.out.println(
                    "\n========= Turn " + turnNo + " =========");

            displayGameState(turnNo);
            processHappyHourChoice(turnNo);

            System.out.println("\n--- Arrivals ---");

            if (arrivalTurns.contains(turnNo)) {
                Customer newCustomer =
                        customerFactory.createCustomer(currCustomerNo);

                waitingQueue.addCustomer(newCustomer);

                System.out.println("Customer "
                        + currCustomerNo + " ("
                        + newCustomer.getTypeLabel() + ") arrives.");

                currCustomerNo++;
            }
            else {
                System.out.println("No new arrivals.");
            }

            happyHour.recoverWaitingCustomers(waitingQueue, tables, turnNo);

            System.out.println("\n--- Manager Choices ---");

            System.out.println("\nAvailable Host Tasks:");
            for (String task : host.getTasks(waitingQueue, tables)) {
                System.out.println("- " + task);
            }
            processHostChoice();

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

            processEatingAndPayment(turnNo);

            processSatisfaction();

            displayTurnResults(turnNo);
        }

        displayFinalResults();
    }

    private void displayGameState(int turnNo) {

        System.out.printf(
                "Revenue: $%.2f%n", revenueCents / 100.0);

        System.out.println("Happy Hour: "
                + (happyHour.isActive(turnNo)
                    ? "ACTIVE (" + happyHour.remainingTurns(turnNo) + " turn(s) left)"
                    : (happyHour.hasBeenActivated() ? "expired" : "available")));

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
                        + " (" + waitingCustomer.getTypeLabel() + ")"
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
                        "Table %d: Customer %d (%s) | Satisfaction %d"
                        + " | Status: %s | Order: %s%n",
                        table.getTableNo(),
                        cust.getCustomerNo(),
                        cust.getTypeLabel(),
                        cust.getSatisfaction(),
                        cust.getCustomerStatus(),
                        constructOrderString(table));
            }
        }

        System.out.println("\nAvailable Tasks:");

        System.out.println("Host:");
        for (String task : host.getTasks(waitingQueue, tables)) {
            System.out.println("- " + task);
        }

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

    private void processHappyHourChoice(int turnNo) {
        if (happyHour.hasBeenActivated()) {
            System.out.println("Happy Hour remaining this turn: "
                    + happyHour.remainingTurns(turnNo));
            return;
        }
        while (true) {
            System.out.println("\nHappy Hour (one activation per game):");
            System.out.println("1. Activate now (this turn and next)");
            System.out.println("2. Not now");
            int choice = readInt("Choose Happy Hour action: ");
            if (choice == 1) {
                happyHour.activate(turnNo);
                System.out.println("Happy Hour activated for turns "
                        + turnNo + " and " + (turnNo + 1) + ".");
                return;
            }
            if (choice == 2) {
                return;
            }
            System.out.println("Invalid: enter 1 or 2. Please try again.");
        }
    }

    private void processHostChoice() {
        while (true) {
            System.out.println("\nHost:\n1. Seat queuing customer\n2. Wait");
            int choice = readInt("Choose Host action: ");
            if (choice == 2) {
                System.out.println("Host waits.");
                return;
            }
            if (choice != 1) {
                System.out.println("Invalid: enter 1 or 2. Please try again.");
                continue;
            }
            int customerNo = readInt("Enter customer number: ");
            int tableNo = readInt("Enter table number: ");
            Customer customer = waitingQueue.findCustomer(customerNo);
            Table table = findTable(tableNo);
            if (host.seatCustomer(customer, table, waitingQueue)) {
                System.out.println("Host seated Customer " + customerNo
                        + " at Table " + tableNo);
                return;
            }
            System.out.println("Invalid: customer not queued or table is occupied. Please try again.");
        }
    }

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

                    MenuItem orderItem =
                            cust.chooseFood(menu);

                    int billCents = happyHour.billCents(cust, orderItem, turnNo);
                    if (waiter.takeOrder(table, orderItem, billCents)) {

                        System.out.printf(
                                "Customer %d (%s) chooses %s "
                                + "(menu $%.2f, bill $%.2f)%n",
                                cust.getCustomerNo(),
                                cust.getTypeLabel(),
                                orderItem.getName(),
                                orderItem.getPrice() / 100.0,
                                cust.getOrder().getBillCents() / 100.0);

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

            System.out.println("Please try again.");
        }
    }

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

    private Table findTable(int tableNo) {

        for (Table table : tables) {
            if (table.getTableNo() == tableNo) {
                return table;
            }
        }

        return null;
    }

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

        Customer paidCustomer =
                cashier.handlePayments(tables, turnNo);

        if (paidCustomer != null) {

            int priceCents = paidCustomer.getOrder()
                    .getBillCents();

            revenueCents += priceCents;

            paidCustomers.add(paidCustomer);

            System.out.printf(
                    "Customer %d (%s) paid $%.2f%n",
                    paidCustomer.getCustomerNo(),
                    paidCustomer.getTypeLabel(),
                    priceCents / 100.0);

            System.out.println("Table has been freed.");
        }
        else {
            System.out.println(
                    "No customer eligible for payment.");
        }
    }

    private void processSatisfaction() {

        System.out.println(
                "\n--- Satisfaction and Abandonment ---");

        ArrayList<Customer> waitingCustomers =
                new ArrayList<>(
                        waitingQueue.getCustomerInQueue());

        for (Customer cust : waitingCustomers) {

            cust.applyWaitingPenalty();

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

                cust.applyWaitingPenalty();

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
                + " (" + cust.getTypeLabel() + ")"
                + " abandoned the restaurant.");
    }

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

    private String constructOrderString(Table table) {

        String returnStr = "none";

        if (table.getCustomer() != null &&
                table.getCustomer().getOrder() != null) {

            Order order = table.getCustomer().getOrder();

            returnStr =
                    order.getOrderItem().getName()
                    + " "
                    + order.getOrderStatus().name()
                    + String.format(" (bill $%.2f)",
                            order.getBillCents() / 100.0);

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
