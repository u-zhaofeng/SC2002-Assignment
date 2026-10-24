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
    private final ArrayList<Customer> paidCustomers = new ArrayList<>();
    private Set<Integer> arrivalTurns;
    private Waiter waiter;
    private Chef chef;
    private Cashier cashier;

    private void DefaultGameSetup(){
        totalTurns = 18;
        revenueCents = 0;
        waitingQueue = new WaitingQueue();
        arrivalTurns = Set.of(1, 4, 7, 10, 13, 16);

        // add 2 tables
        tables.add(new Table(1));
        tables.add(new Table(2));
        // add 3 menu items
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
        for (int turnNo = 1; turnNo <= totalTurns; turnNo++) {
            System.out.println("\n========= Turn " + (turnNo) + " =========");
            System.out.printf("Revenue: $%.2f%n", revenueCents / 100.0);
            System.out.println("Average Paid Customers Satisfaction: " + calculateAvgCustSatisfaction() + "\n");
            System.out.println("Waiting Queue: ");
            if (waitingQueue.getCustomerInQueue().isEmpty()) {
                System.out.println("(empty)");
            }
            else {
                for (Customer waitingCustomer : waitingQueue.getCustomerInQueue()) {
                    System.out.println("Customer " + waitingCustomer.getCustomerNo() + " | " + "Satisfaction: " + waitingCustomer.getSatisfaction());
                }
            }

            System.out.println("\nTables: ");
            for (Table table : tables) {
                if (table.getCustomer() == null) {
                    System.out.println("Table " + table.getTableNo() + ": " + "Empty");
                }
                else {
                    System.out.printf(
                        "Table %d: Customer %d | Satisfaction %d | Order: %s%n", 
                        table.getTableNo(),
                        table.getCustomer().getCustomerNo(),
                        table.getCustomer().getSatisfaction(),
                        constructOrderString(table)
                    );
                }
            }
            System.out.println("\nAvailable Tasks: \n");
            System.out.println("Waiter: ");
            for (String tasks : waiter.getTasks(waitingQueue, tables)){
                System.out.println(tasks);
            }

            System.out.println("\nChef: ");
            for (String tasks : chef.getTasks(tables)){
                System.out.println(tasks);
            }


            System.out.println("\n--- Arrivals ---");
            if (arrivalTurns.contains(turnNo)){
                Customer newCustomer = new Customer(currCustomerNo);
                waitingQueue.addCustomer(newCustomer);
                System.out.println("Customer " + currCustomerNo + " arrives");
                currCustomerNo++;
            }

            System.out.println("\n--- Manager Choices ---");
            System.out.println(
                "Waiter:\n1. Seat queuing customer\n2. Take order\n3. Serve a order\n4. Wait"
            );
            userChoice = scanner.nextInt();
            switch (userChoice) {
                // process user choice for waiter
                case 1:
            } 
            System.out.println(
                "Chef:\n1. Advance order\n2. Wait"
            );
            userChoice = scanner.nextInt();
            switch (userChoice){
                case 1:
                // process user choice for chef
            }



        }
    }

    private String calculateAvgCustSatisfaction(){
        int total = 0;
        if (!paidCustomers.isEmpty()){
            for (Customer customer : paidCustomers){
                total += customer.getSatisfaction();
            }
            total /= paidCustomers.size();
        }
        return total == 0? "N/A" : Integer.toString(total);
    }

    private String constructOrderString(Table table){
        String returnStr = "none";
        if (table.getCustomer().getOrder() != null){
            returnStr = table.getCustomer().getOrder().getOrderItem().getName() + " " + table.getCustomer().getOrder().getOrderStatus().name();
            if (table.getCustomer().getOrder().getOrderStatus() == OrderStatus.PREPARING){
                returnStr += String.format(" %d/%d", table.getCustomer().getOrder().getUnitsDone(), table.getCustomer().getOrder().getOrderItem().getPreparationUnits());
            }
        }
        return returnStr;
    }



}